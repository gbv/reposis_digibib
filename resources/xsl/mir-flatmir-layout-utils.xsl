<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:i18n="xalan://org.mycore.services.i18n.MCRTranslation"
    xmlns:mcrver="xalan://org.mycore.common.MCRCoreVersion"
    xmlns:mcrxsl="xalan://org.mycore.common.xml.MCRXMLFunctions"
    exclude-result-prefixes="i18n mcrver mcrxsl">

  <xsl:import href="resource:xsl/layout/mir-common-layout.xsl" />
  <xsl:param name="piwikID" select="'0'" />

  <xsl:template name="mir.navigation">

    <div id="header_box" class="clearfix container">

      <a
        id="project_logo_box"
        class="hidden-xs"
        href="http://www.tu-braunschweig.de"
        title="{i18n:translate('digibib.goToMainSite')}">
        <img
          src="{$WebApplicationBaseURL}images/siegel_rot.png"
          alt="{i18n:translate('digibib.logoTUBS')}" />
      </a>

      <div id="options_nav_box" class="mir-prop-nav">
        <nav>
          <ul class="navbar-nav ml-auto flex-row flex-row-reverse">
            <xsl:call-template name="mir.languageMenu" />
            <xsl:call-template name="mir.loginMenu" />
          </ul>
        </nav>
        <a id="bs-bibliothek" href="https://ub.tu-braunschweig.de/">
          <xsl:value-of select="i18n:translate('digibib.bsLibrary')" />
        </a>
      </div>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="mir-main-nav bg-primary">
      <div class="container">

        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
          <button
            class="navbar-toggler"
            type="button"
            data-toggle="collapse"
            data-target="#mir-main-nav-collapse-box"
            aria-controls="mir-main-nav-collapse-box"
            aria-expanded="false"
            aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>

          <div id="mir-main-nav-collapse-box" class="collapse navbar-collapse mir-main-nav__entries">
            <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
              <xsl:call-template name="digibib.generate_single_menu_entry">
                <xsl:with-param name="menuID" select="'brand'"/>
              </xsl:call-template>
              <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='search']" />
              <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='collections']" />
              <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='publish']" />
              <xsl:call-template name="mir.basketMenu" />
            </ul>
          </div>

          <form
            action="{$WebApplicationBaseURL}servlets/solr/find"
            class="bs-search form-inline"
            role="search">
            <input
              name="condQuery"
              placeholder="{i18n:translate('mir.navsearch.placeholder')}"
              class="form-control search-query"
              type="text" />
            <xsl:choose>
              <xsl:when test="mcrxsl:isCurrentUserInRole('admin') or mcrxsl:isCurrentUserInRole('editor')">
                <input name="owner" type="hidden" value="createdby:*" />
              </xsl:when>
              <xsl:when test="not(mcrxsl:isCurrentUserGuestUser())">
                <input name="owner" type="hidden" value="createdby:{$CurrentUser}" />
              </xsl:when>
            </xsl:choose>
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-search"></i>
            </button>
          </form>

        </nav>
      </div>
    </div>

    <div id="digibib_feedback">
      <a href="mailto:digibib@tu-braunschweig.de">Feedback</a>
    </div>
  </xsl:template>

  <xsl:template name="mir.jumbotwo">
    <!-- do nothing special -->
  </xsl:template>

  <xsl:template name="mir.footer">
    <div class="container">
      <div class="row">
        <div class="col-6 col-sm-9">
          <ul class="internal_links">
            <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='below']/*" mode="footerMenu" />
          </ul>
        </div>
        <div class="col-6 col-sm-3">
          <xsl:variable name="mcr_version" select="concat('MyCoRe ',mcrver:getCompleteVersion())" />
          <div id="powered_by">
            <a id="dini_logo" href="https://www.dini.de/dienste-projekte/dini-zertifikat/" title="{i18n:translate('digibib.diniCertificate2016')}">
              <img alt="Logo DINI-Zertifikat 2016" src="{$WebApplicationBaseURL}images/dini_zertifikat_2016.svg" height="50" />
            </a>
            <a id="mycore_logo" href="http://www.mycore.de">
              <img src="{$WebApplicationBaseURL}mir-layout/images/mycore_logo_powered_120x30_blaue_schrift_frei.png" title="{$mcr_version}" alt="powered by MyCoRe" />
            </a>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>

  <xsl:template name="digibib.generate_single_menu_entry">
    <xsl:param name="menuID" />
    <li class="nav-item">
      <xsl:variable name="activeClass">
        <xsl:choose>
          <xsl:when test="$loaded_navigation_xml/menu[@id=$menuID]/item[@href = $browserAddress ]">
          <xsl:text>active</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>not-active</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <a id="{$menuID}" href="{$WebApplicationBaseURL}{$loaded_navigation_xml/menu[@id=$menuID]/item/@href}" class="nav-link {$activeClass}" >
        <xsl:choose>
          <xsl:when test="$loaded_navigation_xml/menu[@id=$menuID]/item/label[lang($CurrentLang)] != ''">
            <xsl:value-of select="$loaded_navigation_xml/menu[@id=$menuID]/item/label[lang($CurrentLang)]" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$loaded_navigation_xml/menu[@id=$menuID]/item/label[lang($DefaultLang)]" />
          </xsl:otherwise>
        </xsl:choose>
      </a>
    </li>
  </xsl:template>

  <xsl:template name="mir.powered_by">
    <script type="text/javascript" src="{$WebApplicationBaseURL}js/jquery.cookiebar.js"></script>
    <!-- Piwik -->
    <xsl:if test="$piwikID &gt; 0">
      <script type="text/javascript">
        var _paq = _paq || [];
        _paq.push(['setDoNotTrack', true]);
        _paq.push(['trackPageView']);
        _paq.push(['enableLinkTracking']);
        (function() {
        var u="https://matomo.gbv.de/";
        var objectID = '<xsl:value-of select="//site/@ID" />';
        if(objectID != "") {
        _paq.push(["setCustomVariable",1, "object", objectID, "page"]);
        }
        _paq.push(['setTrackerUrl', u+'piwik.php']);
        _paq.push(['setSiteId', '<xsl:value-of select="$piwikID" />']);
        _paq.push(['setDownloadExtensions', '7z|aac|arc|arj|asf|asx|avi|bin|bz|bz2|csv|deb|dmg|doc|exe|epub|flv|gif|gz|gzip|hqx|jar|jpg|jpeg|js|mp2|mp3|mp4|mpg|mpeg|mov|movie|msi|msp|odb|odf|odg|odp|ods|odt|ogg|ogv|pdf|phps|png|ppt|qt|qtm|ra|ram|rar|rpm|sea|sit|tar|tbz|tbz2|tgz|torrent|txt|wav|wma|wmv|wpd|z|zip']);
        var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
        g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
        })();
      </script>
      <noscript><p><img src="https://matomo.gbv.de/piwik.php?idsite={$piwikID}" style="border:0;" alt="" /></p></noscript>
    </xsl:if>
    <!-- End Piwik Code -->
  </xsl:template>

</xsl:stylesheet>
