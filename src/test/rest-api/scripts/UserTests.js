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

		return TestUtils.for_POSTMethod(Globals.baseURL + "rest/user",data);
		// .then(function(result){
		// 	console.log("++++++++++++++++++++++++++++"+JSON.stringify(result));
		// 	Response=result;
		// });

		// var userCreated = new Promise(function(resolve, reject) {
		// 	console.log("-------------------"+JSON.stringify(Response));
		// 		resolve(Response);
		// });		  
		// return userCreated;
	  }

	module('User');
	var userid=0;
  
	QUnit.test("Test Case For Single User", function( assert ) {
		var done = assert.async();		
		TestUtils.for_GETMethod(Globals.baseURL + "rest/user/1").then(function(result){
			var flag=false;
			if(result.email !=null && result.firstName != null && result.lastName !=null)
			{						
					flag=true;
				}
				assert.ok(result != null , "Response Should not null")
		        assert.ok(flag,"Object should have 3 Properties");
		        done();
		  });
	});

	QUnit.test("Test Case For User With id=2", function( assert ) {
		var done = assert.async();
		var input = $( "#test-input" ).focus();
		TestUtils.for_GETMethod(Globals.baseURL + "rest/user/2").then(function(result){
			var flag=false;
				if(result.email !=null && result.firstName != null && result.lastName !=null)
				{						
					flag=true;
				}					
		        assert.ok(flag,"Object should not Null");
		        done();
		  });	
  	});
		
	QUnit.test( "Test Case For all Active Users", function( assert ) {
		var done = assert.async();
		var input = $( "#test-input" ).focus();
		var flag= false;
		var count_flag=false;
		TestUtils.for_GETMethod(Globals.baseURL + "rest/user?active=true").then(function(result){			
			if (result.length > 0)
			{
				count_flag=true;
			}
			if (count_flag)
			{
				for(var i=0;i<result.length;i++)
				{
					if(result[i].email !=null && result[i].firstName != null && result[i].lastName !=null)
					{						
						flag=true;
					}
					else
					{
						flag = false;
						break;
					}
				}
				assert.ok(flag,"Parameters should not be null");
			}
		    assert.ok(count_flag,"Retrieved All Active Users");
		    done();
		});	
	});
		
	QUnit.test("Test Case For all non-active Users", function( assert ) {
		var done = assert.async();
		var input = $( "#test-input" ).focus();
		var flag= false;
		var count_flag=false;
		TestUtils.for_GETMethod(Globals.baseURL + "rest/user?active=false").then(function(result){
			if (result.length >= 0)
			{
				count_flag=true;
			}
			if (count_flag)
			{
				for(var i=0;i<result.length;i++)
				{
					if(result[i].email !=null && result[i].firstName != null && result[i].lastName !=null)
					{						
						flag=true;
					}
					else
					{
						flag = false;
						break;
					}
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
			//   console.log("Post test cases for new User");
			//   console.log(result);
			//   console.log(JSON.stringify(result));
			//   console.log(JSON.parse(JSON.stringify(result)));
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
		TestUtils.for_POSTMethod(Globals.baseURL + "rest/user",data).then(function(result){
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
			"firstName":"jay",
			"lastName": "ganaguly",
			"password":"saurav1231",
			"email": "saurav@gmail.com",
			"externalid": "s1auravg",
			"id":"1013"
		}
		TestUtils.for_PUTMethod(Globals.baseURL + "rest/user",data).then(function(res){
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");
			assert.equal(res.includes("Successfully"), true, "record must be updated successfully !");
			done();
		});		
	});

	QUnit.test( "Deleting User", function( assert ) {
		var done=assert.async();		
		TestUtils.for_DELETEMethod(Globals.baseURL + "rest/user/"+userid).then(function(res){


			var getResponse = res;
 			var checkSubString = getResponse.indexOf("de-activated successfully");
  			assert.ok((checkSubString > 0),"User Deleted Successfully!");
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");
			//assert.equal(res.includes("de-activated successfully"), true, "User Deleted Successfully!");
			done();
		});
		
	});
	return {
		createNewUser: createNewUser
	  };
})( QUnit.module, QUnit.test );