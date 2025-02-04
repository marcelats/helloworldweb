/* author: Felipe Osorio Thomé */

/* Closure: objPos. Keep the position of an object. The function is generic, but drawArea div is the main target */
var objPos = (function () {
    "use strict";
    var currTop = 0, currLeft = 0;

    return {
        update: function (obj) {
            /* Don't let the results be accumulated on every call */
            currTop = 0;
            currLeft = 0;
            
            if (obj.offsetParent) {
                do {
                    currLeft += obj.offsetLeft;
                    currTop += obj.offsetTop;
                    obj = obj.offsetParent;
                } while (obj);
            }
        },
        getCurrLeft: function () {
            return currLeft;
        },
        getCurrTop: function () {
            return currTop;
        }
    };
}());

/* Captures coordinates of the event "e" */
function captureCoords(e) {
    "use strict";
    return {X: e.clientX, Y: e.clientY};
}

/* Cross-browser stop propagation */
function stopPropagation(e) {
    "use strict";
    if (typeof e.stopPropagation !== "undefined") {
        e.stopPropagation();
    }
    else {
        e.cancelBubble = true;
    }
}

/* Doesn't let user interact with div. Don't forget to create the appropriate css class */
function blockDiv(divId, divStyles) {
    "use strict";
    var div = document.createElement("div");

    div.id = "block" + divId;
    div.className = divStyles;
    document.getElementById(divId).appendChild(div);

    $("#" + divId).fadeTo("slow", 0.5);
}

/* Let the user interact with div again */
function unBlockDiv(divId) {
    "use strict";
    var div = document.getElementById("block" + divId),
    father = div.parentNode;

    $("#" + divId).fadeTo("slow", 1.0);
    father.removeChild(div);
}