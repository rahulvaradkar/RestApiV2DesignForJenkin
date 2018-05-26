//Testcases for Member ReST API
(function(module, test) {

//Data needed for tests in this module
var validMemberAttributes = ["id", "nhid", "userId"];
var rootNhId = 1;


module('Members');
//GET members in Neighborhood
  test("Get members of neighborhood", function(assert) {
    var done = assert.async();
    $.ajax({
      url: Globals.baseURL + "rest/neighborhood/" + rootNhId + "/member",
      type: "GET",
      dataType: "json",
      success: function(data) {

        assert.equal(isNullObject(data), false, "Member data shouldn't be null");
        assert.equal(hasOneOrMoreElements(data), true, "1 or more members exist for neighborhood with members");
        for(var i = 0; i < data.length; i++) {
          assert.equal(hasValidProperties(data[i], validMemberAttributes), true, "Member object should have id, nhid and userId properties");
          assert.equal(hasNullAttributes(data[i], validMemberAttributes), false, "Member object should not have null attributes.");
        }
        done();

      }
    });
  });
})( QUnit.module, QUnit.test );