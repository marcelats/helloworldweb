/*
 * (Singleton) Keep the last tool that the user selected.
 * 
 * author: Felipe Osorio Thomé
 */

define(["Cons"],
    function(cons) {
        "use strict";

        var tool = "none";
        
        function defineStatus() {
            document.getElementById(cons.STATUS_TOOL).innerHTML =
                "Current tool: " + tool;
        }

        return {
            setTool: function(toolId) {
                tool = toolId;
                defineStatus();
            },
            getTool: function() {
                return tool;
            }
        };
    }
);