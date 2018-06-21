var system = require('system');
var fs = require('fs');
var page = require('webpage').create();
// argument 0 is always the file which is called (this)
var index = 1;
page.onConsoleMessage = function(msg, lineNum, sourceId) {
  console.log('CONSOLE: ' + msg + ' (from line #' + lineNum + ' in "' + sourceId + '")');
  console.log("==========="+index+"=========");
  index = index+1;
  
};



console.log(window.location.hostname);  
console.log(window.location.href); 
console.log(window.location.pathname);
var url = 'http://localhost:8080/BAE_4_3_REST_API_TEST_V1/scripts/rest_api_test_runner_local_jenkins.html';
//var url = 'http://localhost:8080/BAE_4_3_REST_API_TEST/scripts/index2.html';
//var url = 'https://bwrestapiqa.boardwalktech.com:8443/BAE_4_3_REST_API_TEST/scripts/rest_api_test_runner.html';

console.log("Opening " + url);

page.open(url, function (status) {
	
    console.log("Status: " + status);
    if (status === "success") {
        setTimeout(function () {
            var path = 'results.xml';
					
            var output = page.evaluate(function () {				
				return document.getElementById('myid').innerHTML;
                return document.output;
            });
            fs.write(path,  output, 'w');
			console.log(output);
            console.log("Wrote JUnit style output of QUnit tests into " + path);
            console.log("Tests finished. Exiting.");
            phantom.exit();
        }, 100000000);
    } else {
        console.log("Failure opening" + url + ". Exiting.");
        phantom.exit();
    }
});
