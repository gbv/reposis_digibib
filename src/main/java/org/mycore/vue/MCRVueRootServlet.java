package org.mycore.vue;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.mycore.common.content.MCRContent;
import org.mycore.common.content.MCRJDOMContent;
import org.mycore.common.content.MCRURLContent;
import org.mycore.frontend.MCRFrontendUtil;
import org.mycore.frontend.servlets.MCRContentServlet;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>This Servlet can be bound to a URL where a Vue Build App with a createWebHistory() router is present.</p>
 * <p></p>
 * It will pass through resources that exists at this path like javascript and css, but at every other path it will deliver a modified version of the index.html.
 * The index.html is convert to xhtml and then will be wrapped with a MyCoReWebPage, which will produce the surrounding default layout.</p>
 * <p>In the example the vue app is located in: src/main/vue/modul_handbuecher</p>
 * <p>The Router needs to be configured like this:</p>
 * <code>
 * function getContext() { <br>
 * &nbsp;&nbsp;const el = document.createElement('a');<br>
 * &nbsp;&nbsp;el.href = (&lt;any&gt;window).webApplicationBaseURL;<br>
 * &nbsp;&nbsp;return el.pathname;<br>
 * }<br>
 * const router = createRouter({<br>
 * &nbsp;&nbsp;history: createWebHistory(getContext() + "handbuecher/"),<br>
 * &nbsp;&nbsp;routes<br>
 * })
 * </code>
 * <p>The String "handbuecher/" is the location of the vue app below the java application context.</p>
 * <code>
 * &lt;servlet&gt;<br>
 * &nbsp;&nbsp;&lt;servlet-name&gt;MCRVueRootServlet&lt;/servlet-name&gt;<br>
 * &nbsp;&nbsp;&lt;servlet-class&gt;org.mycore.vue.MCRVueRootServlet&lt;/servlet-class&gt;<br>
 * &lt;/servlet&gt;<br>
 * &lt;servlet-mapping&gt;<br>
 * &nbsp;&nbsp;&lt;servlet-name&gt;MCRVueRootServlet&lt;/servlet-name&gt;<br>
 * &nbsp;&nbsp;&lt;url-pattern&gt;/handbuecher/*&lt;/url-pattern&gt;<br>
 * &lt;/servlet-mapping&gt;<br>
 * </code>
 * <p>To change the output destination of the vue compiler process you need to change the vue.config.js</p>
 * <code>
 * const { defineConfig } = require('@vue/cli-service')<br>
 * module.exports = defineConfig({<br>
 * &nbsp;&nbsp;transpileDependencies: true,<br>
 * &nbsp;&nbsp;outputDir: "../../../../target/classes/META-INF/resources/handbuecher",<br>
 * &nbsp;&nbsp;publicPath: "./"<br>
 * });<br>
 * </code>
 */
public class MCRVueRootServlet extends MCRContentServlet {

    @Override
    public MCRContent getContent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String indexHtmlPath = req.getServletPath() + "/index.html";
        URL resource = getServletContext().getResource(req.getServletPath() + pathInfo);

        if (resource != null && !pathInfo.endsWith("/") && !pathInfo.endsWith("index.html")) {
            return new MCRURLContent(resource);
        } else {
            URL indexResource = getServletContext().getResource(indexHtmlPath);
            org.jdom2.Document mycoreWebpage = getIndexDocument(indexResource, MCRFrontendUtil.getBaseURL() + removeLeadingSlash(req.getServletPath()));
            if (pathInfo != null && pathInfo.endsWith("/404")) {
                /* if there is a requested route which does not exist, the app should
                 * redirect to this /404 route the get the actual 404 Code.
                 * see also https://www.youtube.com/watch?v=vjj8B4sq0UI&t=1815s
                 * */
                resp.setStatus(404);
            }
            try {
                return getLayoutService().getTransformedContent(req, resp, new MCRJDOMContent(mycoreWebpage));
            } catch (TransformerException | SAXException e) {
                throw new IOException(e);
            }
        }
    }

    private String removeLeadingSlash(String sp) {
        return sp.startsWith("/") ? sp.substring(1) : sp;
    }

    private org.jdom2.Document getIndexDocument(URL indexResource, String absoluteServletPath) throws IOException {
        try (InputStream indexFileStream = indexResource.openStream()) {
            Document document = Jsoup.parse(indexFileStream, StandardCharsets.UTF_8.toString(), "");
            document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            document.outerHtml();
            org.jdom2.Document jdom = new SAXBuilder().build(new StringReader(document.outerHtml()));
            Element jdomRoot = jdom.getRootElement();
            List<Element> scriptAndLinks = jdomRoot.getChild("head").getChildren().stream().filter(el -> el.getName().equals("script") || el.getName().equals("link")).collect(Collectors.toList()).stream().map(Element::detach).peek(el -> {
                String hrefAttr = el.getAttributeValue("href");
                if (hrefAttr != null) {
                    el.setAttribute("href", absoluteServletPath + "/" + hrefAttr);
                }

                String srcAttr = el.getAttributeValue("src");
                if (srcAttr != null) {
                    el.setAttribute("src", absoluteServletPath + "/" + srcAttr);
                }
            }).collect(Collectors.toList());


            List<Element> bodyContent = new ArrayList<>(jdomRoot.getChild("body").getChildren()).stream().map(Element::detach).collect(Collectors.toList());

            Element webPage = new Element("MyCoReWebPage");
            org.jdom2.Document webpageDoc = new org.jdom2.Document(webPage);

            Element section = new Element("section");
            webPage.addContent(section);
            section.setAttribute("lang", "de", Namespace.XML_NAMESPACE);
            section.addContent(scriptAndLinks).addContent(bodyContent);

            return webpageDoc;
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendFile(HttpServletResponse resp, URL resource) throws IOException {
        try (OutputStream os = resp.getOutputStream()) {
            try (InputStream is = resource.openStream()) {
                is.transferTo(os);
            }
        }
    }


}
