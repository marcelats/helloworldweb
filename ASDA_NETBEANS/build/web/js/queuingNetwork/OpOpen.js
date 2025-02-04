/*
 * (Singleton) Top options controller.
 * 
 * author: Felipe Osorio Thomé
 */

define(["jquery", "LightBoxManager", "JsonManager", "OpNew", "Utils", "Cons"],
    function($, lightBoxManager, jsonManager, opNew, utils, cons) {
        "use strict";

        var lastAction = null, callback = null, elementManager = null;

        var OpOpen = {
            initialize: function(manager) {
                elementManager = manager;

                /* Close button of the light box. */
                $(document).on("click", "#opOpen-btClose", function() {
                    lightBoxManager.closeBox(cons.SHADOWING, cons.BOX_CONTAINER);
                });

                $(document).on("click", "#opOpen-btSubmit", function() {
                    OpOpen.execute("submit");
                });
            },
            execute: function(action) {
            if(!jsonManager.isSaved()) {
                opNew.execute(function() {
                    var userChose = opNew.getLastAction();
                    
                    if(userChose !== "cancel") {
                        OpOpen.execute();
                    }
                });
            }
                if (typeof action !== "string") {
                    lightBoxManager.openBox(cons.SHADOWING, cons.BOX_CONTAINER,
                        "qnetwork?cmd=open-box&type=open", function() {
                        $("#opOpen-filename").focus();
                    });

                    callback = action;

                } else {
                    lastAction = action;
                    
                    if (action === "submit") {
                        var filename = $("#opOpen-filename").val(),
                            re = new RegExp(cons.REG_EXP_FILENAME);

                        if (filename.match(re) === null) {
                            alert("You need enter a valid filename.");

                        } else {
                            open(filename);
                        }
                    }

                    if (typeof callback === "function") {
                        callback();
                        callback = null;
                    }
                }
            },
            getLastAction: function() {
                return lastAction;
            }
        };

        /* --- Private methods. --- */

        function open(filename) {
            $.ajax({
                url: "qnetwork?cmd=open",
                type: "POST",
                data: "graphName=" + filename,
                dataType: "json",
                success: function(data) {
                    lightBoxManager.closeBox(cons.SHADOWING, cons.BOX_CONTAINER);

                    jsonManager.setGraph(data);
                    jsonManager.setSaved(true);

                    if (data.name !== "") {
                        document.title = data.name;
                    }

                    constructGraph(data);
                },
                error: function(xhr, ajaxOptions, thrownError) {
                    var errorHeader = xhr.getResponseHeader('fot-error');

                    if (errorHeader != null) {
                        alert(errorHeader);
                    } else {
                        alert(thrownError);
                    }
                }
            });
        }

        function opParam(action) {
            if (typeof action === "undefined") {
                lightBoxManager.openBox(cons.SHADOWING, cons.BOX_CONTAINER,
                    "qnetwork?cmd=open-box&type=parameters", function() {
                    var parameters = jsonManager.getGraphParameters();

                    $("#" + cons.BOX_CONTAINER).values(parameters);
                });

            } else if (action === "submit") {
                var parameters = $("#" + cons.BOX_CONTAINER).values();

                jsonManager.setGraphParameters(parameters);

                lightBoxManager.closeBox(cons.SHADOWING, cons.BOX_CONTAINER);
            }
        }

        function constructGraph(graph) {
            $("#" + cons.DRAW_AREA).empty();

            if (!utils.mapIsEmpty(graph.mapNodes)) {

                for (var key in graph.mapNodes) {

                    var position = {
                        x: parseInt(graph.mapNodes[key].x),
                        y: parseInt(graph.mapNodes[key].y)
                    };

                    elementManager.add(graph.mapNodes[key].type, position, key);
                }

                for (var keyNode in graph.mapNodes) {

                    var mapTargets = graph.mapNodes[keyNode].mapTargets;

                    if (!utils.mapIsEmpty(mapTargets)) {
                        for (var keyTarget in mapTargets) {
                            elementManager.linkElements($("#" + keyNode));
                            elementManager.linkElements($("#" + keyTarget));
                        }
                    }
                }
            }
        }

        return OpOpen;
    }
);