/*
 * (Singleton) Json manager. Keeps the graph in json format.
 * 
 * author: Felipe Osorio Thomé
 */

define(["jquery", "jsPlumb", "IdManager"],
    function($, jsPlumb, idManager) {
        "use strict";

        function Graph() {
            this.name = "untitled";
            this.parameters = {};
            this.mapNodes = {};
        }

        function Node(id, type, x, y) {
            this.id = id;
            this.type = type;
            this.x = x;
            this.y = y;
            this.properties = {};
            this.mapTargets = {};
        }

        var graph = new Graph(), saved = true;

        var JsonManager = {
            setGraph: function(pGraph) {
                graph = pGraph;
            },
            getGraph: function() {
                return graph;
            },
            setName: function(name) {
                graph.name = name;
            },
            getName: function() {
                return graph.name;
            },
            setGraphParameters: function(parameters) {
                graph.parameters = parameters;

                saved = false;
            },
            getGraphParameters: function() {
                return graph.parameters;
            },
            addNode: function(element) {
                graph.mapNodes[element.id] = new Node(element.id, element.name,
                    $(element).css("left"), $(element).css("top"));

                saved = false;
            },
            removeNode: function(element) {
                /*jsPlumb.select({target: element.id}).each(function(connection) {
                    delete graph.mapNodes[connection.sourceId].mapTargets[element.id];                   
                  
                });
                delete graph.mapNodes[element.id];
                for (const key in graph.mapNodes) {
                    const no = graph.mapNodes[key];

                    for (const targetId in no.mapTargets) {
                        if (targetId == element.id) {
                            delete no.mapTargets[targetId];
                        }
                    }
                }*/

                /*const novoMapa = new Map();
                Object.entries(graph.mapNodes).forEach(([oldId, node], index) => {
                    novoMapa.set(oldId, index); // ID antigo → novo
                    node.id = index;            // atualiza o próprio nó com o novo ID
                    console.log(node.id);
                });*/
                

                /*Object.keys(graph.mapNodes).forEach((oldId) => {
                    const no = graph.mapNodes[oldId];

                    const novosTargets = {};
                    Object.keys(no.mapTargets).forEach((targetIdAntigo) => {
                        const targetIdNovo = novoMapa.get(targetIdAntigo);
                        if (targetIdNovo !== undefined) {
                            novosTargets[targetIdNovo] = no.mapTargets[targetIdAntigo];
                            
                        }
                    });

                    no.mapTargets = novosTargets; // substitui pelos IDs atualizados
                    console.log(no.mapTargets);
                });*/
                 var father = element.parentNode;

            jsPlumb.detachAllConnections(element);
            father.removeChild(element);

            // 1. Remove do graph.mapNodes
            delete graph.mapNodes[element.id];

            // 2. Recria mapeamento de ID antigo -> novo ID sequencial
            const novoMapa = new Map();
            const chavesAntigas = Object.keys(graph.mapNodes);
            chavesAntigas.forEach((oldId, index) => {
                novoMapa.set(oldId, index);
                console.log("novoMapa:");
                console.log(novoMapa);
            });

            // 3. Atualiza os nós com os novos IDs
            const novosMapNodes = {};
            chavesAntigas.forEach((oldId) => {
                const no = graph.mapNodes[oldId];
                const novoId = novoMapa.get(oldId);

                // Atualiza id no objeto
                no.id = novoId;

                // Atualiza targets
                const novosTargets = {};
                Object.keys(no.mapTargets).forEach((targetIdAntigo) => {
                    const novoTarget = novoMapa.get(targetIdAntigo);
                    if (novoTarget !== undefined) {
                        novosTargets[novoTarget] = no.mapTargets[targetIdAntigo];
                    }
                });
                no.mapTargets = novosTargets;
                //console.log(no.mapTargets);
                novosMapNodes[novoId] = no;
            });
            graph.mapNodes = novosMapNodes;
            console.log("novosMapNode:");
            console.log(novosMapNodes);
//console.log(nos e arestas pra ver se ta certo)
            // 4. Atualiza a interface (DOM)
            $(".idDiv").each(function() {
                const $div = $(this);
                //const $element = $div.closest("div");
                const $element = $div.parent();
                console.log("element:");
                console.log($element);
                const oldId = $element.attr("id");
                
                console.log("oldId:");
                console.log(oldId);
                const novoId = novoMapa.get(oldId);
                console.log("novoId:");
                console.log(novoId);
                if (novoId !== undefined) {
                    jsPlumb.removeAllEndpoints($element);
                    $element.attr("id", novoId);
                    $div.text(novoId);
                }
            });

            // 5. Recriar conexões jsPlumb
            Object.values(graph.mapNodes).forEach((no) => {
                Object.keys(no.mapTargets).forEach((targetId) => {
                    jsPlumb.connect({
                        source: no.id.toString(),
                        target: targetId.toString(),
                        paintStyle: {lineWidth: 3, strokeStyle: "#660700"},
                        anchors: ["RightMiddle", "LeftMiddle"],
                        endpoint: "Blank",
                        overlays: [["PlainArrow", {location: 1, width: 15, length: 12}]]
                    });
                });
            });
                      
                idManager.setStartCid(Object.keys(graph.mapNodes).length);

                saved = false;
            },
            linkNodes: function(connection) {
                graph.mapNodes[connection.sourceId].mapTargets[connection.targetId] = 1;

                saved = false;
            },
            changeNodePosition: function(element) {
                graph.mapNodes[element.id].x = $(element).css("left");
                graph.mapNodes[element.id].y = $(element).css("top");

                saved = false;
            },
            setNodeProperties: function(element, properties) {
                graph.mapNodes[element.id].properties = properties;

                saved = false;
            },
            getNodeProperties: function(element) {
                return graph.mapNodes[element.id].properties;
            },
            stringifyGraph: function() {
                return JSON.stringify(graph);
            },
            parseGraph: function(stringJson) {
                graph = JSON.parse(stringJson);

                return graph;
            },
            setSaved: function(pSaved) {
                saved = pSaved;
            },
            isSaved: function() {
                return saved;
            },
            clearGraph: function() {
                graph = new Graph();
            }
        };

        return JsonManager;
    }
);