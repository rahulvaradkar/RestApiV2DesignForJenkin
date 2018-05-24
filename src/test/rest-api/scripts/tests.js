QUnit.test( "hello test", function( assert ) {
    assert.ok( 1 == "1", "Passed!" );
  });
  QUnit.test( "Getting users", function( assert ) {
    var done = assert.async();
    var input = $( "#test-input" ).focus();
    $.ajax({url: "http://localhost:8080/BAE/rest/user?active=true",
          type: "GET",
          dataType: "json",
          success: function(result){
              console.log(result);
              console.log(JSON.stringify(result));
              console.log(JSON.parse(JSON.stringify(result)));
          assert.equal(result.length,6,"Retrieved the JSON");
          done();
      }});
  });
  QUnit.test( "Getting neighborhood", function( assert ) {
    var done = assert.async();
    var data = {
        "level":"-1",
	"name": "apiTest2",
	"id":"0",
	"parentId": "-1",
	"secure": "false"
    }
    $.ajax({url: "http://localhost:8080/BAE/rest/neighborhood",
          type: "POST",
          dataType: "json",
          data: data,
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