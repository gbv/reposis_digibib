function replaceMaskedEmails() {
  document.querySelectorAll('span.madress').forEach(span => {
    const address = span.textContent.replace(' [at] ', '@');
    const link = document.createElement('a');
    link.href = `mailto:${address}`;
    link.textContent = address;
    span.replaceWith(link);
  });
}

function ignoreEmptyFieldsOnSubmit(event) {
  const form = event.currentTarget;
  const inputs = form.querySelectorAll('input');
  inputs.forEach(input => {
    if (!input.value) {
      input.dataset.nameBackup = input.name;
      input.removeAttribute('name');
    }
  });
  // Restore field names after the form is submitted
  // setTimeout ensures this runs after the submit event completes
  setTimeout(() => {
    inputs.forEach(input => {
      if (input.dataset.nameBackup) {
        input.name = input.dataset.nameBackup;
        delete input.dataset.nameBackup;
      }
    });
  }, 0);
}

function init() {
  replaceMaskedEmails();
}

document.addEventListener("DOMContentLoaded", init);

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
