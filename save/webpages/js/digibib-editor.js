$(document).ready(function() {
    function handlePublicationTypeSelect(value) {
        const parent = $(".mir-publication");
        const container = $(".mir-publication-embargo-container");
        if (value == "oae") {
            container.removeClass("d-none");
            parent.addClass("mir-fieldset-content");
        } else {
            container.addClass("d-none");
            parent.removeClass("mir-fieldset-content");
        }
    }

    function handleProjectTypeSelect(value) {
        const parent = $(".mir-project");
        const container = $(".mir-project-openAIRE-container");
        if (value == "oaire") {
            container.removeClass("d-none");
            parent.addClass("mir-fieldset-content");
        } else {
            container.addClass("d-none");
            parent.removeClass("mir-fieldset-content");
        }
    }

    function init() {
        const publicationType = $(".mir-publication-type-select").val();
        handlePublicationTypeSelect(publicationType);
        const projectType = $(".mir-project-type-select").val();
        handleProjectTypeSelect(projectType);
    }

    $("body").on("change", ".mir-publication-type-select", function () {
        const value = $(this).val();
        handlePublicationTypeSelect(value);
    });

    $("body").on("change", ".mir-project-type-select", function () {
        const value = $(this).val();
        handleProjectTypeSelect(value);
    });

    init();
});
