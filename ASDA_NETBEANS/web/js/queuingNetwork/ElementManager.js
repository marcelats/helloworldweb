/*
 * Add, remove or link elements in a div.
 * 
 * author: Felipe Osorio Thomé
 */

define(["jquery", "jsPlumb", "DrawArea", "PropertiesArea", "JsonManager", "Utils"],
    function($, jsPlumb, drawArea, propertiesArea, jsonManager, utils) {
        "use strict";

        function ElementManager(folder, format, drawArea) {
            this.folder = folder;
            this.format = format;
            this.drawArea = drawArea;
            this.prevElement = null;
            this.prevEndPoint = null;
        }

        ElementManager.prototype.add = function(type, position, id) {
            var img = document.createElement("img"),
                elementDiv = document.createElement("div"),
                idDiv = document.createElement("div");

            img.src = this.folder + type + this.format;
            elementDiv.id = id;
            elementDiv.name = type;
            idDiv.innerHTML = elementDiv.id;

            $(idDiv).addClass("idDiv");
            $(elementDiv).css({
                position: "absolute",
                left: (position.x) + "px",
                top: (position.y) + "px"
            });

            this.addEventHandlers(elementDiv);

            elementDiv.appendChild(idDiv);
            elementDiv.appendChild(img);
            document.getElementById(this.drawArea).appendChild(elementDiv);

            return elementDiv;
        };

        ElementManager.prototype.addEventHandlers = function(element) {
            $(element).click(function(event) {
                drawArea.ctrl(event, element);
            });

            $(element).dblclick(function() {
                propertiesArea.ctrl(element);
            });

            $(element).draggable({
                drag: function() {
                    jsPlumb.repaintEverything();
                },
                stop: function(event) {
                    jsonManager.changeNodePosition(element);
                    
                    utils.stopPropagation(event);
                }
            });
        };

        ElementManager.prototype.remove = function(element) {
            var father = element.parentNode;

            jsPlumb.detachAllConnections(element);
            father.removeChild(element);
        };

        ElementManager.prototype.linkElements = function(element) {
            var connection = null;

            var targetEndPoint,
                sourceOption = {
                anchor: "RightMiddle",
                isSource: true,
                endpoint: "Blank"
            },
            targetOption = {
                anchor: "LeftMiddle",
                isTarget: true,
                endpoint: "Blank"
            },
            linkConnector = {
                paintStyle: {lineWidth: 3, strokeStyle: "#660700"},
                overlays: [["PlainArrow", {location: 1, width: 15, length: 12}]]
            };

            if (this.prevEndPoint === null) {
                this.prevElement = element;
                /* Tells jsPlumb to create the first end point */
                this.prevEndPoint = jsPlumb.addEndpoint(element, sourceOption);
            } else {
                /* Avoid links between the same element or links that already exists */
                if (this.prevElement !== element && !checkExistingLinks(this.prevElement, element)) {
                    /* Tells jsPlumb to create the second end point */
                    targetEndPoint = jsPlumb.addEndpoint(element, targetOption);
                    /* Finally, connects elements */
                    connection = jsPlumb.connect({source: this.prevEndPoint, target: targetEndPoint}, linkConnector);
                    /* Reinitialize variables for a future link */
                    this.prevElement = null;
                    this.prevEndPoint = null;
                } else {
                    this.prevElement = null;
                    this.prevEndPoint = null;
                }
            }

            function checkExistingLinks(prevElement, element) {
                var flag = false;

                jsPlumb.select({source: prevElement.id}).each(function(connection) {
                    if (connection.targetId === element.id) {
                        flag = true;
                    }
                });

                return flag;
            }

            return connection;
        };

        return ElementManager;
    }
);