/*
 * (Singleton) Block or unblock a div.
 * 
 * author: Felipe Osorio Thomé
 */

define(["jquery"],
    function($) {
        "use strict";

        /* Block or unblock user interaction with a div. Don't forget to create the appropriate css class */
        var DivManager = {
            blockDiv: function(divId) {
                var div = document.getElementById("block" + divId);

                if (div === null) {
                    div = document.createElement("div");
                    div.id = "block" + divId;

                    /* This can be very dangerous, but works for this layout. */
                    if ($("#" + divId).css("position") === "static") {
                        $("#" + divId).css("position", "relative");
                    }

                    $(div).css({
                        position: "absolute",
                        top: 0,
                        left: 0,
                        zIndex: 1000,
                        width: $("#" + divId).width(),
                        height: $("#" + divId).height()
                    });

                    document.getElementById(divId).appendChild(div);

                    $("#" + divId).fadeTo("fast", 0.5);
                } else {
                    /* Let the user interact with div again */
                    var father = div.parentNode;
                    
                    $("#" + divId).fadeTo("fast", 1.0);
                    father.removeChild(div);
                }
            }
        };

        return DivManager;
    }
);