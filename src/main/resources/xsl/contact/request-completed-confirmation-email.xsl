<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="objectId" />
  <xsl:param name="rname" />
  <!-- TODO language english or german? -->

  <xsl:variable name="newline" select="'&#xA;'" />
  <xsl:variable name="indent" select="'  '" />

  <xsl:template match="/email">
    <email>
      <xsl:copy-of select="node()" />
      <xsl:call-template name="subject" />
      <xsl:call-template name="body" />
    </email>
  </xsl:template>

  <xsl:template name="subject">
    <subject>
      <xsl:value-of select="concat('Anfrage für: ', $objectId, ' abgeschlossen')" />
    </subject>
  </xsl:template>

  <xsl:template name="body">
    <body>
      <xsl:value-of select="concat('Hallo ', $rname, ',', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('Ihre Anfrage wurde soeben als abgeschlossen gekennzeichnet.', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('Beste Grüße', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('FD-Team der TU Braunschweig', $newline)" />
    </body>
  </xsl:template>

</xsl:stylesheet>
