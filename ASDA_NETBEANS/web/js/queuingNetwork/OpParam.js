/*
 * (Singleton) Top options controller.
 * 
 * author: Felipe Osorio Thomé
 */

define(["jquery", "LightBoxManager", "JsonManager", "Utils", "Cons"],
    function($, lightBoxManager, jsonManager, utils, cons) {
        "use strict";

        var lastAction = null, callback = null;

        var OpParam = {
            initialize: function() {

                /* Close button of the light box. */
                $(document).on("click", "#opParam-btClose", function() {
                    lightBoxManager.closeBox(cons.SHADOWING, cons.BOX_CONTAINER);
                });

                $(document).on("click", "#opParam-btSubmit", function() {
                    OpParam.execute("submit");
                });
            },
            execute: function(action) {
                if (typeof action !== "string") {
                    lightBoxManager.openBox(cons.SHADOWING, cons.BOX_CONTAINER,
                        "qnetwork?cmd=open-box&type=parameters", function() {
                        var parameters = jsonManager.getGraphParameters();

                        $("#" + cons.BOX_CONTAINER).values(parameters);
                    });

                    callback = action;

                } else {
                    lastAction = action;
                    
                    if (action === "submit") {
                        var parameters = $("#" + cons.BOX_CONTAINER).values();

                        jsonManager.setGraphParameters(parameters);
                        lightBoxManager.closeBox(cons.SHADOWING, cons.BOX_CONTAINER);
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

        return OpParam;
    }
);