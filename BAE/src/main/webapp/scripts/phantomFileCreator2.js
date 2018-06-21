var system = require('system');
var fs = require('fs');
var page = require('webpage').create();
// argument 0 is always the file which is called (this)


console.log(window.location.hostname);  
console.log(window.location.href); 
console.log(window.location.pathname);
var url = 'http://localhost:8080/BAE_4_3_REST_API_TEST/scripts/rest_api_test_runner_local.html';
console.log("Opening " + url);

page.open(url, function (status) {
	
    console.log("Status: " + status);
    if (status === "success") {
        setTimeout(function () {
            var path = 'results.xml';
					
            var output = page.evaluate(function () {				
				return document.getElementById('myid').innerHTML;
                //return document.output;
            });
			console.log("output :"+output+" finished");
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