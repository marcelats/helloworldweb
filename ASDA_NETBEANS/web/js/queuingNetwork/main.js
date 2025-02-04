/*
 * author: Felipe Osorio Thomé
 */

require(["jquery", "DrawArea", "PropertiesArea", "TopOptions", "ActiveTool", "ElementManager", "JqueryPlugins", "Cons", "domReady!"],
    function($, drawArea, propertiesArea, topOptions, activeTool, ElementManager, jqueryPlugins, cons) {
        "use strict";

        /*
         * Fill the entire window.
         * Before, I had used css calc function, but when the window has the properties div opened
         * the calc was replaced by a value calculated by jquery and this breaks the layout.
         * Sorry for this, but I did not find any better solution to accomplish this "dynamic fill".
         */
        $("#" + cons.CENTER).height(($(window).height() -
            $("#" + cons.TOP).height() - $("#" + cons.BOTTOM).height()));
        $("#" + cons.DRAW_AREA).width(($(window).width() -
            $("#" + cons.LEFT_TOOLS).outerWidth()));

        /* On resize must be recalculated */
        $(window).resize(function() {
            var propertiesAreaWidth;

            $("#" + cons.CENTER).height(($(window).height() -
                $("#" + cons.TOP).height() - $("#" + cons.BOTTOM).height()));

            if ($("#" + cons.PROPERTIES_AREA).css("display") !== "none") {
                propertiesAreaWidth = $("#" + cons.PROPERTIES_AREA).width();
            } else {
                propertiesAreaWidth = 0;
            }

            $("#" + cons.DRAW_AREA).width(($(window).width() -
                $("#" + cons.LEFT_TOOLS).outerWidth() - propertiesAreaWidth));
        });

        /* Creating an instance of ElementManager. */
        var elementManager = new ElementManager(cons.FOLDER, cons.FORMAT, cons.DRAW_AREA);
        
        /* Initializing the three app main sectors. */
        topOptions.initialize(elementManager);
        drawArea.initialize(elementManager);
        propertiesArea.initialize();
        
        /* Initializing jquery plugins. */
        jqueryPlugins.initialize();

        /* Registering main events. */
        var eventMap = function() {
            /* topMenu event map */
            $("#opNew").click(function() {
                topOptions.ctrl("new");
            });
            $("#opSave").click(function() {
                topOptions.ctrl("save");
            });
            $("#opOpen").click(function() {
                topOptions.ctrl("open");
            });
            $("#opParam").click(function() {
                topOptions.ctrl("parameters");
            });
            $("#opGen").click(function() {
                topOptions.ctrl("generate");
            });
            $("#opReport").click(function() {
                topOptions.ctrl("report");
            });
            $("#opCode").click(function() {
                topOptions.ctrl("code");
            });
            $("#opManual").click(function() {
                topOptions.ctrl("manual");
            });

            /* leftMenu event map */
            $("#server").click(function() {
                activeTool.setTool("server");
            });
            $("#multiServer").click(function() {
                activeTool.setTool("multiServer");
            });
            $("#source").click(function() {
                activeTool.setTool("source");
            });
            $("#out").click(function() {
                activeTool.setTool("out");
            });
            $("#link").click(function() {
                activeTool.setTool("link");
            });
            $("#erase").click(function() {
                activeTool.setTool("erase");
            });

            /* The drawArea click event mapping */
            $("#drawArea").click(function(event) {
                drawArea.ctrl(event);
            });

            $(document).bind('ajaxComplete', function(event, xhr, options) {
                var redirectHeader = xhr.getResponseHeader('fot-redirect');

                if (xhr.readyState == 4 && redirectHeader != null) {
                    window.location.href = redirectHeader;
                }
            });
        }();
    }
);