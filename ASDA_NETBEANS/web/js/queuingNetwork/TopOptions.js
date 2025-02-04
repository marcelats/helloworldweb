/*
 * (Singleton) Top options controller.
 * 
 * author: Felipe Osorio Thomé
 */

define(["OpNew", "OpSave", "OpOpen", "OpParam"],
    function(opNew, opSave, opOpen, opParam) {
        "use strict";

        var elementManager = null;

        var TopOptions = {
            initialize: function(manager) {
                elementManager = manager;
                
                opNew.initialize();
                opSave.initialize();
                opOpen.initialize(elementManager);
                opParam.initialize();
                
            },
            ctrl: function(option) {
                if (option === "new") {
                    opNew.execute();

                } else if (option === "save") {
                    opSave.execute();

                } else if (option === "open") {
                    opOpen.execute();

                } else if (option === "parameters") {
                    opParam.execute();

                } else if (option === "generate") {
                    //opGen.execute();

                } else if (option === "report") {
                    //opReport.execute();

                } else if (option === "code") {
                    //opCode.execute();

                } else if (option === "manual") {
                    //opHelp.execute();
                }
            }
        };

        return TopOptions;
    }
);