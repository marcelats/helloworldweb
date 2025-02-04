/* author: Felipe Osorio Thomé */

$(document).ready(function () {
    /* leftTools event mapping */
    $("#server").click(function () {
        activeTool.setTool("server");
    });
    $("#multiServer").click(function () {
        activeTool.setTool("multiServer");
    });
    $("#source").click(function () {
        activeTool.setTool("source");
    });
    $("#out").click(function () {
        activeTool.setTool("out");
    });
    $("#link").click(function () {
        activeTool.setTool("link");
    });
    $("#erase").click(function () {
        activeTool.setTool("erase");
    });
    
    /* The drawArea click event mapping */
    $("#drawArea").click(function (event) {
        drawAreaCtrl({theEvent: event});
    });
    
    /* The propertiesArea click event mapping */
    $("#btCancel").click(function () {
        propertiesCtrl("cancel");
    });
    $("#btSubmit").click(function () {
        propertiesCtrl("submit");
    });
});