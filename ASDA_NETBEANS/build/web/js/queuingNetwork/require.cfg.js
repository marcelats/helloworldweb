/*
 * author: Felipe Osorio Thomé
 */

require.config({
	baseUrl: "js/queuingNetwork",
	paths: {
                "domReady": "../libs/domReady",
		"jquery": "../libs/jquery-1.10.2.min",
		"jquery-ui": "../libs/jquery-ui-1.10.4.custom.min",
		"jquery-validate": "../libs/jquery.validate.min",
		"jquery-form": "../libs/jquery.form.min",
		"jsPlumb": "../libs/jquery.jsPlumb-1.5.5-min"
	},
	shim: {
		"jquery-ui": ["jquery"],
		"jquery-validate": ["jquery"],
		"jquery-form": ["jquery"],
		"jsPlumb": {
			deps: ["jquery", "jquery-ui"],
			exports: "jsPlumb"
		}
	}
});