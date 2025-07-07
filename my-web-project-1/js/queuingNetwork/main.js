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
        function waitForElement(selector, timeout = 5000) {
  return new Promise((resolve, reject) => {
    const element = document.querySelector(selector);
    if (element) {
      resolve(element);
      return;
    }

    const observer = new MutationObserver(() => {
      const elem = document.querySelector(selector);
      if (elem) {
        resolve(elem);
        observer.disconnect();
      }
    });

    observer.observe(document.body, { childList: true, subtree: true });

    setTimeout(() => {
      observer.disconnect();
      reject(new Error(`Timeout: Element ${selector} not found after ${timeout}ms`));
    }, timeout);
  });
}

waitForElement("#opParam_library").then(opParam => {
  const multiServer = document.getElementById("multiServer");
  if (!multiServer) {
    console.error("Elemento #multiServer não encontrado!");
    return;
  }
  
const fieldset = document.getElementById("queueStatsFieldset");
  if (!fieldset) {
    console.error("Elemento #queuestats não encontrado!");
  }
  


  const allowed = ['C SMPL', 'C SMPLX', 'C ParSMPL', 'C SIMPACK', 'C SIMPACK2'];

  function updateMultiServer() {
    const selected = opParam.value;
    console.log("Selecionado:", selected);

    if (allowed.includes(selected)) {
      multiServer.style.opacity = '1';
      multiServer.style.pointerEvents = 'auto';

      fieldset.style.opacity = '1';
      fieldset.style.pointerEvents = 'auto';
    } else {
      multiServer.style.opacity = '0.3';
      multiServer.style.pointerEvents = 'none';

      fieldset.style.opacity = '0.3';
      fieldset.style.pointerEvents = 'none';
    }
  }

  updateMultiServer();

  opParam.addEventListener("change", () => {
    updateMultiServer();
    console.log(`Opção escolhida mudou para: ${opParam.value}`);
  });
}).catch(err => {
  console.error(err.message);
});

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
        (function() {
  const allowed = ['C SMPL', 'C SMPLX', 'C ParSMPL', 'C SIMPACK', 'C SIMPACK2'];

  function updateMultiServer(opParam) {
    const selected = opParam.value;
    console.log("Selecionado:", selected);

    const multiServer = document.getElementById("multiServer");
    if (!multiServer) {
      console.warn("#multiServer não encontrado.");
      return;
    }
    const fieldset = document.getElementById("queueStatsFieldset");
  if (!fieldset) {
    console.error("Elemento #queuestats não encontrado!");
  }
    if (allowed.includes(selected)) {
      multiServer.style.opacity = '1';
      multiServer.style.pointerEvents = 'auto';

      fieldset.style.opacity = '1';
      fieldset.style.pointerEvents = 'auto';
    } else {
      multiServer.style.opacity = '0.3';
      multiServer.style.pointerEvents = 'none';
  
      fieldset.style.opacity = '0.3';
      fieldset.style.pointerEvents = 'none';
    }
  }

  function attachLogic(opParam) {
    if (opParam.dataset.listenerAttached) return; // Evita múltiplos listeners

    updateMultiServer(opParam);

    opParam.addEventListener("change", () => {
      updateMultiServer(opParam);
    });

    opParam.dataset.listenerAttached = "true";
  }

  const observer = new MutationObserver(() => {
    const opParam = document.querySelector("#opParam_library");
    if (opParam) {
      attachLogic(opParam);
    }
  });

  observer.observe(document.body, {
    childList: true,
    subtree: true
  });
})();
        
    }
);