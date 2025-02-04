/*
 * (Singleton) Generate an unique id for an element in the graph.
 * 
 * author: Felipe Osorio Thomé
 */

define([],
    function() {
        "use strict";

        var newCid = -1;

        return {
            getNewCid: function() {
                newCid++;
                return newCid;
            },
            setStartCid: function(start) {
                newCid = start;
            }
        };
    }
);