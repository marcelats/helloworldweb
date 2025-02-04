/*
 * (Singleton) Responsible for initialize the jquery plugins.
 * 
 * author: Felipe Osorio Thomé
 */

define(["jquery"],
    function($) {
        "use strict";

        var JqueryPlugins = {
            initialize: function() {
                initialize_values();
            }
        };

        /* --- Private methods. --- */
        
        /* Jquery plugin for serialize and re-populate a form.
         * Thanks for stackoverflow community. */
        function initialize_values() {
            $.fn.values = function(data) {
                var els = $(this).find(':input').get();

                if (typeof data != 'object') {
                    // return all data
                    data = {};

                    $.each(els, function() {
                        if (this.id && !this.disabled && (this.checked
                            || /select|textarea/i.test(this.nodeName)
                            || /text|password/i.test(this.type))) {
                            data[this.id] = $(this).val();
                        }
                    });

                    return data;
                } else {
                    $.each(els, function() {
                        if (this.id && data[this.id]) {
                            if (this.type == 'checkbox' || this.type == 'radio') {
                                $(this).attr("checked", (data[this.id] == $(this).val()));
                            } else {
                                $(this).val(data[this.id]);
                            }
                        }
                    });
                    return $(this);
                }
            };
        }

        return JqueryPlugins;
    }
);