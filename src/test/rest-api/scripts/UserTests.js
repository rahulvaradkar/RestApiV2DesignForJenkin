// Test cases fot getting single user with his id
QUnit.test( "Get test cases for single user", function( assert ) {
    var id = 1;
    var done = assert.async();
    $.ajax({url: "http://localhost:8080/BAE/rest/user/"+id,
          type: "GET",
          dataType: "json",
          success: function(result){
          var flag=false;
          if(result.email !=null && result.firstName != null && result.lastName !=null)
          {						
              flag=true;
          }					
          assert.equal(true,flag,"Object should not be null");
          done();
      }
  });
});
// Test cases for getting all the active users
QUnit.test( "Get test cases for all active users", function( assert ) {
    var done = assert.async();
    var flag= false;
    var count_flag=false;
    $.ajax({url: "http://localhost:8080/BAE/rest/user?active=true",
          type: "GET",
          dataType: "json",
          success: function(result){
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
// Test cases fot getting all the non-active users 
QUnit.test( "Get test cases for non-active users", function( assert ) {
  var done = assert.async();
  var flag= false;
    var count_flag=false;
  $.ajax({url: "http://localhost:8080/BAE/rest/user?active=false",
      type: "GET",
      dataType: "json",
      success: function(result){
          if (result.length > 0)
          {
              count_flag=true;
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
// Test cases fot creating new user
  QUnit.test( "Post test cases for new User", function( assert ) {
    var done = assert.async();
    var data = {
        "firstName":"saurav",
        "lastName": "ganguly",
        "password":"saurav123",
        "email": "saurav@gmail.com",
        "externalid": "sauravg",
        "id":"0"
    }
    $.ajax({url: "http://localhost:8080/BAE/rest/user",
          type: "POST",
          dataType: "json",
          data: JSON.stringify(data),
          contentType: "application/json",
          success: function(result){
              console.log(result);
              console.log(JSON.stringify(result));
              console.log(JSON.parse(JSON.stringify(result)));
              var json = result[0];
          assert.notEqual(result.length,0,"object should not be empty !");
          if(Object.keys(json).length == 3)
          assert.equal(Object.keys(json).length, 3,"Error, trying to create existing user or one of property is null !");
          else {
            assert.equal(Object.keys(json).length, 4,"user should have 4 properties !");
            assert.equal(json.email != null, true, "email should not be null");
            assert.equal(json.firstName != null, true, "firstName should not be null");
            assert.equal(json.lastName != null, true, "lastName should not be null");
          }
          done();
      }});
  });
// Test cases fot updating the user
QUnit.test("Put test cases for User", function(assert){
    var data = {
        "firstName":"sachin",
        "lastName": "tendulkar",
        "password":"sachin123",
        "email": "sachin@gmail.com",
        "externalid": "sachin",
        "id":"1007"
    }
    var res = $.ajax({url: "http://localhost:8080/BAE/rest/user",
        type: "PUT",
        dataType: "json",
        data: JSON.stringify(data),
        contentType: "application/json",
        async: false
    }).responseText;
    assert.equal(typeof res, "string", "response should be string !");
    assert.notEqual(res.length, 0, "should not be null !");
    assert.equal(true, res.includes("Successfully"), "record must be updated successfully !");
});
// Test cases fot deleting the user
QUnit.test("Delete test cases for User", function(assert){
    var id = 1007
    var res = $.ajax({url: "http://localhost:8080/BAE/rest/user/"+id,
        type: "DELETE",
        dataType: "json",
        contentType: "application/json",
        async: false
    }).responseText;
    assert.equal(typeof res, "string", "response should be string !");
    assert.notEqual(res.length, 0, "should not be null !");
    assert.equal(res.includes("de-activated successfully"), true, "record must be updated successfully !");
});