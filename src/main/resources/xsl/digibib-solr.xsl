<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:mods="http://www.loc.gov/mods/v3"
        xmlns:mcrxsl="xalan://org.mycore.common.xml.MCRXMLFunctions"
        xmlns:xlink="http://www.w3.org/1999/xlink"
        exclude-result-prefixes="mods mcrxsl xlink"
>
    <xsl:import href="xslImport:solr-document:digibib-solr.xsl" />

    <xsl:template match="mycoreobject[contains(@ID,'_mods_')]">
        <xsl:apply-templates select="metadata/def.modsContainer/modsContainer/mods:mods" mode="digibib" />

        <xsl:apply-imports />
    </xsl:template>

    <xsl:template match="mods:mods" mode="digibib">
        <xsl:apply-templates select="mods:subject/mods:topic" mode="digibib" />
        <xsl:apply-templates select="mods:classification[@authorityURI='http://www.digibib.tu-bs.de/discipline']" mode="digibib" />
        <xsl:apply-templates select="mods:name[@type='corporate' and @authorityURI='http://www.mycore.org/classifications/mir_institutes']" mode="digibib" />
    </xsl:template>

    <xsl:template match="mods:subject/mods:topic" mode="digibib">
        <field name="digibib.mods.subject.string">
            <xsl:value-of select="text()" />
        </field>
    </xsl:template>

    <xsl:template match="mods:classification[@authorityURI='http://www.digibib.tu-bs.de/discipline']" mode="digibib">
        <field name="digibib.mods.discipline">
            <xsl:value-of select="substring-after(@valueURI, '#')" />
        </field>
    </xsl:template>

    <xsl:template match="mods:name[@type='corporate' and @authorityURI='http://www.mycore.org/classifications/mir_institutes']" mode="digibib">
        <field name="digibib.mods.faculty">
            <xsl:value-of select="substring-after(@valueURI, '#')" />
        </field>
    </xsl:template>

</xsl:stylesheet>