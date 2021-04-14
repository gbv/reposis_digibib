$(document).ready(function() {
    $("body").on("change", ".publication-type-select", function () {
        const type = $(this).val();
        let parent = $(this).parent().parent().parent().parent();
        let container = $(".embargo-container");
        if (type == "oae") {
            container.removeClass("d-none");
            parent.addClass("mir-fieldset-content")
        } else {
            container.addClass("d-none");
            parent.removeClass("mir-fieldset-content")
        }
    });

    $("body").on("change", ".project-type-select", function () {
        const type = $(this).val();
        let container = $(".mir-openAIRE-container");
        $(this).parent().parent().parent().parent().toggleClass("mir-fieldset-content");
        if (type == "oaire") {
            container.removeClass("d-none");
        } else {
            container.addClass("d-none");
        }
    });

    //TODO on load
});
