<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:param name="WebApplicationBaseURL" />
  <xsl:param name="MCR.mir-module.MailSender" />
  <xsl:param name="MIR.UploadForm.path" />

  <xsl:template match="/">
    <email>
      <from>
        <xsl:value-of select="email/from/@name" />
        &lt;<xsl:value-of select="email/from/@mail" />&gt;
      </from>
      <to>
        <xsl:value-of select="$MCR.mir-module.MailSender" />
      </to>
      <subject>
        [Publikationsserver] - Online-Einreichung
      </subject>
      <body>
        <xsl:value-of select="email/from/@name" /> sendet folgende Publikation zur Einreichung:


    Angaben zur Person:
    -------------------
        Name:      <xsl:value-of select="email/from/@name" />
        E-Mail:    <xsl:value-of select="email/from/@mail" />
        Institut:  <xsl:value-of select="email/from/@institute" />
        Fakultät:  <xsl:value-of select="email/from/@faculty" />


    Angaben zur Publikation:
    ------------------------
        Titel (deutsch):        <xsl:value-of select="email/body/publication/title[@lang='de']" />
        Titel (englisch):       <xsl:value-of select="email/body/publication/title[@lang='en']" />
        Schlagworte (deutsch):  <xsl:value-of select="email/body/publication/keywords[@lang='de']" />
        Schlagworte (englisch): <xsl:value-of select="email/body/publication/keywords[@lang='en']" />
        Lizenz:                 <xsl:value-of select="email/body/publication/license" />

        Anhang: <xsl:value-of select="email/file/@name" />

      </body>
      <part>
        <xsl:value-of select="concat('file://', $MIR.UploadForm.path, '/', email/file/@name )" />
      </part>
    </email>
  </xsl:template>

</xsl:stylesheet>
