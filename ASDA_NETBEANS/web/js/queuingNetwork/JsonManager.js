/*
 * (Singleton) Json manager. Keeps the graph in json format.
 * 
 * author: Felipe Osorio Thomé
 */

define(["jquery", "jsPlumb"],
    function($, jsPlumb) {
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
                jsPlumb.select({target: element.id}).each(function(connection) {
                    delete graph.mapNodes[connection.sourceId].mapTargets[element.id];
                });

                delete graph.mapNodes[element.id];

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