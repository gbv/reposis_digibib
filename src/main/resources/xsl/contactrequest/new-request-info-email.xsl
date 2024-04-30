<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="objectId" />
  <xsl:param name="WebApplicationBaseURL" />

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
      <xsl:value-of select="concat('New contact request for: ' , $objectId)" />
    </subject>
  </xsl:template>

  <xsl:template name="body">
    <body>
      <xsl:value-of select="concat('Hello,', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('there is a new contact request for: ', $objectId, '.', $newline)" />
    </body>
  </xsl:template>

</xsl:stylesheet>
