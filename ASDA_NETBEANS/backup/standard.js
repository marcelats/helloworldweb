/* author: Felipe Osorio Thomé */

/* I only used jquery where I really needed or simplified the code. */

/* Closure: activeTool. Keep the last tool that the user selected */
var activeTool = (function () {
    "use strict";
    
    var tool = "none";

    return {
        setTool: function (toolId) {
            tool = toolId;
            document.getElementById("bottomStatus").innerHTML = "Current tool: " + tool;
        },
        getTool: function () {
            return tool;
        }
    };
}());

/* Draw area controller. He can be called by to ways: clicking on the div drawArea or an added element */
function drawAreaCtrl(opt) {
    "use strict";
    
    var e = opt.theEvent || window.event,
    img = opt.theImg,
    tool = activeTool.getTool();

    /* Called by drawArea div */
    if (typeof img === "undefined") {
        if (tool === "server" || tool === "multiServer" || tool === "source" || tool === "out") {
            addElement(captureCoords(e));
        }
    }
    /* Called by an added element */
    else {
        if (tool === "erase") {
            remElement(img);
        }
        else if (tool === "link") {
            linkElements(img);
        }

        stopPropagation(e); /* If an element was clicked the event does not propagate to the div */
    }
}

/* Properties area controller. He can be called by to ways: double click on an added element or by the buttons in the form */
function propertiesCtrl(action) {
    "use strict";
    
    /* Called by double click (make the div visible) */
    if($("#propertiesArea").css("display") === "none") {
        /* The jquery width() returns a value without "px" */
        $("#drawArea").width($("#drawArea").width() - $("#propertiesArea").width());
        $("#propertiesArea").css("display", "inline");
    }
    /* Called by the buttons */
    else {
        /* Make the div not visible again */
        if(action === "cancel") {
            $("#drawArea").width($("#drawArea").width() + $("#propertiesArea").width());
            $("#propertiesArea").css("display", "none");
        }
    }
}

function addElement(coords) {
    "use strict";
    
    var img = document.createElement("img");

    /* For simplicity the tool id and image file has the same name */
    img.src = "img/element/" + activeTool.getTool() + ".gif";
    
    /* 37.5 is half of size of the image. For the image center and cursor have the same position */
    img.style.position = "absolute";
    img.style.left = (coords.X - objPos.getCurrLeft() - 37.5) + "px";
    img.style.top = (coords.Y - objPos.getCurrTop() - 37.5) + "px";
    
    img.name = activeTool.getTool();
    img.id = idManager.getNewCid();

    enableDrag(img);

    /* Preventing onclick event be executed (jquery ui fire onclick when drags) */
    img.ondragstart = function () {
        img.wasDragged = true;
    };
    img.onclick = function (e) {
        if (img.wasDragged === true) {
            img.wasDragged = false;
        }
        /* Remember: 'undefined' is falsy */
        else {
            drawAreaCtrl({theEvent: e, theImg: img});
        }
    };
    
    /* Obs: ondblclick also fire click event. Just warning you! */
    img.ondblclick = function () {
        propertiesCtrl(activeTool.getTool());
    };

    document.getElementById("drawArea").appendChild(img);
}

function remElement(img) {
    "use strict";
    
    var father = img.parentNode;

    jsPlumb.detachAllConnections(img);
    father.removeChild(img);

}

function linkElements(img) {
    "use strict";
    
    /* Simulating the behaviour of static variables */
    if (typeof linkElements.prevImg === "undefined") {
        linkElements.prevImg = null;
    }
    if (typeof linkElements.prevEndPoint === "undefined") {
        linkElements.prevEndPoint = null;
    }

    var targetEndPoint,
    sourceOption = {
        anchor: "RightMiddle",
        isSource: true,
        reattach: true,
        endpoint: "Blank"
    },
    targetOption = {
        anchor: "LeftMiddle",
        isTarget: true,
        reattach: true,
        endpoint: "Blank"
    },
    linkConnector = {
        paintStyle: {lineWidth: 3, strokeStyle: "#600"},
        overlays: [["PlainArrow", {location: 1, width: 15, length: 12}]]
    };

    if (linkElements.prevImg === null || linkElements.prevEndPoint === null) {
        linkElements.prevImg = img;
        /* Tells jsPlumb to create the first end point */
        linkElements.prevEndPoint = jsPlumb.addEndpoint(img, sourceOption);
        /* Block leftTools div preventing the user of selecting another tool */
        blockDiv("leftTools", "blocked");
    }
    else {
        /* Avoid links between the same element */
        if (linkElements.prevImg !== img) {
            /* Tells jsPlumb to create the second end point */
            targetEndPoint = jsPlumb.addEndpoint(img, targetOption);
            /* Finally, connects elements */
            jsPlumb.connect({source: linkElements.prevEndPoint, target: targetEndPoint}, linkConnector);
            /* Reinitialize variables for a future link */
            linkElements.prevImg = null;
            linkElements.prevEndPoint = null;
            /* Unblock leftTools div allowing user to select another tool */
            unBlockDiv("leftTools");
        }
        else {
            linkElements.prevImg = null;
            linkElements.prevEndPoint = null;
            unBlockDiv("leftTools");
        }
    }
}

function enableDrag(img) {
    "use strict";
    
    jsPlumb.ready(function () {
        jsPlumb.draggable(img);
    });
}

window.onload = function () {
    "use strict";
    
    objPos.update(document.getElementById("drawArea"));
};

/* On resize the position of an object changes */
window.onresize = function () {
    "use strict";
    
    objPos.update(document.getElementById("drawArea"));
};