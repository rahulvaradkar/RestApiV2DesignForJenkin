var UserTests =(function( module, test ) {

	function createNewUser() {
		var x = Math.floor((Math.random() * 10000) + 1);
		var Response;
		var data = {
		  "firstName":"u",
		  "lastName": "u",
		  "password":"u",
		  "email": "u"+x+"@gmail.com",
		  "externalid": "u",
		  "id":"0"
		};

		return TestUtils.sendPostRequest(Globals.baseURL + "rest/user",data);		
	  }

	module('User');
	var userid=0;
  
	QUnit.test("Test Case For Single User", function( assert ) {
		var done = assert.async();		
		TestUtils.sendGetRequest(Globals.baseURL + "rest/user/1").then(function(result){
			var flag=false;
			if(result.email !=null && result.firstName != null && result.lastName !=null && result.id != null && result.password != null) 
			{						
				flag=true;
			}
			assert.equal(typeof result.email, "string", "Email should be String");
			assert.equal(typeof result.firstName, "string", "Firstname should be String");
			assert.equal(typeof result.lastName, "string", "Last name should be String");
			assert.equal(typeof result.id, "number", "Id Should be number");
			assert.ok(result.id > 0,"Id should greater than 0");
			assert.equal(typeof result.password, "string", "Password should be String");
			assert.ok(result != null , "Response Should not null")
		    assert.ok(flag,"Object should have 5 Properties");
		    done();
		  });
	});

	QUnit.test("Test Case For User With id=2", function( assert ) {
		var done = assert.async();
		var input = $( "#test-input" ).focus();
		TestUtils.sendGetRequest(Globals.baseURL + "rest/user/2").then(function(result){
			var flag=false;
				if(result.email !=null && result.firstName != null && result.lastName !=null)
				{						
					flag=true;
				}					
		        assert.ok(flag,"Object should not Null");
		        done();
		  });	
  	});
		
	QUnit.test("Test Case For Negative user Id", function( assert ) {
		var done = assert.async();		
		TestUtils.sendGetRequest(Globals.baseURL + "rest/user/-1").then(function(result){			
			assert.ok(result != null , "Response Should not null")
			assert.ok(result[0].error,"IsNegativeOrZero","User Id is Negative");			
			assert.ok(result[0].proposedSolution,"UserId must be Positive Integer","UserId must be Positive Integer");
		    done();
		  });
	});

	QUnit.test("Test Case For zero user Id", function( assert ) {
		var done = assert.async();		
		TestUtils.sendGetRequest(Globals.baseURL + "rest/user/0").then(function(result){			
			assert.ok(result != null , "Response Should not null")
			assert.ok(result[0].error,"IsNegativeOrZero","User Id is Zero");			
			assert.ok(result[0].proposedSolution,"UserId must be Positive Integer","UserId must be Positive Integer");
		    done();
		  });
	});

	QUnit.test("Test Case For User Not Found", function( assert ) {
		var done = assert.async();		
		TestUtils.sendGetRequest(Globals.baseURL + "rest/user/789689").then(function(result){			
			assert.ok(result != null , "Response Should not null")
			assert.ok(result[0].error,"404 - User not found","User Not Found");		
			assert.ok(result[0].proposedSolution,"The requested user does not exist. Enter correct UserId","The requested user does not exist. Enter correct UserId");
		    done();
		  });
	});

	QUnit.test("Test Case For Missing Authorization for user ID = 1", function( assert ) {
		var done = assert.async();	
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/user/1", null, "GET").then(function(result){			
			assert.ok(result != null , "Response Should not null")
			assert.ok(result[0].error,"Missing Authorization in Header","Missing Authorization in Header");		
			assert.ok(result[0].proposedSolution,"Authorization Header should contain user:pwd:nhPath as Base64 string","Authorization Header should contain user:pwd:nhPath as Base64 string");
		    done();
		});			
	});

	QUnit.test( "Test Case For all Active Users", function( assert ) {
		var done = assert.async();
		var input = $( "#test-input" ).focus();
		var flag= false;
		var count_flag=false;
		TestUtils.sendGetRequest(Globals.baseURL + "rest/user?active=true").then(function(result){			
			if (result.length > 0)
			{
				count_flag=true;
			}
			assert.ok(count_flag,"Retrieved All Active Users");
			if (count_flag)
			{
				for(var i=0;i<result.length;i++)
				{
					if(result[i].email !=null && result[i].firstName != null && result[i].lastName !=null && result[i].id != null && result[i].password != null && result[i].externalId != null)
					{						
						flag=true;
					}
					else
					{
						flag = false;
						break;
					}
					assert.equal(typeof result[i].email, "string", "Email should be String");
					assert.equal(typeof result[i].firstName, "string", "Firstname should be String");
					assert.equal(typeof result[i].lastName, "string", "Last name should be String");
					assert.equal(typeof result[i].externalId, "string", "externalid shold be string")
					assert.equal(typeof result[i].id, "number", "Id Should be number");
					assert.ok(result[i].id > 0,"Id should greater than 0");
					assert.equal(typeof result[i].password, "string", "Password should be String");
				}
				assert.ok(flag,"Parameters should not be null");
			}		    
		    done();
		});	
	});

	QUnit.test("Test Case For Missing Authorization for Active Users", function( assert ) {
		var done = assert.async();	
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/user?active=true", null, "GET").then(function(result){			
			assert.ok(result != null , "Response Should not null")
			assert.ok(result[0].error,"Missing Authorization in Header","Missing Authorization in Header");		
			assert.ok(result[0].proposedSolution,"Authorization Header should contain user:pwd:nhPath as Base64 string","Authorization Header should contain user:pwd:nhPath as Base64 string");
		    done();
		});			
	});
		
	QUnit.test("Test Case For all non-active Users", function( assert ) {
		var done = assert.async();
		var input = $( "#test-input" ).focus();
		var flag= false;
		var count_flag=false;
		TestUtils.sendGetRequest(Globals.baseURL + "rest/user?active=false").then(function(result){
			if (result.length >= 0)
			{
				count_flag=true;
			}
			if (count_flag)
			{
				for(var i=0;i<result.length;i++)
				{
					if(result[i].email !=null && result[i].firstName != null && result[i].lastName !=null && result[i].id != null && result[i].password != null && result[i].externalId != null)
					{
						flag=true;
					}
					else
					{
						flag = false;
						break;
					}
					assert.equal(typeof result[i].email, "string", "Email should be String");
					assert.equal(typeof result[i].firstName, "string", "Firstname should be String");
					assert.equal(typeof result[i].lastName, "string", "Last name should be String");
					assert.equal(typeof result[i].externalId, "string", "externalid shold be string")
					assert.equal(typeof result[i].id, "number", "Id Should be number");
					assert.ok(result[i].id > 0,"Id should greater than 0");
					assert.equal(typeof result[i].password, "string", "Password should be String");
				}
				assert.ok(flag,"Parameters should not be null");
			}
		    assert.ok(count_flag,"Object should not null");
		    done();
		});	
	});
	
	test( "Post test cases for new User", function(assert) {
		var done = assert.async();
		createNewUser().then(function(result) {			
			var json = result[0];
			assert.notEqual(result.length,0,"object should not be empty !");
			if(Object.keys(json).length == 3)
			assert.equal(Object.keys(json).length, 3,"Error, trying to create existing user!");
			else {
			  	userid=json.id;
				assert.equal(Object.keys(json).length, 4,"user should have 4 properties !");
				assert.equal(json.email != null, true, "email should not be null");
				assert.equal(json.firstName != null, true, "firstName should not be null");
				assert.equal(json.lastName != null, true, "lastName should not be null");
	
			}
			done();
		});
	});

	QUnit.test("Test Case For Invalid Authorization for Posting new user", function( assert ) {
		var done = assert.async();
		var x = Math.floor((Math.random() * 10000) + 1);
		var data = {
			"firstName":"u",
			"lastName": "u",
			"password":"u",
			"email": "u"+x+"@gmail.com",
			"externalid": "u",
			"id":"0"
		  };
		TestUtils.sendRequestInvalidAuthorization(Globals.baseURL + "rest/user",data, "POST").then(function(result){			
			assert.ok(result != null , "Response Should not null")
			assert.equal(result[0].error,"Invalid Authorization. User is not a member of Neighborhood Path.","Invalid Authorization. User is not a member of Neighborhood Path.");		
			assert.equal(result[0].proposedSolution,"User must have valid Membership of Neighborhood Collaboration of Grid","User must have valid Membership of Neighborhood Collaboration of Grid");
		    done();
		});			
	});

	QUnit.test( "Post test cases for new User with Blank Name And Password", function( assert ) {
		var done = assert.async();
		var data = {
			"firstName":"",
			"lastName": "",
			"password":"saurav123",
			"email": "saurav@gmail.com",
			"externalid": "sauravg",
			"id":"0"
		}
		TestUtils.sendPostRequest(Globals.baseURL + "rest/user",data).then(function(result){
			var json = result[0];
			assert.notEqual(result.length,0,"object should not be empty !");
			if(Object.keys(json).length == 3)
			{  
				assert.equal(Object.keys(json).length, 3,"Error, trying to create existing user!");
			} 
			else 
			{
				userid=json.id;
				assert.equal(Object.keys(json).length, 4,"user should have 4 properties !");
				assert.ok(json.email != null, "email should not be null");
				assert.ok(json.firstName != null, "firstName should not be null");
				assert.ok(json.lastName != null, "lastName should not be null");
			} 
			done();
		});
	});

	QUnit.test("Put test cases for User", function(assert){
		var done = assert.async();
		var data = {
			"firstName":"j",
			"lastName": "jp",
			"password":"0",
			"email": "j",
			"externalid": "j",
			"id":"1001"
		}
		TestUtils.sendPutRequest(Globals.baseURL + "rest/user",data).then(function(res){
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");
			assert.equal(res.includes("Successfully"), true, "record must be updated successfully !");
			done();
		});		
	});

	QUnit.test("Put test cases for User with Blank email, firstName, lastName", function(assert){
		var done = assert.async();
		var data = {
			"firstName":"",
			"lastName": "",
			"password":"0",
			"email": "",
			"externalid": "j",
			"id":"1001"
		}
		TestUtils.sendPutRequest(Globals.baseURL + "rest/user",data).then(function(res){
			assert.ok(res != null,"Response should not be null");
			assert.equal(res[0].error,"IsBlank","firstName is Blank");
			assert.equal(res[1].error,"IsBlank","lastName is Blank");
			assert.equal(res[2].error,"IsBlank","email is Blank");			
			done();
		});		
	});

	QUnit.test("Put test cases for User with Missing email, firstName, lastName", function(assert){
		var done = assert.async();
		var data = {			
			"lastName": "j",
			"password":"0",			
			"externalid": "j",
			"id":"1001"
		}
		TestUtils.sendPutRequest(Globals.baseURL + "rest/user",data).then(function(res){
			assert.ok(res != null,"Response should not be null");
			assert.equal(res[0].error,"IsMissing","firstName is Blank");		
			assert.equal(res[1].error,"IsMissing","email is Blank");			
			done();
		});		
	});

	QUnit.test( "Test case for Deleting User", function( assert ) {
		var done=assert.async();		
		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/user/"+userid).then(function(res){
			var getResponse = res;
 			var checkSubString = getResponse.indexOf("de-activated successfully");
  			assert.ok((checkSubString > 0),"User Deleted Successfully!");
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");			
			done();
		});		
	});
	
	QUnit.test( "Test case for Deleting User with Missing Authorization", function( assert ) {
		var done=assert.async();		
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/user/1", null, "DELETE").then(function(result){	
			console.log((result));	
			assert.ok(result != null , "Response Should not null")
			assert.equal(result[0].error,"Missing Authorization in Header","Missing Authorization in Header");		
			assert.equal(result[0].proposedSolution,"Authorization Header should contain user:pwd:nhPath as Base64 string","Authorization Header should contain user:pwd:nhPath as Base64 string");
		    done();			
		});
	});

	return {
		createNewUser: createNewUser
	  };
})( QUnit.module, QUnit.test );