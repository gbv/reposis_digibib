<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:param name="WebApplicationBaseURL" />
  <xsl:param name="ticketId" />
  <xsl:param name="objectId" />
  <xsl:param name="rmessage" />
  <xsl:param name="rname" />
  <xsl:param name="rorcid" />
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
      <xsl:value-of select="concat('Bestätigung der Anfrage zu: ', $objectId)" />
    </subject>
  </xsl:template>

  <xsl:template name="body">
    <body>
      <xsl:value-of select="concat('Hallo ', $rname, ',', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('wir haben folgende Kontaktanfrage für ', $objectId, ' [0] erhalten:', $newline)" />
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
      <xsl:value-of select="concat('Die Anfrage wird nun geprüft und nach erfolgreicher Prüfung an die Beteiligten weitergeleitet.', $newline)" />
      <xsl:value-of select="concat('Den Status der Anfrage können Sie unter [1] prüfen.', $newline)" />
      <xsl:value-of select="concat('Sollten Sie in den nächsten 5 Werktagen keine Bestätigung über eine Weiterleitung erhalten haben, kontaktieren Sie bitte unseren Support: forschungsdaten@tu-braunschweig.de.', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('Beste Grüße', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('FD-Team der TU Braunschweig', $newline)" />
      <xsl:value-of select="$newline" />
      <xsl:value-of select="concat('[0]: ', $WebApplicationBaseURL, 'receive/', $objectId, $newline)" />
      <xsl:value-of select="concat('[1]: ', $WebApplicationBaseURL, 'contact-request/status.xml?rid=', $ticketId)" />
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
