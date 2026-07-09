<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:fn="http://www.w3.org/2005/xpath-functions"
  xmlns:mods="http://www.loc.gov/mods/v3"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  exclude-result-prefixes="fn mods">

  <xsl:import href="xslImport:modsmeta:metadata/metadata-extension.xsl" />
  <xsl:import href="resource:xsl/metadata/rep-metadata-utils.xsl" />

  <xsl:template match="/">
    <xsl:variable name="mods" select="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods" />
      <xsl:if test="$mods/mods:extension[@type='advanced-part']/fn:map">
        <div id="metadata-extension">
          <div class="mir_metadata" style="margin-top:-30px;">
            <hr class="my-3" />
            <dl>
              <xsl:call-template name="advanced">
                <xsl:with-param name="advanced" select="$mods/mods:extension[@type='advanced-part']/fn:map" />
              </xsl:call-template>
            </dl>
          </div>
        </div>
      </xsl:if>
    <xsl:apply-imports />
  </xsl:template>

  <xsl:template name="advanced">
    <xsl:param name="advanced" />
    <xsl:if test="$advanced/fn:array[@key='type']/fn:map">
      <xsl:call-template name="print-field">
        <xsl:with-param name="i18n" select="'digibib.researchData.advanced.type'" />
        <xsl:with-param name="pre-value">
          <xsl:call-template name="get-classification-label">
            <xsl:with-param name="classification" select="$advanced/fn:array[@key='type']/fn:map/fn:string[@key='type']" />
          </xsl:call-template>
        </xsl:with-param>
        <xsl:with-param name="value" select="$advanced/fn:array[@key='type']/fn:map/fn:string[@key='description']" />
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$advanced/fn:array[@key='researchObject']/fn:map">
      <xsl:call-template name="print-field">
        <xsl:with-param name="i18n" select="'digibib.researchData.advanced.researchObject'" />
        <xsl:with-param name="pre-value">
          <xsl:call-template name="get-classification-label">
            <xsl:with-param name="classification" select="$advanced/fn:array[@key='researchObject']/fn:map/fn:string[@key='type']" />
          </xsl:call-template>
        </xsl:with-param>
        <xsl:with-param name="value" select="$advanced/fn:array[@key='researchObject']/fn:map/fn:string[@key='description']" />
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$advanced/fn:array[@key='dataOrigin']/fn:map">
      <xsl:call-template name="print-field">
        <xsl:with-param name="i18n" select="'digibib.researchData.advanced.dataOrigin'" />
        <xsl:with-param name="pre-value">
          <xsl:call-template name="get-classification-label">
            <xsl:with-param name="classification" select="$advanced/fn:array[@key='dataOrigin']/fn:map/fn:string[@key='type']" />
          </xsl:call-template>
        </xsl:with-param>
        <xsl:with-param name="value" select="$advanced/fn:array[@key='dataOrigin']/fn:map/fn:string[@key='description']" />
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$advanced/fn:array[@key='software_types']/fn:map">
      <xsl:variable name="name" select="$advanced/fn:array[@key='software_types']/fn:map/fn:string[@key='name']" />
      <xsl:variable name="version" select="$advanced/fn:array[@key='software_types']/fn:map/fn:string[@key='version']" />
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
            <xsl:with-param name="classification" select="$advanced/fn:array[@key='software_types']/fn:map/fn:string[@key='type']" />
          </xsl:call-template>
        </xsl:with-param>
        <xsl:with-param name="value" select="$fullname" />
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$advanced/fn:string[@key='methods']">
      <xsl:call-template name="print-field">
        <xsl:with-param name="i18n" select="'digibib.researchData.advanced.methods'" />
        <xsl:with-param name="value" select="$advanced/fn:string[@key='methods']" />
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$advanced/fn:string[@key='instruments']">
      <xsl:call-template name="print-field">
        <xsl:with-param name="i18n" select="'digibib.researchData.advanced.instruments'" />
        <xsl:with-param name="value" select="$advanced/fn:string[@key='instruments']" />
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$advanced/fn:string[@key='processing']">
      <xsl:call-template name="print-field">
        <xsl:with-param name="i18n" select="'digibib.researchData.advanced.processing'" />
        <xsl:with-param name="value" select="$advanced/fn:string[@key='processing']" />
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
