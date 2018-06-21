

var system = require('system');
var fs = require('fs');
var page = require('webpage').create();
page.open("http://localhost:8080/BAE_4_3_REST_API_TEST/scripts/rest_api_test_runner_local.html", function (status) {
	
    console.log("Status: " + status);
    if (status === "success") {
        setTimeout(function () {
            var path = 'results.xml';
					
            var output = page.evaluate(function () {
				
				return document.getElementById('myid').innerHTML;
                //return document.output;
            });
			console.log(output);
            fs.write(path,  output, 'w+');
			
	
            console.log("Wrote JUnit style output of QUnit tests into " + path);
            console.log("Tests finished. Exiting.");
            phantom.exit();
        }, 3000);
    } else {
        console.log("Failure opening" + url + ". Exiting.");
        phantom.exit();
    }
});