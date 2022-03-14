<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:i18n="xalan://org.mycore.services.i18n.MCRTranslation"
                xmlns:mcrxsl="xalan://org.mycore.common.xml.MCRXMLFunctions"
                xmlns:mods="http://www.loc.gov/mods/v3"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:encoder="xalan://java.net.URLEncoder"
                xmlns:exslt="http://exslt.org/common"
                xmlns:xsL="http://www.w3.org/1999/XSL/Transform" version="1.0"
                exclude-result-prefixes="i18n mcrxsl encoder mods xlink exslt">

  <!-- embed a solr search into the about us > institution pages -->
  <xsl:template match="div[contains(@class, 'solrsearch')]">
    <!-- the search query -->
    <xsl:variable name="parameters" select="@data-parameters" />
    <xsl:variable name="listtitle" select="@data-listtitle" />

    <xsl:variable name="searchResult">
      <xsl:choose>
        <xsl:when test="mcrxsl:isCurrentUserInRole('admin') or mcrxsl:isCurrentUserInRole('editor')">
          <xsL:copy-of select="document(concat('solr:', $parameters, '&amp;sort=id asc'))"/>
        </xsl:when>
        <xsl:otherwise>
          <xsL:copy-of select="document(concat('solr:', $parameters, '%20AND%20state:published&amp;sort=id asc&amp;'))"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <ul>
      <xsl:for-each select="exslt:node-set($searchResult)//doc">
        <li>
          <xsl:variable name="id" select="str[@name='id']/text()" />
          <a href="{$WebApplicationBaseURL}receive/{$id}">
            <xsl:value-of select="./str[@name='mods.title.main']" />
            <xsl:if test="./str[@name='mods.title.subtitle']">
              <xsl:value-of select="concat(' : ', ./str[@name='mods.title.subtitle'])" />
            </xsl:if>
          </a>
        </li>
      </xsl:for-each>
    </ul>
  </xsl:template>


</xsl:stylesheet>