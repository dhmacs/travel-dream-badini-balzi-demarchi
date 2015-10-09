$( document ).ready(function() {
    //$('#content-wrapper').css('height',$('#content').height());
    $('#login-wrapper').css('width',$('.inner').first().width());
    $('#login-wrapper').center();

    $( window ).resize(function() {
        $('#login-wrapper').center();
    });

    var $loginForm = $("#login-wrapper");
    $loginForm.hide();

    $loginForm.delay(1000).fadeIn(1000);

    /* VIDEO */;
});

jQuery.fn.center = function () {
    this.css("position","absolute");
    this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) +
        $(window).scrollTop()) + "px");
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) +
        $(window).scrollLeft()) + "px");
    return this;
}