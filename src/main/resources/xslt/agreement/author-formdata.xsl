<?xml version="1.0"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="author">
    <xsl:call-template name="name"/>
    <xsl:call-template name="mail"/>
    <xsl:call-template name="institution"/>
    <xsl:call-template name="phone"/>
  </xsl:template>

  <xsl:template name="name">
    <xsl:if test="name">
      <field name="name" type="text">
        <xsl:value-of select="name/text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="mail">
    <xsl:if test="email">
      <field name="mail" type="text">
        <xsl:value-of select="email/text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="institution">
    <xsl:if test="institute">
      <field name="institution" type="text">
        <xsl:value-of select="institute/text()"/>
      </field>
    </xsl:if>
  </xsl:template>

  <xsl:template name="phone">
    <xsl:if test="phone">
      <field name="phone" type="text">
        <xsl:value-of select="phone/text()"/>
      </field>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
