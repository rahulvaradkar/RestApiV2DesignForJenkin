//Testcases for Member ReST API
(function (module, test) {

	//Data needed for tests in this module
	var validMemberAttributes = ["id", "nhid", "userId"];
	var rootNhId = NeighborhoodInput.ROOT_NhId;
	var emptyNhId = NeighborhoodInput.NHID_3;
	var errorAttributes = ["error", "path", "proposedSolution"];
	var zeroNhId = 0;
	var negativeNhId = NeighborhoodInput.Negative_Nh_Id;
	var nonExistingNhId = NeighborhoodInput.Invalid_Nh_Id;
	var validAttributesForCreatedMember = ["active", "id", "nhid", "userId"];
	var memberId = 0;
	var user;
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
	test("Get members of neighborhood", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + 2 + "/member", null, UserInput.authorization, "GET").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
            }
			assert.notOk(isNullObject(data), "Member data shouldn't be null");
			assert.ok(hasOneOrMoreElements(data), "1 or more members exist for neighborhood with members");
			for (var i = 0; i < data.length; i++) {
				var member = data[i];
				assert.ok(hasValidProperties(member, validMemberAttributes), "Member object should have id, nhid and userId properties");
				assert.notOk(hasNullAttributes(member, validMemberAttributes), "Member object should not have null attributes.");
				assert.ok(isValidNumber(member.id), "Member id should be greater than 0.");
				assert.ok(isValidNumber(member.nhid), "Member's nhid should be greater than 0.");
				assert.ok(isValidNumber(member.userId), "Member's userId should be greater than 0.");
			}
			done();
		});
	});

	test("Get members of neighborhood with Missing Authorization", function (assert) {
		var done = assert.async();
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member", null, "GET").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + res.responseText);
                done();
                return;
            }
			assert.notOk(isNullObject(data), "Member data shouldn't be null");
			assert.equal(data[0].error, "Missing Authorization in Header", "Missing Authorization in Header");
			done();
		});
	});

	//Get empty data when no members in neighborhood
	test("Get empty data for neighborhood without any members", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + emptyNhId + "/member", null, UserInput.authorization, "GET").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
            }
			var isEmptyData = true;
			if (!data) {
				isEmptyData = false;
			}
			assert.ok(isEmptyData, "No members exist for neighborhood");
			done();
		});
	});

	//Get members from neighborhood with id 0
	test("Get members from nhid 0 neighborhood", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + zeroNhId + "/member", null, UserInput.authorization, "GET").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
            }
			assert.notOk(isNullObject(), "Response shouldn't be null when nhid is 0");
			assert.ok(hasOneOrMoreElements(data), "When nhid is 0, response must have some data ready to be parsed.");
			var exception = data[0];
			assert.ok(hasValidProperties(exception, errorAttributes), "Exception object should have valid 3 attributes - error, path and proposedSolution");
			assert.notOk(hasNullAttributes(exception, errorAttributes), "Exception object shouldn't have null attribute values for its 3 attributes");
			assert.equal(exception.error, "IsNegative", "0 or negative nhId results in error message \"IsNegative\"");
			assert.equal(exception.path, "nhId", "Should specify which part of API path caused exception to occur. In this case, nhId.");
			assert.equal(exception.proposedSolution, "You must enter an Existing Neighborhood ID. It should be a Positive Number.", "Should provide appropriate solution.");
			done();
		});
	});

	//Get members from neighborhood with id -1
	test("Get members from nhid -1 neighborhood", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + negativeNhId + "/member", null, UserInput.authorization, "GET").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
            }
			assert.notOk(isNullObject(), "Response shouldn't be null when nhid is -1");
			assert.ok(hasOneOrMoreElements(data), "When nhid is -1, response must have some data ready to be parsed.");
			var exception = data[0];
			assert.ok(hasValidProperties(exception, errorAttributes), "Exception object should have valid 3 attributes - error, path and proposedSolution");
			assert.notOk(hasNullAttributes(exception, errorAttributes), "Exception object shouldn't have null attribute values for its 3 attributes");
			assert.equal(exception.error, "IsNegative", "0 or negative nhId results in error message \"IsNegative\"");
			assert.equal(exception.path, "nhId", "Should specify which part of API path caused exception to occur. In this case, nhId.");
			assert.equal(exception.proposedSolution, "You must enter an Existing Neighborhood ID. It should be a Positive Number.", "Should provide appropriate solution.");
			done();
		});
	});

	//Get members from neighborhood which has nhId that doesn't exist in database
	test("Get members from non-existing neighborhood", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + nonExistingNhId + "/member", null, UserInput.authorization, "GET").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
            }
			assert.notOk(isNullObject(), "Response shouldn't be null when neighborhood doesn't exist");
			assert.ok(hasOneOrMoreElements(data), "When neighborhood doesn't exist, response must have some data ready to be parsed.");
			var exception = data[0];
			assert.ok(hasValidProperties(exception, errorAttributes), "Exception object should have valid 3 attributes - error, path and proposedSolution");
			assert.notOk(hasNullAttributes(exception, errorAttributes), "Exception object shouldn't have null attribute values for its 3 attributes");
			undecidedAssertEqual(assert, exception.error, "NeighborhoodId NOT FOUND ", "NeighborhoodId NOT FOUND ", "Non-existing nhId results in error message indicating not found");
			undecidedAssertEqual(assert, exception.path, "NeighborhoodManagement.neighborhoodNhIdMemberGet::BoardwalkNeighborhoodManager.getNeighborhoodTree", "NeighborhoodManagement.neighborhoodNhIdMemberGet::BoardwalkNeighborhoodManager.getNeighborhoodTree", "Should specify which part of API path caused exception to occur. In this case, nhId.");
			assert.equal(exception.proposedSolution, "The NeighborhoodId NOT FOUND. You must provide an existing Neigborhood Id.", "Should provide appropriate solution.");
			done();
		});
	});

	//Create member test
	test("Create new membership for user", function (assert) {
		var done = assert.async();
		UserTests.createNewUser().then(function (result) {
			user = result[0];
			var membershipData = {
				userId: user.id,
				nhid: rootNhId,
				id: 0
			};
			TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member", membershipData, UserInput.authorization, "POST").then(function (data) {
				assert.ok(data != null, "Response should not be null");
				if (data.status == 500) {
					assert.ok(data.status == 500, " It seems server side error" + data.responseText);
					done();
					return;
				}
				assert.notOk(isNullObject(data), "Member data shouldn't be null");
				assert.ok(hasOneOrMoreElements(data), "Response has some data to parse on sending POST request to create membership.");
				var membership = data[0];
				memberId = data[0].id;
				assertValidMembershipCreatedResponse(assert, membership);
				done();
			});
		});
	});

	test("Create new membership for user with Missing Authorization", function (assert) {
		var done = assert.async();		
		var membershipData = {
			userId: user.id,
			nhid: rootNhId,
			id: 0
		};
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member", membershipData, "POST").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
            }
			assert.ok(data != null, "Response should not be null");
			assert.equal(data[0].error, "Missing Authorization in Header", "Missing Authorization in Header");
			done();
			//	});	
		});
	});

	test("Create new membership for user with Invalid User", function (assert) {
		var done = assert.async();
		var membershipData = {
			userId: 99999,
			nhid: rootNhId,
			id: 0
		};
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member", membershipData, UserInput.authorization, "POST").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
            }
			assert.ok(data != null, "Response should not be null");
			assert.equal(data[0].error, "The User NOT FOUND", "The User NOT FOUND");
			done();
		});
	});

	test("Create new membership for existing user", function (assert) {
		var done = assert.async();
		var membershipData = {
			userId: UserInput.User_Id,
			nhid: NeighborhoodInput.NHID_1,
			id: 0
		};
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.NHID_1 + "/member", membershipData, UserInput.authorization, "POST").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
			}
			assert.ok(data != null, "Response should not be null");
			assert.equal(data[0].error, "Creating new membership to Neighborhood Failed", "Membership already exist");
			done();
		});
	});

	test("Create new membership for user with Invalid Neighborhood", function (assert) {
		var done = assert.async();
		var membershipData = {
			userId: user.id,
			nhid: 99999,
			id: 0
		};
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/99999/member", membershipData, UserInput.authorization, "POST").then(function (data) {
			assert.ok(data != null, "Response should not be null");
            if (data.status == 500) {
                assert.ok(data.status == 500, " It seems server side error" + data.responseText);
                done();
                return;
            }
			assert.ok(data != null, "Response should not be null");
			assert.equal(data[0].error, "NeighborhoodId NOT FOUND ", "NeighborhoodId NOT FOUND ");
			done();
		});
	});

	//Create membership by passing mismatching nhid
	test("New membership for user should ignore nhid when mismatching nhid is sent in POST data", function (assert) {
		var done = assert.async();
		UserTests.createNewUser().then(function (result) {
			var user = result[0];
			var membershipData = {
				userId: user.id,
				nhid: emptyNhId,
				id: 0
			};
			TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member", membershipData, UserInput.authorization, "POST").then(function (data) {
				assert.ok(data != null, "Response should not be null");
            	if (data.status == 500) {
             		assert.ok(data.status == 500, " It seems server side error" + data.responseText);
           		    done();
                	return;
           		}
				assert.notOk(isNullObject(data), "Member data shouldn't be null");
				assert.ok(hasOneOrMoreElements(data), "Response has some data to parse on sending POST request to create membership.");
				var membership = data[0];
				assertValidMembershipCreatedResponse(assert, membership);
				assert.notEqual(membership.nhid, membershipData.nhid, "Nhid in request data doesn't match nhid in response and path param but still membership is created ignoring the nhid in request.");
				done();
			});
		});
	});

	test("Deleting Member", function (assert) {
		var done = assert.async();
		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member/" + memberId, UserInput.authorization).then(function (res) {
			assert.ok(res != null, "Response should not be null");
            if (res.status == 500) {
                assert.ok(res.status == 500, " It seems server side error" + res.responseText);
                done();
                return;
            }
			assert.ok(res != null, "Response should not be null !");
			var getResponse = res;
			var checkSubString = getResponse.indexOf("Successfully Deleted.");
			assert.ok((checkSubString >= 0), "User Deleted Successfully!");
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");
			done();
		});
	});

	test("Deleting Member with negative member ID", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member/-" + memberId, null, UserInput.authorization, "DELETE").then(function (res) {
			assert.ok(res != null, "Response should not be null");
            if (res.status == 500) {
                assert.ok(res.status == 500, " It seems server side error" + res.responseText);
                done();
                return;
            }
			assert.ok(res != null, "Response should not be null !");
			assert.equal(res[0].error, "IsNegative", "Negative Member ID");
			done();
		});
	});

	test("Deleting Member with invalid NHID", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + nonExistingNhId + "/member/" + memberId, null, UserInput.authorization, "DELETE").then(function (res) {
			assert.ok(res != null, "Response should not be null");
            if (res.status == 500) {
                assert.ok(res.status == 500, " It seems server side error" + res.responseText);
                done();
                return;
            }
			assert.ok(res != null, "Response should not be null !");
			assert.equal(res[0].error, "NeighborhoodId NOT FOUND ", "NeighborhoodId NOT FOUND ");
			done();
		});
	});

	test("Deleting Member with invalid MembershipId", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member/" + UserInput.Invalid_MemberId, null, UserInput.authorization, "DELETE").then(function (res) {
			assert.ok(res != null, "Response should not be null");
            if (res.status == 500) {
                assert.ok(res.status == 500, " It seems server side error" + res.responseText);
                done();
                return;
            }
			assert.ok(res != null, "Response should not be null !");
			assert.equal(res[0].error, "The Membership NOT FOUND", "The Membership NOT FOUND");
			done();
		});
	});

	test("Deleting Member with Missing Authorization", function (assert) {
		var done = assert.async();
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/neighborhood/" + rootNhId + "/member/" + memberId, null, "DELETE").then(function (res) {
			assert.ok(res != null, "Response should not be null");
            if (res.status == 500) {
                assert.ok(res.status == 500, " It seems server side error" + res.responseText);
                done();
                return;
            }
			assert.ok(res != null, "Response should not be null")
			assert.equal(res[0].error, "Missing Authorization in Header", "Missing Authorization in Header")
			done();
		});
	});

})(QUnit.module, QUnit.test);