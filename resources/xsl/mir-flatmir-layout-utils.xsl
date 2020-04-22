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
      <div id="options_nav_box" class="mir-prop-nav">

        <!-- do not show on startpage -->
        <xsl:if test="not(//div/@class='jumbotwo')">
          <div class="searchfield_box">
            <form action="{$WebApplicationBaseURL}servlets/solr/find" class="navbar-form navbar-left pull-right" role="search">
              <div class="form-group">
                <input name="condQuery" placeholder="{i18n:translate('mir.navsearch.placeholder')}" class="form-control search-query" id="searchInput" type="text" />
                <xsl:choose>
                  <xsl:when test="mcrxsl:isCurrentUserInRole('admin') or mcrxsl:isCurrentUserInRole('editor')">
                    <input name="owner" type="hidden" value="createdby:*" />
                  </xsl:when>
                  <xsl:when test="not(mcrxsl:isCurrentUserGuestUser())">
                    <input name="owner" type="hidden" value="createdby:{$CurrentUser}" />
                  </xsl:when>
                </xsl:choose>
              </div>
              <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i></button>
            </form>
          </div>
        </xsl:if>

        <nav>
          <ul class="navbar-nav ml-auto flex-row">
            <xsl:call-template name="mir.loginMenu" />
            <xsl:call-template name="mir.languageMenu" />
          </ul>
        </nav>
        <br />
        <a href="https://ub.tu-braunschweig.de/" id="bs-bibliothek"><xsl:value-of select="i18n:translate('digibib.bsLibrary')" /></a>
      </div>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="navbar navbar-expand-lg bg-primary mir-main-nav">
      <div class="container">

        <div class="navbar-header">
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target=".mir-main-nav-entries">
            <span class="navbar-toggler-icon"></span>
          </button>	
	  <div id="project_logo_box">
            <a class="navbar-brand hidden-xs" href="http://www.tu-braunschweig.de" title="{i18n:translate('digibib.goToMainSite')}"><img src="{$WebApplicationBaseURL}images/siegel_rot.png" alt="{i18n:translate('digibib.logoTUBS')}" /></a>
            <!-- a class="navbar-brand visible-xs-block" href="http://www.tu-braunschweig.de" title="zur Startseite der TU Braunschweig"><img src="{$WebApplicationBaseURL}images/siegel_rot_small.png" alt="Technische Universität Braunschweig" /></a -->
          </div>
        </div>

        <nav class="collapse navbar-collapse mir-main-nav-entries">
          <ul class="navbar-nav mr-auto">
            <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='brand']/*" />
            <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='search']" />
            <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='collections']" />
            <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='publish']" />
            <xsl:call-template name="mir.basketMenu" />
          </ul>
        </nav>

        <div id="digibib_feedback">
          <a href="mailto:digibib@tu-braunschweig.de">Feedback</a>
        </div>

      </div><!-- /container -->
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
            <xsl:apply-templates select="$loaded_navigation_xml/menu[@id='below']/*" />
          </ul>
        </div>
        <div class="col-6 col-sm-3">
          <xsl:variable name="mcr_version" select="concat('MyCoRe ',mcrver:getCompleteVersion())" />
          <div id="powered_by">
            <a id="dini_logo" href="https://www.dini.de/dienste-projekte/dini-zertifikat/" title="{i18n:translate('digibib.diniCertificate2016')}">
              <img alt="Logo DINI-Zertifikat 2016" src="{$WebApplicationBaseURL}images/dini_zertifikat_2016.svg" height="50" />
            </a>
            <a id="mycore_logo" href="http://www.mycore.de">
              <img src="{$WebApplicationBaseURL}mir-layout/images/mycore_logo_small_invert.png" title="{$mcr_version}" alt="powered by MyCoRe" />
            </a>
          </div>
        </div>
      </div>
    </div>
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
