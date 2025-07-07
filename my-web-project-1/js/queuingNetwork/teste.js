const fs = require('fs');
var graph = new Graph(), saved = true;

function Graph() {
    this.name = "mm1";
    this.parameters = {};
    this.mapNodes = {};
}

function Node(id, name) {
    this.id = id;
    this.name = name;
    this.properties = {};
    this.mapTargets = {};
}

function setGraph(pGraph) {
    graph = pGraph;
}

function addNode(element) {
    graph.mapNodes[element.id] = new Node(element.id, element.name);
}

function linkNodes(connection) {
    graph.mapNodes[connection.sourceId].mapTargets[connection.targetId] = 1;

    saved = false;
}

function getNodes(){
    return Object.entries(graph.mapNodes);
}

function getLinks(){
    const pairs = [];
    Object.values(graph.mapNodes).forEach(node => {
        Object.keys(node.mapTargets).forEach(targetId => {
            pairs.push([node.id, targetId]);
        });
    });
    return pairs;
}

function setGraphParameters(parameters) {
    graph.parameters = parameters;

    saved = false;
}

function getGraphParameters() {
    return graph.parameters;
}

function generateDot() {
    let content = `digraph ${graph.name} {\n    comment=" 2000 0 0 0 True False 0 10 " rankdir=LR\n`;

    Object.values(graph.mapNodes).forEach(node => {
        content += `    ${node.id} [label=${node.name} comment=${node.id+1}]\n`;
    })

    Object.values(graph.mapNodes).forEach(node => {
        Object.keys(node.mapTargets).forEach(targetId => {
            content += `    ${node.id} -> ${targetId}\n`;
        });
    });

    content += "}\n";

    fs.writeFileSync("graph.gv", content);
}


function main() {
    var graph = new Graph(), saved = true;
    setGraph(graph);
    var element1 = {};
    element1.id = 0;
    element1.name = "Source";
    addNode(element1);
    var element2 = {}; 
    element2.id = 1;
    element2.name = "CPU";
    addNode(element2);
    var element3 = {}; 
    element3.id = 2;
    element3.name = "Destination";
    addNode(element3);
    console.log(getNodes());
    var connection1 = {};
    connection1.sourceId = 0;
    connection1.targetId = 1;
    linkNodes(connection1);
    var connection2 = {};
    connection2.sourceId = 1;
    connection2.targetId = 2;
    linkNodes(connection2);
    console.log(getLinks());
    console.log(generateDot());
}

// Chamar a função main
main();