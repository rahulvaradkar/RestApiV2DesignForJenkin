testcases for the Boardwalk ReST API.

To verify QUnit testcases are running fine:
1. Copy "scripts" and "rest-api-test-runner.html" to your deployed instance path (eg. "C:\Apache Software Foundation\Tomcat8.0\webapps\BAE4").
2. From your browser, hit <baseURL>/rest-api-test-runner.html and check whether you get QUnit reports.

To commit your own test JS file:
1. Place your test JS file in "scripts" directory. Ensure you have named it plurally eg. UserTests.js.
2. Find and replace all `"http://localhost:<port>/<instance>/` with `Globals.baseURL + "`
3. Add <script src="scripts/UserTests.js"></script> in "rest-api-test-runner.html" 
4. Run the "rest-api-test-runner.html" from browser.
5. Once you are ok with the tests, commit your test JS file in the same directory structure along with the "rest-api-test-runner.html" file.
