/*
 * (Singleton) Open or Close a light box.
 * 
 * author: Felipe Osorio Thomé
 */

define([],
    function() {
        "use strict";

        return {
            openBox: function(shadowing, box, link, callback) {
                $("#" + shadowing).fadeIn("fast", function() {
                    if (typeof link !== "undefined") {
                        $("#" + box).load(link, function() {
                            $("#" + box).css({
                                display: "inline-block",
                                top: $(window).height() > $("#" + box).outerHeight() ?
                                    ($(window).height() - $("#" + box).outerHeight()) / 2 : 0
                            });

                            $(window).resize(function() {
                                $("#" + box).css({
                                    top: $(window).height() > $("#" + box).outerHeight() ?
                                        ($(window).height() - $("#" + box).outerHeight()) / 2 : 0
                                });
                            });

                            if (typeof callback === "function") {
                                callback();
                            }
                        });
                    }
                });
            },
            closeBox: function(shadowing, box, callback) {
                $("#" + box).css("display", "none");
                $("#" + shadowing).fadeOut("fast", function() {
                    if (typeof callback === "function") {
                        callback();
                    }
                });
            }
        };
    }
);