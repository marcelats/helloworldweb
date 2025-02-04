/**
 * (Closure) Generates an unique id for an element on the graph.
 * @author Felipe Osorio Thomé
 */

define([], function () {
    var newCid = -1;
    
    return {
        getNewCid : function() {
            newCid++;
            return newCid;
        }
    };
});