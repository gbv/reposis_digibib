<?xml version="1.0"?>
<xsl:stylesheet version="3.0"
  xmlns:mods="http://www.loc.gov/mods/v3"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  exclude-result-prefixes="mods">

  <xsl:import href="resource:xslt/agreement/mods-formdata.xsl"/>
  <xsl:import href="resource:xslt/agreement/author-formdata.xsl"/>
  <xsl:variable name="supportedLicenses" select="[ 'cc_by_4.0', 'cc_by-sa_4.0', 'cc_by-nc_4.0', 'cc_by-nc-sa_4.0', 'cc_by-nd_4.0', 'cc_by-nc-nd_4.0', 'rights_reserved', 'other' ]"/>

  <xsl:output encoding="UTF-8"/>

  <xsl:template match="mycoreobject">
    <formData>
      <xsl:variable name="createdBy" select="service/servflags/servflag[@type='createdby']/text()"/>
      <xsl:apply-templates select="document(concat('agreementauthor:', $createdBy))/author"/>
      <xsl:apply-templates select="metadata/def.modsContainer/modsContainer/mods:mods"/>
    </formData>
  </xsl:template>

  <xsl:template match="mods:mods">
    <xsl:call-template name="title"/>
    <xsl:call-template name="license"/>
    <xsl:call-template name="comments"/>
  </xsl:template>

</xsl:stylesheet>
