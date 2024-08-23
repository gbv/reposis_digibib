<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:mods="http://www.loc.gov/mods/v3">
  
  <xsl:param name="WebApplicationBaseURL" />
  <xsl:param name="requestId" />
  <xsl:param name="objectId" />
  <xsl:param name="rmessage" />
  <xsl:param name="rname" />
  <xsl:param name="rorcid" />

  <xsl:variable name="newline" select="'&#xA;'" />
  <xsl:variable name="indent" select="'  '" />

  <xsl:variable select="document(concat('mcrobject:', $objectId))/mycoreobject" name="mycoreobject" />

  <xsl:template match="/email">
    <email>
      <xsl:copy-of select="node()" />
      <xsl:call-template name="subject" />
      <xsl:call-template name="body" />
    </email>
  </xsl:template>

  <xsl:template name="subject">
    <subject>
      <xsl:value-of select="concat('Confirmation of contact request [', $requestId, ']')" />
    </subject>
  </xsl:template>

  <xsl:template name="body">
    <body>
      <xsl:value-of select="concat('Hello ', $rname, ',', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('we have received the following contact request for the publication ', $mycoreobject//mods:mods/mods:titleInfo/mods:title, ' [0] on LeoPARD:', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat($indent, '=====', $newline)" />
      <xsl:value-of select="concat($indent, 'Name:  ', $rname, $newline)" />
      <xsl:if test="string-length($rorcid) &gt; 0">
        <xsl:value-of select="concat($indent, 'ORCiD: ', $rorcid, $newline)" />
      </xsl:if>
      <xsl:value-of select="$newline" />
      <xsl:call-template name="split">
        <xsl:with-param name="text" select="$rmessage"/>
      </xsl:call-template>
      <xsl:value-of select="concat($indent, '=====', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('The request will be forwarded to the parties involved in the publication after successful verification.', $newline)" />
      <xsl:value-of select="concat('If you have any questions, please do not hesitate to contact us at forschungsdaten@tu-braunschweig.de.', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('Best regards', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('Research Data Team of TU Braunschweig', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('[0]: ', $WebApplicationBaseURL, 'receive/', $objectId, $newline)" />
    </body>
  </xsl:template>

  <xsl:template name="split">
    <xsl:param name="text" select="."/>
    <xsl:if test="(string-length($text) > 0) or ($text=$newline)">
      <xsl:variable name="output-text">
        <xsl:value-of select="normalize-space(substring-before(concat($text, $newline), $newline))"/>
      </xsl:variable>
      <xsl:value-of select="concat($indent, $output-text, $newline)" />
      <xsl:call-template name="split">
        <xsl:with-param name="text" select="substring-after($text, $newline)"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
