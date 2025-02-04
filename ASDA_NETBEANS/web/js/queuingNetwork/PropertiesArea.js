/*
 * (Singleton) Properties area controller. He can be called by to ways: double
 * click on an added element or by the buttons in the properties form.
 * 
 * author: Felipe Osorio Thomé
 */

define(["JsonManager", "Cons"],
    function(jsonManager, cons) {
        "use strict";

        var propertiesArea = {
            initialize: function() {
                /* The propertiesArea click event mapping */
                $(document).on("click", "#btSubmit", function() {
                    propertiesArea.ctrl("submit");
                });
                $(document).on("click", "#btCancel", function() {
                    propertiesArea.ctrl("cancel");
                });
            },
            ctrl: function(element) {
                /* Invoked from an element. */
                if (typeof element !== "string") {
                    /* If already opened close it */
                    if ($("#" + cons.PROPERTIES_AREA).css("display") !== "none") {
                        closeDiv(element);
                    }
                    if (element.name === "server" || element.name === "source" || element.name === "multiServer") {
                        openDiv(element);
                    }
                }
                /* Invoked from within propertiesArea div. */
                else {
                    closeDiv();

                    if (element === "submit") {
                        var properties = $("#" + cons.PROPERTIES_AREA).values();
                        var callerId = $("#" + cons.HIDDEN_FIELD_ID).val();
                        var tempElement = document.getElementById(callerId);

                        jsonManager.setNodeProperties(tempElement, properties);
                    }
                }
            }
        };

        /* --- Private methods. --- */

        function openDiv(element) {
            /* The jquery width() returns a value without "px". */
            $("#" + cons.DRAW_AREA).width($("#" + cons.DRAW_AREA).outerWidth()
                - $("#" + cons.PROPERTIES_AREA).outerWidth());
            $("#" + cons.PROPERTIES_AREA).css("display", "inline");

            $("#" + cons.PROPERTIES_AREA).load(
                "qnetwork?cmd=open-properties&type=" + element.name,
                function() {
                    prepareForm(element);
                }
            );

        }

        function prepareForm(element) {
            document.getElementById(cons.HIDDEN_FIELD_ID).value = element.id;
            document.getElementById(cons.HIDDEN_FIELD_TYPE).value = element.name;

            $("#" + cons.PROPERTIES_AREA).values(jsonManager.getNodeProperties(element));
        }

        function closeDiv() {
            $("#" + cons.DRAW_AREA).width($("#" + cons.DRAW_AREA).outerWidth()
                + $("#" + cons.PROPERTIES_AREA).outerWidth());
            $("#" + cons.PROPERTIES_AREA).css("display", "none");
        }

        return propertiesArea;
    }
);