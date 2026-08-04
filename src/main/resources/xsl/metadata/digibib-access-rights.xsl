<?xml version="1.0" encoding="UTF-8"?>
<!-- Used to override mir-access-rights div to change its div order -->
<xsl:stylesheet version="1.0"
  xmlns:cmd="http://www.cdlib.org/inside/diglib/copyrightMD"
  xmlns:i18n="xalan://org.mycore.services.i18n.MCRTranslation"
  xmlns:mods="http://www.loc.gov/mods/v3"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  exclude-result-prefixes="cmd i18n mods xlink"
>
  <xsl:import href="xslImport:modsmeta:metadata/digibib-access-rights.xsl" />
 
  <xsl:template match="/">
    <xsl:variable name="mods" select="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods" />
    <xsl:variable name="use-and-reproduction" select="$mods/mods:accessCondition[@type='use and reproduction']" />
    <xsl:variable name="copyright-md" select="$mods/mods:accessCondition[@type='copyrightMD']" />

    <xsl:if test="$use-and-reproduction or $copyright-md">
      <div id="digibib-access-rights">
        <xsl:if test="$use-and-reproduction">
          <p>
            <strong>
              <xsl:value-of select="i18n:translate('mir.useAndReproduction')" />
            </strong>
            <br />
            <xsl:variable name="trimmed" select="
              substring-after(normalize-space($use-and-reproduction/@xlink:href),'#')
            " />
            <xsl:choose>
              <xsl:when test="contains($trimmed, 'cc_')">
                <xsl:apply-templates select="$use-and-reproduction" mode="cc-logo" />
              </xsl:when>
              <xsl:when test="contains($trimmed, 'rights_reserved')">
                <xsl:apply-templates select="$use-and-reproduction" mode="rights_reserved" />
              </xsl:when>
              <xsl:when test="contains($trimmed, 'oa_nlz')">
                <xsl:apply-templates select="$use-and-reproduction" mode="oa_nlz" />
              </xsl:when>
              <xsl:when test="contains($trimmed, 'oa')">
                <xsl:apply-templates select="$use-and-reproduction" mode="oa-logo" />
              </xsl:when>
              <xsl:when test="contains($trimmed, 'ogl')">
                <xsl:apply-templates select="$use-and-reproduction" mode="ogl-logo" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="
                  document(concat('classification:metadata:0:children:mir_licenses:', $trimmed))
                    /mycoreclass/categories/category[@ID=$trimmed]
                    /label[@xml:lang=$CurrentLang]
                    /@text
                " />
              </xsl:otherwise>
            </xsl:choose>
          </p>
        </xsl:if>
        <xsl:if test="$copyright-md">
          <p>
            <strong>
              <xsl:value-of select="i18n:translate('mir.rightsHolder')" />
            </strong>
            <xsl:text> </xsl:text>
            <xsl:value-of select="$copyright-md/cmd:copyright/cmd:rights.holder/cmd:name" />
          </p>
        </xsl:if>
      </div>
    </xsl:if>
    <xsl:apply-imports />
  </xsl:template>

</xsl:stylesheet>
