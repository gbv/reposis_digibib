
$(document).ready(function() {

  // replace placeholder USERNAME with username
  var userID = $("#currentUser strong").html();
  var newHref = 'https://publikationsserver.tu-braunschweig.de/servlets/solr/select?q=state:%22submitted%22+%2Bcreatedby:' + userID + '&fq=objectType:mods';
  $("a[href='https://publikationsserver.tu-braunschweig.de/servlets/solr/select?q=state:%22submitted%22+%2Bcreatedby:USERNAME']").attr('href', newHref);

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