<?xml version="1.0"?>
<xsl:stylesheet version="3.0"
  xmlns:mods="http://www.loc.gov/mods/v3"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output encoding="UTF-8"/>

  <xsl:template match="mycoreobject">
  	<content>
      <xsl:apply-templates select="metadata/def.modsContainer/modsContainer/mods:mods"/>
      <xsl:copy-of select="document('agreementauthor:administrator')"/>
  	</content>
  </xsl:template>

  <xsl:template match="mods:mods">
    <xsl:call-template name="title"/>
    <xsl:call-template name="doi"/>
    <xsl:call-template name="license"/>
    <xsl:call-template name="embargo"/>
  </xsl:template>

  <xsl:template name="title">
    <xsl:if test="mods:titleInfo/mods:title">
      <title>
        <xsl:value-of select="mods:titleInfo/mods:title/text()"/>
      </title>
    </xsl:if>
  </xsl:template>

  <xsl:template name="doi">
    <xsl:if test="mods:identifier[@type='doi']">
      <doi>
        <xsl:value-of select="mods:identifier[@type='doi']/text()"/>
      </doi>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="license">
    <xsl:if test="mods:accessCondition[@type='use and reproduction' and starts-with(@xlink:href, 'http://www.mycore.org/classifications/mir_licenses')]">
      <license>
        <xsl:value-of select="mods:accessCondition[@type='use and reproduction' and starts-with(@xlink:href, 'http://www.mycore.org/classifications/mir_licenses')]/substring-after(@xlink:href, '#')"/>
      </license>
    </xsl:if>
  </xsl:template>

  <xsl:template name="embargo">
    <xsl:if test="mods:accessCondition[@type='embargo' and @xlink:type='simple']">
      <embargo>
        <xsl:value-of select="mods:accessCondition[@type='embargo' and @xlink:type='simple']/text()"/>
      </embargo>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
