<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="xslImport:modsmeta:metadata/contact-request-modal.xsl" />
  <xsl:param name="Digibib.Contact.RequestForm.EnabledGenres" />

  <xsl:template match="/">
    <xsl:if test="contains($Digibib.Contact.RequestForm.EnabledGenres, $mods-type)">
      <div id="contactModalDiv">
        <xsl:call-template name="print-create-contact-request-modal" />
        <xsl:call-template name="print-open-create-contact-request-form-btn" />
      </div>
    </xsl:if>
    <xsl:apply-imports />
  </xsl:template>

  <xsl:template name="print-create-contact-request-modal">
    <script src="{$WebApplicationBaseURL}vue/vue.global.prod.js" />
    <script src="{$WebApplicationBaseURL}vue/vue-i18n.global.prod.js" />
    <script src="{$WebApplicationBaseURL}vue/contactRequestForm/contactRequestForm.umd.js" />
    <link rel="stylesheet" href="{$WebApplicationBaseURL}vue/contactRequestForm/contactRequestForm.css" />

    <div id="contact-request-form-mount">
      <cf base-url="{$WebApplicationBaseURL}" object-id="{$id}"></cf>
    </div>
    <script>
    function initialize(i18nData) {
        const i18n = VueI18n.createI18n({
        locale: '_',
        messages: {
          _: i18nData,
        },
      });
      const app = Vue.createApp({
        components: {
          cf: contactRequestForm
        },
      });
      app.use(i18n);
      app.mount('#contact-request-form-mount');
    }
    fetch(webApplicationBaseURL + 'rsc/locale/translate/digibib.contact.frontend.*')
      .then(response => response.json())
      .then((data) => initialize(data))
      .catch((error) => initialize({}));
    </script>
  </xsl:template>

  <xsl:template name="print-open-create-contact-request-form-btn">
    <li>
      <div id="btnContact" />
    </li>
    <script>
    $(document).ready(() => {
      actionMenu = $("div#mir-edit-div").find("ul.dropdown-menu");
      actionMenu.append($("#btnContact").parent());
    });
    </script>
  </xsl:template>
</xsl:stylesheet>
