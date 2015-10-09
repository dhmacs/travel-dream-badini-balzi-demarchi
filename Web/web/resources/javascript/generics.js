$( document ).ready(function() {
    // Clear not not useful styles
    $('a').removeClass('ui-commandlink ui-widget');

    // Search box
    var $menuBar = $("#menu-bar");
    var $errorMessage = $("[id*='search-box-message']").first();
    $errorMessage.addClass("search-box-error");
    $errorMessage.delay(800).fadeOut(400);
    $errorMessage.horizontalCenter();
    $errorMessage.css("top", $menuBar.height() + "px");

    // DIALOG BOX
    var $shareButton = $(".share-package-button").first();
    var $dialogBox = $("#share-dialog-box");
    var $cancelButton = $("[id*='cancel-share-package-button']").first();
    var $confirmButton = $("[id*='confirm-share-package-button']").first();

    $("body").append("<div id='blackOverlay'></div>");

    var $blackOverlay = $("#blackOverlay")
    $blackOverlay.hide();
    $blackOverlay.css("position", "fixed");
    $blackOverlay.css("z-index", "50");
    $blackOverlay.css("min-height", "100%");
    $blackOverlay.css("min-width", "100%");
    $blackOverlay.css("height", "auto");
    $blackOverlay.css("width", "auto");
    $blackOverlay.css("right", "0px");
    $blackOverlay.css("top", "0px");
    $blackOverlay.css('background-color', 'rgba(0,0,0, 0.5)');

    $dialogBox.css("z-index", "100");

    $shareButton.click(function() {
        $dialogBox.center();
        $blackOverlay.fadeIn(400);
        $dialogBox.fadeIn(400);
    });

    $cancelButton.click(function() {
        $dialogBox.fadeOut(400);
        $blackOverlay.fadeOut(400);
    });

    $confirmButton.click(function() {
        $dialogBox.fadeOut(400);
        $blackOverlay.fadeOut(400);
    });

});

jQuery.fn.horizontalCenter = function () {
    this.css("position","fixed");
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) +
        $(window).scrollLeft()) + "px");
    return this;
}

jQuery.fn.center = function () {
    this.css("position","fixed");
    this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) +
        $(window).scrollTop()) + "px");
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) +
        $(window).scrollLeft()) + "px");
    return this;
}