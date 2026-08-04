<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:mcrxsl="xalan://org.mycore.common.xml.MCRXMLFunctions"
  xmlns:mods="http://www.loc.gov/mods/v3"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  exclude-result-prefixes="mcrxsl mods xlink">

  <xsl:import href="xslImport:solr-document:digibib-solr.xsl" />

  <xsl:param name="discipline-uri" select="'http://www.digibib.tu-bs.de/discipline'" />
  <xsl:param name="validity-state-uri" select="'http://www.digibib.tu-bs.de/validity_state'" />
  <xsl:param name="institutes-uri" select="'http://www.mycore.org/classifications/mir_institutes'" />

  <xsl:template match="mycoreobject[contains(@ID,'_mods_')]">
    <xsl:apply-templates select="metadata/def.modsContainer/modsContainer/mods:mods" mode="digibib" />
    <xsl:apply-imports />
  </xsl:template>

  <xsl:template match="mods:mods" mode="digibib">
    <xsl:apply-templates mode="digibib" select="mods:subject/mods:topic" />
    <xsl:apply-templates mode="digibib" select="mods:classification[@authorityURI=$discipline-uri]" />
    <xsl:apply-templates mode="digibib" select="mods:classification[@authorityURI=$validity-state-uri]" />
    <xsl:apply-templates mode="digibib" select="mods:name[@type='corporate' and @authorityURI=$institutes-uri]" />
  </xsl:template>

  <xsl:template mode="digibib" match="mods:subject/mods:topic">
    <field name="digibib.mods.subject.string">
      <xsl:value-of select="text()" />
    </field>
  </xsl:template>

  <xsl:template mode="digibib" match="mods:classification[@authorityURI=$discipline-uri]">
    <field name="digibib.mods.discipline">
      <xsl:value-of select="substring-after(@valueURI, '#')" />
    </field>
  </xsl:template>

  <xsl:template mode="digibib" match="mods:classification[@authorityURI=$validity-state-uri]">
    <field name="digibib.mods.validity_state">
      <xsl:value-of select="substring-after(@valueURI, '#')" />
    </field>
  </xsl:template>

  <xsl:template mode="digibib" match="mods:name[@type='corporate' and @authorityURI=$institutes-uri]">
    <field name="digibib.mods.faculty">
      <xsl:value-of select="substring-after(@valueURI, '#')" />
    </field>
  </xsl:template>

</xsl:stylesheet>
