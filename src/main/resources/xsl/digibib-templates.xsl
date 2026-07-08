<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:mcri18n="xalan://org.mycore.services.i18n.MCRTranslation"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  exclude-result-prefixes="mcri18n">

  <xsl:template match="index-search-form">
    <form action="../servlets/solr/find" id="bs-searchMainPage" class="form-inline mt-3" role="search">
      <div class="input-group">
        <input
          name="condQuery"
          placeholder="{mcri18n:translate('digibib.index.search.placeholder.default')}"
          class="form-control search-query"
          id="bs-searchInput"
          type="text"
          aria-label=""
          aria-describedby="" />
        <div class="input-group-append">
          <button type="submit" class="btn btn-primary">
            <i class="fas fa-search" />
          </button>
        </div>
      </div>
    </form>
  </xsl:template>

</xsl:stylesheet>
