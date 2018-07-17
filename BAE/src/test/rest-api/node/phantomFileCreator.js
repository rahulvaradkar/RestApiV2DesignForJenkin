var system = require('system');
var fs = require('fs');
var page = require('webpage').create();
var Globals = require('../scripts/globals.js');
page.open(Globals.baseURL + "rest-api-test-runner.html", function (status) {
	
    console.log("Status: " + status);
    if (status === "success") {
        setTimeout(function () {
            var path = 'results.xml';
					
            var output = page.evaluate(function () {
				return document.getElementById('myid').innerHTML;

            });
            fs.write(path,  output, 'w');
            console.log("Wrote JUnit style output of QUnit tests into " + path);
            console.log("Tests finished. Exiting.");
            phantom.exit();
        }, 3000);
    } else {
        console.log("Failure opening" + url + ". Exiting.");
        phantom.exit();
    }
});