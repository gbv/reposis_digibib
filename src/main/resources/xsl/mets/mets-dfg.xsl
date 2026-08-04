<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:exslt="http://exslt.org/common"
  xmlns:mets="http://www.loc.gov/METS/"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  exclude-result-prefixes="exslt">

  <xsl:param name="ServletsBaseURL" />
  <xsl:param name="ThumbnailBaseURL" select="concat($ServletsBaseURL,'MCRDFGThumbnail/')" />
  <xsl:param name="ImageBaseURL" select="concat($ServletsBaseURL,'MCRDFGServlet/')"/>
  <!-- START digibib adaptions -->
  <!--
  <xsl:param name="copyFileGrp" select="emptyNodeList" />
  -->
  <xsl:param name="prepareGroup">
    <xsl:if test="count(.//mets:fileGrp[@USE='ALTO'])=1">
      <mets:fileGrp id="ALTO" USE="FULLTEXT">
        <xsl:copy-of select=".//mets:fileGrp[@USE='ALTO']/*"/>
      </mets:fileGrp>
    </xsl:if>
  </xsl:param>
  <xsl:param name="copyFileGrp" select="exslt:node-set($prepareGroup)"/>
  <!-- END digibib adaptions -->
  <xsl:include href="resource:xsl/mets/mets-dfgProfile.xsl" />
</xsl:stylesheet>
