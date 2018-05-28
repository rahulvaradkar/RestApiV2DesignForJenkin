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
