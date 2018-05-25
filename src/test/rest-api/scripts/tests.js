//test to ensure right URL is being accessed
  QUnit.test("Checking for valid URL", function(assert) {
    var done = assert.async();
    var requestSuccessful = false;
    $.ajax({url: Globals.baseURL + "rest/user/1",
      type: "GET",
      dataType: "json",
      contentType: "application/json",
      success: function(result){
        requestSuccessful = true;
      },
      complete: function() {
        assert.equal(requestSuccessful, true, "Should have valid baseURL in globals.js.");
        done();
      }
    });
  });
  QUnit.test( "Posting new Neighborhood", function( assert ) {
    var done = assert.async();
    var data = {
        "level":"-1",
        "name": "apiTest2",
        "id":"0",
        "parentId": "-1",
        "secure": "false"
    }
    $.ajax({url: Globals.baseURL + "rest/neighborhood",
          type: "POST",
          dataType: "json",
          data: JSON.stringify(data),
          contentType: "application/json",
          success: function(result){
              console.log(result);
              console.log(JSON.stringify(result));
              console.log(JSON.parse(JSON.stringify(result)));
          assert.notEqual(result.length,0,"object should not be empty !");
          assert.equal(Object.keys(result[0]).length, 5,"neighborhood should have 5 properties !");
          done();
      }});
  });
  QUnit.test( "Posting new User", function( assert ) {
    var done = assert.async();
    var data = {
        "firstName":"sachin",
        "lastName": "tendulkar",
        "password":"sachin123",
        "email": "sachin@gmail.com",
        "externalid": "sachin",
        "id":"0"
    }
    $.ajax({url: Globals.baseURL + "rest/user",
          type: "POST",
          dataType: "json",
          data: JSON.stringify(data),
          contentType: "application/json",
          success: function(result){
              console.log(result);
              console.log(JSON.stringify(result));
              console.log(JSON.parse(JSON.stringify(result)));
          assert.notEqual(result.length,0,"object should not be empty !");
          assert.equal(Object.keys(result[0]).length, 5,"neighborhood should have 5 properties !");
          done();
      }});
  });