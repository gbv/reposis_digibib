<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fn="http://www.w3.org/2005/xpath-functions"
  xmlns:mods="http://www.loc.gov/mods/v3"
  exclude-result-prefixes="fn mods">

  <xsl:import href="xslImport:modsmeta:metadata/digibib-metadata-extension.xsl"/>
  <xsl:import href="resource:xsl/metadata/reposis-metadata-utils.xsl"/>

  <xsl:template match="/">
    <xsl:variable name="advanced"
      select="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:extension[@type='advanced-part']/fn:map"/>
    <xsl:if test="$advanced">
      <div id="digibib-metadata-extension">
        <div class="mir_metadata" style="margin-top:-30px;">
          <hr class="my-3"/>
          <dl>
            <xsl:apply-templates select="$advanced/fn:array[@key='type']" mode="advanced"/>
            <xsl:apply-templates select="$advanced/fn:array[@key='researchObject']" mode="advanced"/>
            <xsl:apply-templates select="$advanced/fn:array[@key='dataOrigin']" mode="advanced"/>
            <xsl:apply-templates select="$advanced/fn:array[@key='software_types']" mode="advanced"/>
            <xsl:apply-templates select="$advanced/fn:string[@key='methods']" mode="advanced"/>
            <xsl:apply-templates select="$advanced/fn:string[@key='instruments']" mode="advanced"/>
            <xsl:apply-templates select="$advanced/fn:string[@key='processing']" mode="advanced"/>
          </dl>
        </div>
      </div>
      </xsl:if>
    <xsl:apply-imports/>
  </xsl:template>

  <xsl:template match="fn:array[@key='software_types']" mode="advanced">
    <xsl:variable name="name" select="fn:map/fn:string[@key='name']" />
    <xsl:variable name="version" select="fn:map/fn:string[@key='version']" />
    <xsl:variable name="fullname">
      <xsl:choose>
        <xsl:when test="$version">
          <xsl:value-of select="concat($name, ' (', $version, ')')" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$name" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:call-template name="print-field">
      <xsl:with-param name="i18n" select="'digibib.researchData.advanced.software'" />
      <xsl:with-param name="pre-value">
        <xsl:call-template name="get-classification-label">
          <xsl:with-param name="classification" select="fn:map/fn:string[@key='type']" />
        </xsl:call-template>
      </xsl:with-param>
      <xsl:with-param name="value" select="$fullname" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="fn:array" mode="advanced">
    <xsl:call-template name="print-field">
      <xsl:with-param name="i18n" select="concat('digibib.researchData.advanced.', @key)" />
      <xsl:with-param name="pre-value">
        <xsl:call-template name="get-classification-label">
          <xsl:with-param name="classification" select="fn:map/fn:string[@key='type']" />
        </xsl:call-template>
      </xsl:with-param>
      <xsl:with-param name="value" select="fn:map/fn:string[@key='description']" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="fn:string" mode="advanced">
    <xsl:call-template name="print-field">
      <xsl:with-param name="i18n" select="concat('digibib.researchData.advanced.', @key)" />
      <xsl:with-param name="value" select="text()" />
    </xsl:call-template>
  </xsl:template>

</xsl:stylesheet>
