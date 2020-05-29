
$(document).ready(function() {

// spam protection for mails
  $('span.madress').each(function(i) {
      var text = $(this).text();
      var address = text.replace(" [at] ", "@");
      $(this).after('<a href="mailto:'+address+'">'+ address +'</a>')
      $(this).remove();
  });

  // activate empty search on start page
  $("#bs-searchMainPage").submit(function (evt) {
    $(this).find(":input").filter(function () {
          return !this.value;
      }).attr("disabled", true);
    return true;
  });

  $.cookieBar({
    fixed: true,
    message: 'Diese Website nutzt Cookies, um bestmögliche Funktionalität bieten zu können. Mit der Nutzung dieser Seiten erklären Sie, dass Sie die rechtlichen Hinweise gelesen haben und akzeptieren.',
    acceptText: 'Akzeptieren',
    policyButton: true,
    policyText: 'Hinweise zum Datenschutz',
    policyURL: '/content/below/rights.xml',
    expireDays: 7,
    domain: 'publikationsserver.tu-braunschweig.de',
    referrer: 'publikationsserver.tu-braunschweig.de'
  });

});