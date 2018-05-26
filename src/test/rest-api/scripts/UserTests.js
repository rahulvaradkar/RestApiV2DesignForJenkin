(function( module, test ) {

	module('User');
	var userid=0;

  QUnit.test("Test Case For Single User", function( assert ) {
    var done = assert.async();
    //var input = $( "#test-input" ).focus();
    $.ajax({
      url: Globals.baseURL + "rest/user/1",
		        type: "GET",
		        dataType: "json",
		        success: function(result){
		        console.log("fetching the url : http://localhost:8080/BAE_ReST/rest/user/1");
				console.log("\n");
				var flag=false;
				if(result.email !=null && result.firstName != null && result.lastName !=null)
				{						
					flag=true;
				}					
		        assert.equal(true,flag,"Object is not Null");
		        done();
		    }
		});
	});

	QUnit.test("Test Case For User With id=2", function( assert ) {
		var done = assert.async();
		var input = $( "#test-input" ).focus();
		$.ajax({url: Globals.baseURL + "rest/user/2",
			  type: "GET",
			  dataType: "json",
			  success: function(result){
			  console.log("fetching the url : http://localhost:8080/BAE_ReST/rest/user/2");
			  console.log("\n");
			  var flag=false;
			  if(result.email !=null && result.firstName != null && result.lastName !=null)
			  {						
				  flag=true;
			  }					
			  assert.equal(true,flag,"Object is not Null");
			  done();
		  }
	  });
  });
		
	QUnit.test( "Test Case For all Active Users", function( assert ) {
		  var done = assert.async();
		  var input = $( "#test-input" ).focus();
		  var flag= false;
		  var count_flag=false;
		  $.ajax({url: Globals.baseURL + "rest/user?active=true",
		        type: "GET",
		        dataType: "json",
		        success: function(result){
		        console.log("fetching the url : http://localhost:8080/BAE_ReST/rest/user?active=true");		        
				console.log("\n");
				if (result.length > 0)
				{
					count_flag=true;
				}
				if (result.length > 0)
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
					assert.equal(flag,true,"Parameters should not be null");
				}
		        assert.equal(count_flag,true,"Retrieved the JSON");
		        done();
		    }
		});
	});
		
	QUnit.test("Test Case For all non-active Users", function( assert ) {
		var done = assert.async();
		var input = $( "#test-input" ).focus();
		var flag= false;
		  var count_flag=false;
		$.ajax({url: Globals.baseURL + "rest/user?active=false",
			type: "GET",
		    dataType: "json",
		    success: function(result){
		      	console.log("fetching the url : http://localhost:8080/BAE_ReST/rest/user?active=false");						       
				console.log("\n");
				if (result.length >= 0)
				{
					count_flag=true;
				}
				if (result.length > 0)
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
					assert.equal(flag,true,"Parameters should not be null");
				}				
				assert.equal(count_flag,true,"It Should not return Null");
		        done();
			}
		});
	});
	
	QUnit.test( "Post test cases for new User", function( assert ) {
		var done = assert.async();

		var x = Math.floor((Math.random() * 10000) + 1);
		var data = {
			"firstName":"u",
			"lastName": "u",
			"password":"u",
			"email": "u"+x+"@gmail.com",
			"externalid": "u",
			"id":"0"
		}
		$.ajax({url: Globals.baseURL + "rest/user",
			  type: "POST",
			  dataType: "json",
			  data: JSON.stringify(data),
			  contentType: "application/json",
			  success: function(result){
				  console.log("Post test cases for new User");
				  console.log(result);
				  console.log(JSON.stringify(result));
				  console.log(JSON.parse(JSON.stringify(result)));
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
		  }});
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
		$.ajax({url: Globals.baseURL + "rest/user",
			  type: "POST",
			  dataType: "json",
			  data: JSON.stringify(data),
			  contentType: "application/json",
			  success: function(result){
				  console.log("Post test cases for new User with Blank Name And Password");
				  console.log(result);
				  console.log(JSON.stringify(result));
				  console.log(JSON.parse(JSON.stringify(result)));
				  var json = result[0];
			  assert.notEqual(result.length,0,"object should not be empty !");
			  if(Object.keys(json).length == 3)
			  assert.equal(Object.keys(json).length, 3,"Error, one of property is null !");
			  else {
				assert.equal(Object.keys(json).length, 4,"user should have 4 properties !");
				assert.equal(json.email != null, true, "email should not be null");
				assert.equal(json.firstName != null, true, "firstName should not be null");
				assert.equal(json.lastName != null, true, "lastName should not be null");
			  }
			  done();
		  }});
	  });


	  QUnit.test("Put test cases for User", function(assert){
		var data = {
			"firstName":"jay",
			"lastName": "ganaguly",
			"password":"saurav1231",
			"email": "saura1v@gmail.com",
			"externalid": "s1auravg",
			"id":"1013"
		}
		var res = $.ajax({url: "http://localhost:8080/BAE/rest/user",
			type: "PUT",
			dataType: "json",
			data: JSON.stringify(data),
			contentType: "application/json",
			async: false
		}).responseText;
		console.log("------------------------------- :"+res);
		assert.equal(typeof res, "string", "response should be string !");
		assert.notEqual(res.length, 0, "should not be null !");
		assert.equal(res.includes("Successfully"), true, "record must be updated successfully !");
	});

	QUnit.test( "Deleting User Id = 1018", function( assert ) {
		var id = 1048;
		console.log("******************************************************************http://localhost:8080/BAE_ReST/rest/user/"+userid);
		var res = $.ajax({url: Globals.baseURL + "rest/user/"+userid,
			type: "DELETE",
			dataType: "json",
			contentType: "application/json",
			async: false
		}).responseText;
		assert.equal(typeof res, "string", "response should be string !");
		assert.notEqual(res.length, 0, "should not be null !");
		assert.equal(res.includes("de-activated successfully"), true, "User Deleted Successfully!");
	});
	
	

	
})( QUnit.module, QUnit.test );