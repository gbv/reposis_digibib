
$(document).ready(function() {

  if(typeof enginesItem !== "undefined") {
    enginesItem["digibib_subject"] = {
      engine: new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.whitespace,
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
          url: webApplicationBaseURL + 'servlets/solr/select?%QUERY',
          wildcard: '%QUERY',
          transform: function (list) {
            list = list.facet_counts.facet_fields["digibib.mods.subject.string"];
            const result = [];
            for (let i = 0; i < list.length; i += 2) {
              let el = list[i];
              result.push(el);
            }
            return result;
          }, prepare: function (query, settings) {
            const param = "fq=%2BobjectType:\"mods\"" +
                "&fq=%2Bmods.genre:module_manual" +
                "&version=4.5&rows=0&wt=json" +
                "&facet.field=digibib.mods.subject.string" +
                "&q=%2Bdigibib.mods.subject.string:*" + query + "*";

            settings.url = settings.url.replace("%QUERY", param);
            return settings;
          }
        }
      })
    };
  }

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

  // open search bar
  $( ".js-search-toggler" ).click(function() {
    $( ".searchfield_box" ).addClass('open');
  });
  // close searchbar
  // listen to all clicks
  $(document).click(function(event) {
    var $click = $(event.target);
    // search bar is visible AND
    // clicked element is not inside of the search bar AND
    // clicked element is not the toggle itself
    if( $('.searchfield_box').hasClass("open") &&
        !$click.closest('.js-leo-searchbar').length &&
        !$click.closest('.js-search-toggler').length ) {
      $( ".searchfield_box" ).removeClass('open');
    }
  });

});