<?xml version="1.0" encoding="UTF-8"?>
<!-- Used to override mir-access-rights div to change its div order -->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:i18n="xalan://org.mycore.services.i18n.MCRTranslation"
  xmlns:mods="http://www.loc.gov/mods/v3"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:cmd="http://www.cdlib.org/inside/diglib/copyrightMD"
  exclude-result-prefixes="i18n mods xlink cmd"
>
  <xsl:import href="xslImport:modsmeta:metadata/digibib-access-rights.xsl" />
 
  <xsl:template match="/">
    <xsl:if test="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[contains('copyrightMD|use and reproduction', @type)]">
      <div id="digibib-access-rights">
        <xsl:if test="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']">
          <p>
            <strong>
              <xsl:value-of select="i18n:translate('mir.useAndReproduction')" />
            </strong>
            <br />
            <xsl:variable name="trimmed"
              select="substring-after(normalize-space(mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']/@xlink:href),'#')" />
            <xsl:choose>
              <xsl:when test="contains($trimmed, 'cc_')">
                <xsl:apply-templates select="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']"
                  mode="cc-logo" />
              </xsl:when>
              <xsl:when test="contains($trimmed, 'rights_reserved')">
                <xsl:apply-templates select="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']"
                  mode="rights_reserved" />
              </xsl:when>
              <xsl:when test="contains($trimmed, 'oa_nlz')">
                <xsl:apply-templates select="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']"
                  mode="oa_nlz" />
              </xsl:when>
              <xsl:when test="contains($trimmed, 'oa')">
                <xsl:apply-templates select="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']"
                  mode="oa-logo" />
              </xsl:when>
              <xsl:when test="contains($trimmed, 'ogl')">
                <xsl:apply-templates select="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']"
                  mode="ogl-logo" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="document(concat('classification:metadata:0:children:mir_licenses:', $trimmed))/mycoreclass/categories/category[@ID=$trimmed]/label[@xml:lang=$CurrentLang]/@text" />
              </xsl:otherwise>
            </xsl:choose>
          </p>
        </xsl:if>
        <xsl:if test="mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='copyrightMD']">
          <p>
            <strong>
              <xsl:value-of select="i18n:translate('mir.rightsHolder')" />
            </strong>
            <xsl:text> </xsl:text>
            <xsl:value-of select="//mods:accessCondition[@type='copyrightMD']/cmd:copyright/cmd:rights.holder/cmd:name" />
          </p>
        </xsl:if>
      </div>
    </xsl:if>

    <xsl:apply-imports />

  </xsl:template>
</xsl:stylesheet>
