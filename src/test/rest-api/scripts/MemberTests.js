//Testcases for Member ReST API
(function(module, test) {

//Data needed for tests in this module
var validMemberAttributes = ["id", "nhid", "userId"];
var rootNhId = 1;
var emptyNhId = 2;
var errorAttributes = ["error", "path", "proposedSolution"];
var zeroNhId = 0;
var negativeNhId = -1;
var nonExistingNhId = 30000;
var validAttributesForCreatedMember = ["active", "id", "nhid", "userId"];


module('Member');

//reusable methods

//Assert membership object received as response when membership is newly created.
  function assertValidMembershipCreatedResponse(assert, membership) {
    assert.ok(hasValidProperties(membership, validAttributesForCreatedMember), "On successful creation of membership, membership object should have active, id, nhid and userId properties");
    assert.notOk(hasNullAttributes(membership, validAttributesForCreatedMember), "On successful creation of membership, membership object should not have null attributes.");
    assert.ok(isValidNumber(membership.id), "Member id should be greater than 0.");
    assert.ok(isValidNumber(membership.nhid), "Member's nhid should be greater than 0.");
    assert.ok(isValidNumber(membership.userId), "Member's userId should be greater than 0.");
    assert.ok(membership.active, "Membership is active for newly created membership.");
  }

//GET members in Neighborhood
  test("Get members of neighborhood", function(assert) {
    var done = assert.async();
    $.ajax({
      url: Globals.baseURL + "rest/neighborhood/" + rootNhId + "/member",
      type: "GET",
      dataType: "json",
      success: function(data) {
        assert.notOk(isNullObject(data), "Member data shouldn't be null");
        assert.ok(hasOneOrMoreElements(data), "1 or more members exist for neighborhood with members");
        for(var i = 0; i < data.length; i++) {
          var member = data[i];
          assert.ok(hasValidProperties(member, validMemberAttributes), "Member object should have id, nhid and userId properties");
          assert.notOk(hasNullAttributes(member, validMemberAttributes), "Member object should not have null attributes.");
          assert.ok(isValidNumber(member.id), "Member id should be greater than 0.");
          assert.ok(isValidNumber(member.nhid), "Member's nhid should be greater than 0.");
          assert.ok(isValidNumber(member.userId), "Member's userId should be greater than 0.");
        }
        done();
      }
    });
  });

//Get empty data when no members in neighborhood
  test("Get empty data for neighborhood without any members", function(assert) {
    var done = assert.async();
    $.ajax({
      url: Globals.baseURL + "rest/neighborhood/" + emptyNhId + "/member",
      type: "GET",
      dataType: "json",
      success: function(data) {
        var isEmptyData = true;
        if(!data) {
          isEmptyData = false;
        }
        assert.ok(isEmptyData, "No members exist for neighborhood");
        done();
      }
    });
  });

//Get members from neighborhood with id 0
  test("Get members from nhid 0 neighborhood", function(assert) {
    var done = assert.async();
    $.ajax({
      url: Globals.baseURL + "rest/neighborhood/" + zeroNhId + "/member",
      type: "GET",
      dataType: "json",
      success: function(data) {
        assert.notOk(isNullObject(), "Response shouldn't be null when nhid is 0");
        assert.ok(hasOneOrMoreElements(data), "When nhid is 0, response must have some data ready to be parsed.");
        var exception = data[0];
        assert.ok(hasValidProperties(exception, errorAttributes), "Exception object should have valid 3 attributes - error, path and proposedSolution");
        assert.notOk(hasNullAttributes(exception, errorAttributes), "Exception object shouldn't have null attribute values for its 3 attributes");
        assert.equal(exception.error, "IsNegative", "0 or negative nhId results in error message \"IsNegative\"");
        assert.equal(exception.path, "nhId", "Should specify which part of API path caused exception to occur. In this case, nhId.");
        assert.equal(exception.proposedSolution, "You must enter an Existing Neighborhood ID. It should be a Positive Number.", "Should provide appropriate solution.");
        done();
      }
    });
  });

//Get members from neighborhood with id -1
  test("Get members from nhid -1 neighborhood", function(assert) {
    var done = assert.async();
    $.ajax({
      url: Globals.baseURL + "rest/neighborhood/" + negativeNhId + "/member",
      type: "GET",
      dataType: "json",
      success: function(data) {
        assert.notOk(isNullObject(), "Response shouldn't be null when nhid is -1");
        assert.ok(hasOneOrMoreElements(data), "When nhid is -1, response must have some data ready to be parsed.");
        var exception = data[0];
        assert.ok(hasValidProperties(exception, errorAttributes), "Exception object should have valid 3 attributes - error, path and proposedSolution");
        assert.notOk(hasNullAttributes(exception, errorAttributes), "Exception object shouldn't have null attribute values for its 3 attributes");
        assert.equal(exception.error, "IsNegative", "0 or negative nhId results in error message \"IsNegative\"");
        assert.equal(exception.path, "nhId", "Should specify which part of API path caused exception to occur. In this case, nhId.");
        assert.equal(exception.proposedSolution, "You must enter an Existing Neighborhood ID. It should be a Positive Number.", "Should provide appropriate solution.");
        done();
      }
    });
  });

//Get members from neighborhood which has nhId that doesn't exist in database
  test("Get members from non-existing neighborhood", function(assert) {
    var done = assert.async();
    $.ajax({
      url: Globals.baseURL + "rest/neighborhood/" + nonExistingNhId + "/member",
      type: "GET",
      dataType: "json",
      success: function(data) {
        assert.notOk(isNullObject(), "Response shouldn't be null when neighborhood doesn't exist");
        assert.ok(hasOneOrMoreElements(data), "When neighborhood doesn't exist, response must have some data ready to be parsed.");
        var exception = data[0];
        assert.ok(hasValidProperties(exception, errorAttributes), "Exception object should have valid 3 attributes - error, path and proposedSolution");
        assert.notOk(hasNullAttributes(exception, errorAttributes), "Exception object shouldn't have null attribute values for its 3 attributes");
        undecidedAssertEqual(assert, exception.error, "NeighborhoodId NOT FOUND ", "NotFound", "Non-existing nhId results in error message indicating not found");
        undecidedAssertEqual(assert, exception.path, "NeighborhoodManagement.neighborhoodNhIdMemberGet::BoardwalkNeighborhoodManager.getNeighborhoodTree", "nhId", "Should specify which part of API path caused exception to occur. In this case, nhId.");
        assert.equal(exception.proposedSolution, "The NeighborhoodId NOT FOUND. You must provide an existing Neighborhood Id.", "Should provide appropriate solution.");
        done();
      }
    });
  });

//Create member test
  test("Create new membership for user", function(assert) {
    var done = assert.async();
    UserTests.createNewUser().then(function(result) {
      var user = result[0];
      var membershipData = {
        userId: user.id,
        nhid: rootNhId,
        id: 0
      };
      $.ajax({
        url: Globals.baseURL + "rest/neighborhood/" + rootNhId + "/member",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(membershipData),
        contentType: "application/json",
        success: function(data) {
          assert.notOk(isNullObject(data), "Member data shouldn't be null");
          assert.ok(hasOneOrMoreElements(data), "Response has some data to parse on sending POST request to create membership.");
          var membership = data[0];
          assertValidMembershipCreatedResponse(assert, membership);
          done();
        }
      });
    });
  });

//Create membership by passing mismatching nhid
  test("New membership for user should ignore nhid when mismatching nhid is sent in POST data", function(assert) {
    var done = assert.async();
    UserTests.createNewUser().then(function(result) {
      var user = result[0];
      var membershipData = {
        userId: user.id,
        nhid: emptyNhId,
        id: 0
      };
      $.ajax({
        url: Globals.baseURL + "rest/neighborhood/" + rootNhId + "/member",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(membershipData),
        contentType: "application/json",
        success: function(data) {
          assert.notOk(isNullObject(data), "Member data shouldn't be null");
          assert.ok(hasOneOrMoreElements(data), "Response has some data to parse on sending POST request to create membership.");
          var membership = data[0];
          assertValidMembershipCreatedResponse(assert, membership);
          assert.notEqual(membership.nhid, membershipData.nhid, "Nhid in request data doesn't match nhid in response and path param but still membership is created ignoring the nhid in request.");
          done();
        }
      });
    });
  });
})( QUnit.module, QUnit.test );