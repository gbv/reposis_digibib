<?xml version="1.0"?>
<xsl:stylesheet version="3.0"
  xmlns:mods="http://www.loc.gov/mods/v3"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output encoding="UTF-8"/>

  <xsl:template match="mycoreobject">
    <formData>
      <!-- TODO resolve user from created by -->
      <xsl:apply-templates select="document('agreementauthor:administrator')/author"/>
      <xsl:apply-templates select="metadata/def.modsContainer/modsContainer/mods:mods"/>
    </formData>
  </xsl:template>

  <xsl:template match="author">
    <xsl:call-template name="name"/>
    <xsl:call-template name="mail"/>
    <xsl:call-template name="institution"/>
    <xsl:call-template name="phone"/>
  </xsl:template>

  <xsl:template match="mods:mods">
    <xsl:call-template name="title"/>
    <xsl:call-template name="license"/>
    <xsl:call-template name="comments"/>
    <!-- <xsl:call-template name="embargo"/> -->
  </xsl:template>

  <xsl:template name="name">
    <xsl:if test="name">
      <field name="name" type="text">
        <xsl:value-of select="text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="mail">
    <xsl:if test="email">
      <field name="mail" type="text">
        <xsl:value-of select="text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="institution">
    <xsl:if test="institute">
      <field name="institution" type="text">
        <xsl:value-of select="text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="phone">
    <xsl:if test="phone">
      <field name="phone" type="text">
        <xsl:value-of select="text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="title">
    <xsl:if test="mods:titleInfo/mods:title">
      <field name="title" type="text">
        <xsl:value-of select="mods:titleInfo/mods:title/text()"/>
      </field>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="license">
    <xsl:if test="mods:accessCondition[@type='use and reproduction' and starts-with(@xlink:href, 'http://www.mycore.org/classifications/mir_licenses')]">
      <field name="licenses" type="radio">
        <xsl:value-of select="mods:accessCondition[@type='use and reproduction' and starts-with(@xlink:href, 'http://www.mycore.org/classifications/mir_licenses')]/substring-after(@xlink:href, '#')"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="comments">
    <field name="comments" type="text">
      TODO
    </field>
  </xsl:template>


  <xsl:template name="embargo">
    <xsl:if test="mods:accessCondition[@type='embargo' and @xlink:type='simple']">
      <embargo>
        <xsl:value-of select="mods:accessCondition[@type='embargo' and @xlink:type='simple']/text()"/>
      </embargo>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
