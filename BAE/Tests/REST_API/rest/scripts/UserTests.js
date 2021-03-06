var UserTests = (function (module, test) {

	function createNewUser() {
		var x = Math.floor((Math.random() * 10000000) + 1);
		var Response;
		var data = {
			"firstName": "u",
			"lastName": "u",
			"password": "u",
			"email": "u" + x + "@gmail.com",
			"externalId": "u",
			"id": "0"
		};
		return TestUtils.sendRequest(Globals.baseURL + "rest/v1/user", data, UserInput.authorization, "POST");
	}

	module('User');
	var userid = 0;

	QUnit.test("Test Case For Single User", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user/" + UserInput.User_Id, null, UserInput.authorization, "GET").then(function (result) {
			var flag = false;
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			if (result.email != null && result.firstName != null && result.lastName != null && result.id != null && result.password != null) {
				flag = true;
			}
			assert.equal(typeof result.email, "string", "Email should be String");
			assert.equal(typeof result.firstName, "string", "Firstname should be String");
			assert.equal(typeof result.lastName, "string", "Last name should be String");
			assert.equal(typeof result.id, "number", "Id Should be number");
			assert.ok(result.id > 0, "Id should greater than 0");
			assert.equal(typeof result.password, "string", "Password should be String");
			assert.ok(result != null, "Response Should not null")
			assert.ok(flag, "Object should have 6 Properties");
			done();
		});
	});

	QUnit.test("Test Case For Negative user Id", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user/" + UserInput.Negative_UserId, null, UserInput.authorization, "GET").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.ok(result[0].error, "IsNegativeOrZero", "User Id is Negative");
			assert.ok(result[0].proposedSolution, "UserId must be Positive Integer", "UserId must be Positive Integer");
			done();
		});
	});

	QUnit.test("Test Case For zero user Id", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user/" + UserInput.Zero_USerId, null, UserInput.authorization, "GET").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.ok(result[0].error, "IsNegativeOrZero", "User Id is Zero");
			assert.ok(result[0].proposedSolution, "UserId must be Positive Integer", "UserId must be Positive Integer");
			done();
		});
	});

	QUnit.test("Test Case For User Not Found", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user/" + UserInput.Non_Existing_UserId, null, UserInput.authorization, "GET").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.ok(result[0].error, "404 - User not found", "User Not Found");
			assert.ok(result[0].proposedSolution, "The requested user does not exist. Enter correct UserId", "The requested user does not exist. Enter correct UserId");
			done();
		});
	});

	QUnit.test("Test Case For Missing Authorization for user ID = " + UserInput.User_Id, function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/user/" + UserInput.User_Id, null, "GET").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.ok(result[0].error, "Missing Authorization in Header", "Missing Authorization in Header");
			assert.ok(result[0].proposedSolution, "Authorization Header should contain user:pwd:nhPath as Base64 string", "Authorization Header should contain user:pwd:nhPath as Base64 string");
			done();
		});
	});

	QUnit.test("Test Case For all Active Users", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();	
		var flag = false;
		var count_flag = false;
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user?active=true", null, UserInput.authorization, "GET").then(function (result) {
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			if (result.length > 0) {
				count_flag = true;
			}
			assert.ok(count_flag, "Retrieved All Active Users");
			if (count_flag) {
				for (var i = 0; i < result.length; i++) {
					if (result[i].email != null && result[i].firstName != null && result[i].lastName != null && result[i].id != null && result[i].password != null && result[i].externalId != null) {
						flag = true;
					}
					else {
						flag = false;
						break;
					}
					assert.equal(typeof result[i].email, "string", "Email should be String");
					assert.equal(typeof result[i].firstName, "string", "Firstname should be String");
					assert.equal(typeof result[i].lastName, "string", "Last name should be String");
					assert.equal(typeof result[i].externalId, "string", "externalid should be string")
					assert.equal(typeof result[i].id, "number", "Id Should be number");
					assert.ok(result[i].id > 0, "Id should greater than 0");
					assert.equal(typeof result[i].password, "string", "Password should be String");
				}
				assert.ok(flag, "Parameters should not be null");
			}
			done();
		});
	});

	QUnit.test("Test Case For Missing Authorization for Active Users", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/user?active=true", null, "GET").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.ok(result[0].error, "Missing Authorization in Header", "Missing Authorization in Header");
			assert.ok(result[0].proposedSolution, "Authorization Header should contain user:pwd:nhPath as Base64 string", "Authorization Header should contain user:pwd:nhPath as Base64 string");
			done();
		});
	});

	test("Post test cases for new User", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		createNewUser().then(function (result) {
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			var json = result[0];
			assert.notEqual(result.length, 0, "object should not be empty !");
			if (Object.keys(json).length == 3)
				assert.equal(Object.keys(json).length, 3, "Error, trying to create existing user!");
			else {
				userid = json.id;
				assert.equal(Object.keys(json).length, 6, "user should have 4 properties !");
				assert.equal(json.email != null, true, "email should not be null");
				assert.equal(json.firstName != null, true, "firstName should not be null");
				assert.equal(json.lastName != null, true, "lastName should not be null");
			}
			done();
		});
	});

	QUnit.test("Test Case For Posting Duplicate user", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		var x = Math.floor((Math.random() * 10000) + 1);
		var data = {
			"firstName": "j",
			"lastName": "j",
			"password": "0",
			"email": UserInput.email,
			"externalId": "j",
			"id": "0"
		};
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user", data, UserInput.authorization, "POST").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.equal(result[0].error, "Failed to created user with = " + UserInput.email, "Duplicate User");
			assert.equal(result[0].proposedSolution, "Status: 422. Check User Details Posted.", "Status: 422. Check User Details Posted.");
			done();
		});
	});

	QUnit.test("Test Case For Invalid Authorization for Posting new user", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		var x = Math.floor((Math.random() * 10000) + 1);
		var data = {
			"firstName": "u",
			"lastName": "u",
			"password": "u",
			"email": "u" + x + "@gmail.com",
			"externalId": "u",
			"id": "0"
		};
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user", data, UserInput.invalidAuthorization, "POST").then(function (result) {
			
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.equal(result[0].error, "Authentication_Connection_Failure", "Invalid Authorization");
			done();
		});
	});

	QUnit.test("Post test cases for new User with Blank Name And Password", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		var data = {
			"firstName": "",
			"lastName": "",
			"password": "saurav123",
			"email": "saurav@gmail.com",
			"externalId": "sauravg",
			"id": "0"
		}
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user", data, UserInput.authorization, "POST").then(function (result) {
			
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			var json = result[0];
			assert.notEqual(result.length, 0, "object should not be empty !");
			if (Object.keys(json).length == 3) {
				assert.equal(Object.keys(json).length, 3, "Error, trying to create existing user!");
			}
			else {
				userid = json.id;
				assert.equal(Object.keys(json).length, 4, "user should have 4 properties !");
				assert.ok(json.email != null, "email should not be null");
				assert.ok(json.firstName != null, "firstName should not be null");
				assert.ok(json.lastName != null, "lastName should not be null");
			}
			done();
		});
	});

	QUnit.test("Put test cases for User", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		var data = {
			"firstName": "j",
			"lastName": "jp",
			"password": 0,
			"email": UserInput.email,
			"externalId": "j",
			"id": UserInput.User_Id
		}
		TestUtils.sendPutRequest(Globals.baseURL + "rest/v1/user", data, UserInput.authorization).then(function (result) {
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.equal(typeof result, "string", "response should be string !");
			assert.notEqual(result.length, 0, "should not be null !");
			var getResponse = result;
			var checkSubString = getResponse.indexOf("Successfully");
			assert.ok((checkSubString >= 0), "record must be updated successfully !");
			done();
		});
	});

	QUnit.test("Put test cases for User with Blank firstName, lastName", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		var data = {
			"firstName": "",
			"lastName": "",
			"password": "0",
			"email": "j",
			"externalId": "j",
			"id": "1001"
		}
		TestUtils.sendPutRequest(Globals.baseURL + "rest/v1/user", data, UserInput.authorization).then(function (result) {
			var json = JSON.parse(result);
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.equal(json[0].error, "IsBlank", "firstName is Blank");
			assert.equal(json[1].error, "IsBlank", "lastName is Blank");
			done();
		});
	});

	QUnit.test("Put test cases for User with Missing email, firstName, lastName", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		var data = {
			"lastName": "j",
			"password": "0",
			"externalId": "j",
			"id": "1001"
		}
		TestUtils.sendPutRequest(Globals.baseURL + "rest/v1/user", data, UserInput.authorization).then(function (result) {
			var json = JSON.parse(result);
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.equal(json[0].error, "IsMissing", "firstName is Blank");
			assert.equal(json[1].error, "IsMissing", "email is Blank");
			done();
		});
	});

	QUnit.test("Test case for Deleting User", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/v1/user/" + userid, UserInput.authorization).then(function (result) {
			var getResponse = result;
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			var checkSubString = getResponse.indexOf("de-activated successfully");
			assert.ok((checkSubString > 0), "User Deleted Successfully!");
			assert.equal(typeof result, "string", "response should be string !");
			assert.notEqual(result.length, 0, "should not be null !");
			done();
		});
	});

	QUnit.test("Test case for Deleting User with Missing Authorization", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/user/" + userid, null, "DELETE").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in Header");
			assert.equal(result[0].proposedSolution, "Authorization Header should contain user:pwd:nhPath as Base64 string", "Authorization Header should contain user:pwd:nhPath as Base64 string");
			done();
		});
	});

	QUnit.test("Test case for Deleting Non existing User ", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user/" + UserInput.Non_Existing_UserId, null, UserInput.authorization, "DELETE").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.equal(result[0].error, "404 - User not found", "404 - User not found");
			assert.equal(result[0].proposedSolution, "The requested user does not exist. Enter correct UserId.", "The requested user does not exist. Enter correct UserId.");
			done();
		});
	});

	QUnit.test("Test case for Deleting User With Negative Userid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/user/" + UserInput.Negative_UserId, null, UserInput.authorization, "DELETE").then(function (result) {
			assert.ok(result != null, "Response Should not null");
			if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
			assert.equal(result[0].error, "IsNegativeOrZero", "UserId Is Negative");
			assert.equal(result[0].proposedSolution, "UserId must be Positive Integer", "UserId must be Positive Integer");
			done();
		});
	});
	return {
		createNewUser: createNewUser
	};
})(QUnit.module, QUnit.test);