/*
 * (Singleton) Top options controller.
 * 
 * author: Felipe Osorio Thomé
 */
//const fs = require('fs');
let modelType = "True";
let warmupTime = "False";
define(["jquery", "JsonManager"],
    function($, jsonManager) {
        "use strict";

        var lastAction = null;

        var OpGen = {
            execute: function() {
                //gerar o .gv
                //se a linguagem for R, Java ou Python, e tiver multiservers, tem que emitir um aviso
                
                //if opParam_lbrary = r || java || python
                console.log("passando pelo execute do opgen");
                const opParam = document.getElementById("opParam_library");
                const selectedValue = opParam?.value;
                if (selectedValue == 'R' || selectedValue == 'Java' || selectedValue == 'Python')
                {
                    const mapNodes = jsonManager.getGraph().mapNodes;
                    for (let key in mapNodes) 
                    {
                        if (jsonManager.getGraph().mapNodes[key]?.properties?.multiServer_nbrServers !== undefined) 
                        {
                            alert("Remova os multiservers ou use linguagem C");
                            return;
                        }         
                    }  
                }
                //for jsonManager.getGraph().mapNodes.length if node.properties.multiServer_nbrServers exists
                //alert
                
                var parameters = jsonManager.getGraphParameters();
                var execTime = parameters["opParam_execTime"] || 0;
                var numCycles = parameters["opParam_numCycles"] || 0;
                var batchSize = parameters["opParam_batchSize"] || 0;
                var maxEntities = parameters["opParam_maxEntities"] || 0;
                //let modelType = document.getElementById("opParam_open").checked ;
                //let warmupTime = document.getElementById("opParam_timeDefined").checked;
             
                var definedValue = parameters["opParam_definedValue"] || 0;
                var seed = parameters["opParam_seed"] || 0;
                let content = `digraph ${jsonManager.getGraph().name} {\n    comment=" ${execTime} ${numCycles} ${batchSize} ${maxEntities} ${modelType} ${warmupTime} ${definedValue} ${seed} " rankdir=LR\n`;
   
                    Object.values(jsonManager.getGraph().mapNodes).forEach(node => {
                        
                        content += `    ${node.id} [label=${node.type} comment=`;
                        switch (node.type) {
                            case "source":
                                content += `1]\n`;
                                break;
                            case "server":
                                content += `" 2`;
                                switch (node.properties.arrival_distribution)
                                {
                                    case "Normal":
                                        content += ` 0`;
                                        break;
                                    case "Exponential":
                                        content += ` 1`;
                                        break;
                                    case "Uniform":
                                        content += ` 2`;
                                        break;
                                    default:
                                        content += ` 0`;    
                                }
                                switch (node.properties.server_distribution)
                                {
                                    case "Normal":
                                        content += ` 0`;
                                        break;
                                    case "Exponential":
                                        content += ` 1`;
                                        break;
                                    case "Uniform":
                                        content += ` 2`;
                                        break;
                                    default:
                                        content += ` 0`;    
                                }
                                
                                
                                content += ` ${node.properties.arrival_average} ${node.properties.server_average} 1 "]\n`;
                                break;
                            case "out":
                                content += `3]\n`;
                                break;
                            case "multiServer":
                                
                                content += `" 2`;
                                switch (node.properties.multiServer_arrival_distribution)
                                {
                                    case "Normal":
                                        content += ` 0`;
                                        break;
                                    case "Exponential":
                                        content += ` 1`;
                                        break;
                                    case "Uniform":
                                        content += ` 2`;
                                        break;
                                    default:
                                        content += ` 0`;    
                                }
                                switch (node.properties.server_distribution)
                                {
                                    case "Normal":
                                        content += ` 0`;
                                        break;
                                    case "Exponential":
                                        content += ` 1`;
                                        break;
                                    case "Uniform":
                                        content += ` 2`;
                                        break;
                                    default:
                                        content += ` 0`;    
                                }
                                
                                
                                if(node.properties.multiServer_nbrServers) content += ` ${node.properties.arrival_average} ${node.properties.server_average} ${node.properties.multiServer_nbrServers} "]\n`;
                                else content += ` ${node.properties.arrival_average} ${node.properties.server_average} 1 "]\n`;
                                break;
                            default:
                                content += `" 2 1 1 1 0.1 "]\n`;
                        }
                    });
                
                    Object.values(jsonManager.getGraph().mapNodes).forEach(node => {
                        Object.keys(node.mapTargets).forEach(targetId => {
                            content += `    ${node.id} -> ${targetId}\n`;
                        });
                    });
                
                    content += "}\n";
                    //baixar o .gv
                    const blob = new Blob([content], { type: "text/plain" }); // Criar um arquivo de texto
                    const url = URL.createObjectURL(blob); // Criar URL do Blob

                    const a = document.createElement("a"); // Criar link
                    a.href = url;
                    a.download = "graph.gv"; // Nome do arquivo
                    document.body.appendChild(a);
                    a.click(); // Simular clique
                    document.body.removeChild(a);
                    URL.revokeObjectURL(url); // Liberar memória
            },
            getLastAction: function() {
                return lastAction;
            }
        };
    return OpGen;
    }
);
  
 function checkModelType() { 

                
                    const closed = document.getElementById("opParam_closed");
                    const open = document.getElementById("opParam_open");
                    if (closed.checked) {
                      modelType = "False";
                    } else if (open.checked) {
                      modelType = "True";
                    } 
                
                    const defined = document.getElementById("opParam_timeDefined");
                    const automatic = document.getElementById("opParam_timeAutomatic");
                    if (defined.checked) {
                      warmupTime = "True";
                    } else if (automatic.checked) {
                      warmupTime = "False";
                    }
                }