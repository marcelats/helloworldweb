/* author: Felipe Osorio Thomé */

function closeLightBox(shadowing, box) {
    $("#" + box).slideUp("slow", function() {
        $("#" + shadowing).fadeOut("slow");
    });
}

function openLightBox(shadowing, box) {  
    $("#" + shadowing).fadeIn("slow", function() {
        $("#" + box).slideDown("slow");
    });

    /* Centering the div. */
    
    $("#" + box).css({
        left: $(window).width() > $("#" + box).outerWidth() ?
            ($(window).width() - $("#" + box).outerWidth()) / 2 : 0,
        top: $(window).height() > $("#" + box).outerHeight() ?
            ($(window).height() - $("#" + box).outerHeight()) / 2 : 0,
        width: $("#" + box).width(),
        height: $("#" + box).height()
    });

    $(window).resize(function() {
        $("#" + box).css({
            left: $(window).width() > $("#" + box).outerWidth() ?
                ($(window).width() - $("#" + box).outerWidth()) / 2 : 0,
            top: $(window).height() > $("#" + box).outerHeight() ?
                ($(window).height() - $("#" + box).outerHeight()) / 2 : 0
        });
    });
}