package de.vzg.reposis.digibib;

import org.mycore.common.events.MCRStartupHandler;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Requests to /servlets/MCRDerivateContentTransformerServlet/* should be allowed from everywhere.
 */
public class CORSFilterStarter implements MCRStartupHandler.AutoExecutable, Filter {


    @Override
    public String getName() {
        return "CORSFilterStarter for servlets/MCRDerivateContentTransformerServlet";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void startUp(ServletContext servletContext) {
        if (servletContext == null) {
            return;
        }

        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("MCRDerivateContentTransformerServlet", this);
        corsFilter.addMappingForUrlPatterns(null, false, "/servlets/MCRDerivateContentTransformerServlet/*");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.setHeader("Access-Control-Allow-Origin", "*");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
