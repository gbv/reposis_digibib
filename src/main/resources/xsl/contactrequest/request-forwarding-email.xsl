<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:mods="http://www.loc.gov/mods/v3">
 
  <xsl:param name="WebApplicationBaseURL" />
 
  <xsl:param name="requestId" />
  <xsl:param name="objectId" />
  <xsl:param name="attemptId" />
  <xsl:param name="comment" />
  <xsl:param name="tname" />
  <xsl:param name="remail" />
  <xsl:param name="rname" />
  <xsl:param name="rorcid" select="''" />
  <xsl:param name="rmessage" />

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
      <xsl:value-of select="concat('New contact request [', $requestId, ']')" />
    </subject>
  </xsl:template>

  <xsl:template name="body">
    <body>
      <xsl:value-of select="concat('Hello ', $tname, ',', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('there is a contact request for your publication ', $mycoreobject//mods:mods/mods:titleInfo/mods:title, ' [0] on LeoPARD:', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat($indent, '=====', $newline)" />
      <xsl:value-of select="concat($indent, 'Name:  ', $rname, $newline)" />
      <xsl:value-of select="concat($indent, 'Email: ', $remail, $newline)" />
      <xsl:if test="string-length($rorcid) &gt; 0">
        <xsl:value-of select="concat($indent, 'ORCiD: ', $rorcid, $newline)" />
      </xsl:if>
      <xsl:value-of select="$newline" />
      <xsl:call-template name="split">
        <xsl:with-param name="text" select="$rmessage"/>
      </xsl:call-template>
      <xsl:value-of select="concat($indent, '=====', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:if test="string-length($comment) &gt; 0">
        <xsl:value-of select="$newline" />
        <xsl:value-of select="concat('Comment from the administration:', $newline)" />
        <xsl:call-template name="split">
          <xsl:with-param name="text" select="$comment"/>
        </xsl:call-template>
        <xsl:value-of select="$newline" />
      </xsl:if>
      <xsl:value-of select="concat('You can check the current status of the request via [1].', $newline)" />
      <xsl:value-of select="concat('To complete the contacting process, please confirm receipt of this email via [2].', $newline)" />
      <xsl:value-of select="concat('If you have any questions, please do not hesitate to contact us at forschungsdaten@tu-braunschweig.de.', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('Best regards', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('Research Data Team of TU Braunschweig', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('[0]: ', $WebApplicationBaseURL, 'receive/', $objectId, $newline)" />
      <xsl:value-of select="concat('[1]: ', $WebApplicationBaseURL, 'contact-request/status.xml?rid=', $requestId, $newline)" />
      <xsl:value-of select="concat('[2]: ', $WebApplicationBaseURL, 'contact-request/confirm.xml?aid=', $attemptId, $newline)" />
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
