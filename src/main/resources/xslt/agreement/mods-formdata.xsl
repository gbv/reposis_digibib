<?xml version="1.0"?>
<xsl:stylesheet version="3.0"
  xmlns:array="http://www.w3.org/2005/xpath-functions/array"
  xmlns:digibib="http://www.mycore.de/digibib"
  xmlns:mods="http://www.loc.gov/mods/v3"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  exclude-result-prefixes="array mods xlink xs">

  <xsl:variable name="supportedLicenses" select="[ ]"/>

  <xsl:function name="digibib:get-license-text" as="xs:string?">
    <xsl:param name="licenseValue" as="xs:string"/>
    <xsl:sequence  select="document(concat('classification:metadata:0:children:mir_licenses:', $licenseValue))//category/label[@xml:lang='en']/@text"/>
  </xsl:function>

  <xsl:template name="comments">
    <xsl:if test="mods:note[@type='admin']">
      <field name="comments" type="text">
        <xsl:value-of select="mods:note[@type='admin'][1]/text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="embargo">
    <xsl:if test="mods:accessCondition[@type='embargo' and @xlink:type='simple']">
      <field name="embargo" type="text">
        <xsl:value-of select="mods:accessCondition[@type='embargo' and @xlink:type='simple']/text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="license">
    <xsl:variable name="license" select="mods:accessCondition[@type='use and reproduction' and starts-with(@xlink:href, 'http://www.mycore.org/classifications/mir_licenses')]"/>
    <xsl:if test="$license">
      <xsl:variable name="licenseValue" select="$license/substring-after(@xlink:href, '#')"/>
      <xsl:choose>
        <xsl:when test="$licenseValue = array:flatten($supportedLicenses)">
          <field name="license" type="radio">
            <xsl:value-of select="$licenseValue"/>
          </field>
        </xsl:when>
        <xsl:otherwise>
          <field name="license" type="radio">
            <xsl:value-of select="'other'"/>
          </field>
          <field name="otherLicense" type="text">
            <xsl:value-of select="digibib:get-license-text($licenseValue)"/>
          </field>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="title">
    <xsl:if test="mods:titleInfo/mods:title">
      <field name="title" type="text">
        <xsl:value-of select="mods:titleInfo[1]/mods:title/text()"/>
      </field>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
