/**
 * Author : Jaydeep Bobade Tekvision
 * Module : NeighbourhoodTest
 */

(function (module, test) {

	module('Neighbourhood');
	//Neighbourhood
	var level_0 = 0;
	var level_1 = 0;
	var level_2 = 0;
	var level_3 = 0
	QUnit.test("Reading Neighborhood with ID = " + NeighborhoodInput.NHID_1, function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/1", null, UserInput.authorization, "GET").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			var flag = false;
			if (result.length > 0) {
				count_flag = true;
			}
			if (result.length > 0) {
				for (var i = 0; i < result.length; i++) {
					if (result[i].id != null && result[i].level != null && result[i].name != null && result[i].parentId != null && result[i].secure != null) {
						flag = true;
					}
					else {
						flag = false;
						break;
					}
				}
				assert.equal(flag, true, "Parameters should not be null");
			}
			assert.equal(count_flag, true, "Retrieved the Neighborhood of ID=1");
			done();
		});
	});

	QUnit.test("Posting new Neighborhood at level 0", function (assert) {
		var done = assert.async();
		var data = {
			"level": "-1",
			"name": "level_0",
			"id": "0",
			"parentId": "-1",
			"secure": "false"
		}
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood", data, UserInput.authorization, "POST").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			level_0 = result[0].id;
			assert.notEqual(result.length, 0, "object should not be empty !");
			assert.ok(result[0].id > 0, "Neighborhood ID shoul greater than 0");
			assert.equal(Object.keys(result[0]).length, 5, "neighborhood should have 5 properties !");
			done();
		});
	});

	QUnit.test("Posting new Neighborhood at level 1", function (assert) {
		var done = assert.async();
		var data = {
			"level": "2",
			"name": "level_1",
			"id": "0",
			"parentId": level_0,
			"secure": "true"
		}
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood", data, UserInput.authorization, "POST").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			level_1 = result[0].id;
			assert.notEqual(result.length, 0, "object should not be empty !");
			assert.ok(result[0].id > 0, "Neighborhood ID shoul greater than 0");
			assert.equal(Object.keys(result[0]).length, 5, "neighborhood should have 5 properties !");
			done();
		});
	});

	/******** */
	QUnit.test("Posting new Neighborhood at level 2", function (assert) {
		var done = assert.async();
		var data = {
			"level": "2",
			"name": "level_2",
			"id": "0",
			"parentId": level_1,
			"secure": "true"
		}
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood", data, UserInput.authorization, "POST").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			level_2 = result[0].id;
			assert.notEqual(result.length, 0, "object should not be empty !");
			assert.ok(result[0].id > 0, "Neighborhood ID shoul greater than 0");
			assert.equal(Object.keys(result[0]).length, 5, "neighborhood should have 5 properties !");
			done();
		});
	});

	QUnit.test("Posting new Neighborhood at level 3", function (assert) {
		var done = assert.async();
		var data = {
			"level": "2",
			"name": "level_3",
			"id": "0",
			"parentId": level_2,
			"secure": "true"
		}
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood", data, UserInput.authorization, "POST").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			level_3 = result[0].id;
			assert.notEqual(result.length, 0, "object should not be empty !");
			assert.ok(result[0].id > 0, "Neighborhood ID shoul greater than 0");
			assert.equal(Object.keys(result[0]).length, 5, "neighborhood should have 5 properties !");
			done();
		});
	});

	QUnit.test("Delete test case for neighborhood at level 3", function (assert) {
		var done = assert.async();
		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/v1/neighborhood/" + level_3, UserInput.authorization).then(function (res) {
			assert.ok(res != null, "Response should not be null");
			if (res.status == 500) {
				assert.ok(res.status == 500, " It seems server side error" + res.responseText);
				done();
				return;
			}
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");
			var getResponse = res;
			var checkSubString = getResponse.indexOf("Neighborhood Deleted Successfully");
			assert.ok((checkSubString >= 0), "record must be updated successfully !");
			//assert.equal(res.includes("Neighborhood Deleted Successfully"), true, "record must be updated successfully !");
			done();
		});
	});

	QUnit.test("Delete test case for neighborhood at level 2", function (assert) {
		var done = assert.async();
		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/v1/neighborhood/" + level_2, UserInput.authorization).then(function (res) {
			assert.ok(res != null, "Response should not be null");
			if (res.status == 500) {
				assert.ok(res.status == 500, " It seems server side error" + res.responseText);
				done();
				return;
			}
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");
			var getResponse = res;
			var checkSubString = getResponse.indexOf("Neighborhood Deleted Successfully");
			assert.ok((checkSubString >= 0), "record must be updated successfully !");
			//assert.equal(res.includes("Neighborhood Deleted Successfully"), true, "record must be updated successfully !");
			done();
		});
	});
	/******* */

	QUnit.test("Delete test case for neighborhood at level 1", function (assert) {
		var done = assert.async();
		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/v1/neighborhood/" + level_1, UserInput.authorization).then(function (res) {
			assert.ok(res != null, "Response should not be null");
			if (res.status == 500) {
				assert.ok(res.status == 500, " It seems server side error" + res.responseText);
				done();
				return;
			}
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");
			var getResponse = res;
			var checkSubString = getResponse.indexOf("Neighborhood Deleted Successfully");
			assert.ok((checkSubString >= 0), "record must be updated successfully !");
			//assert.equal(res.includes("Neighborhood Deleted Successfully"), true, "record must be updated successfully !");
			done();
		});
	});

	QUnit.test("Delete test case for neighborhood at level 0", function (assert) {
		var done = assert.async();
		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/v1/neighborhood/" + level_0, UserInput.authorization).then(function (res) {
			assert.ok(res != null, "Response should not be null");
			if (res.status == 500) {
				assert.ok(res.status == 500, " It seems server side error" + res.responseText);
				done();
				return;
			}
			assert.equal(typeof res, "string", "response should be string !");
			assert.notEqual(res.length, 0, "should not be null !");
			assert.equal(res.includes("Neighborhood Deleted Successfully"), true, "record must be updated successfully !");
			done();
		});
	});


	QUnit.test("Posting Duplicate Neighborhood at level 0", function (assert) {
		var done = assert.async();
		var data = {
			"level": "-1",
			"name": NeighborhoodInput.Existing_NhName,
			"id": "0",
			"parentId": "-1",
			"secure": "false"
		}
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood", data, UserInput.authorization, "POST").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			level_0 = result[0].id;
			assert.notEqual(result.length, 0, "object should not be empty !");
			assert.equal(result[0].error, "Failed to Create Neighbornood:" + NeighborhoodInput.Existing_NhName + ",  Error Msg:null, Cause:null", "Trying to create duplicate neighborhood at level 0");
			assert.equal(Object.keys(result[0]).length, 3, "neighborhood should have 3 properties !");
			done();
		});
	});

	QUnit.test("Posting new Neighborhood at level 1 with Invalid Parent id", function (assert) {
		var done = assert.async();
		var data = {
			"level": "-1",
			"name": "LEVEL_11",
			"id": "0",
			"parentId": NeighborhoodInput.InvalidParent_Id,
			"secure": "false"
		}
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood", data, UserInput.authorization, "POST").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			level_0 = result[0].id;
			assert.notEqual(result.length, 0, "object should not be empty !");
			assert.equal(result[0].error, "Failed to Create Neighbornood: Parent NeighborhoodId NOT FOUND ", "Failed to Create Neighbornood: Parent NeighborhoodId NOT FOUND ");
			assert.equal(Object.keys(result[0]).length, 3, "neighborhood should have 3 properties !");
			done();
		});
	});

	QUnit.test("Posting new Neighborhood at level 0 with blank name", function (assert) {
		var done = assert.async();
		var data = {
			"level": "-1",
			"name": "",
			"id": "0",
			"parentId": "-1",
			"secure": "false"
		}
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood", data, UserInput.authorization, "POST").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			level_0 = result[0].id;
			assert.notEqual(result.length, 0, "object should not be empty !");
			assert.equal(result[0].error, "IsBlank", "Name is Blank");
			assert.equal(Object.keys(result[0]).length, 3, "neighborhood should have 3 properties !");
			done();
		});
	});

	QUnit.test("Reading Neighborhood with InValid Id", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.Invalid_Nh_Id, null, UserInput.authorization, "GET").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			assert.equal(result[0].error, "NeighborhoodId NOT FOUND ", "Invalid  or Not Found neighborhood ID");
			done();
		});
	});

	QUnit.test("Missing Authorization while Reading Neighborhood", function (assert) {
		var done = assert.async();
		TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.NHID_1, null, "GET").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in Header");
			done();
		});
	});

	QUnit.test("Delete test case for non existing neighborhood ID", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.Invalid_Nh_Id, null, UserInput.authorization, "DELETE").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			assert.equal(result[0].error, "NeighborhoodId NOT FOUND ", "NeighborhoodId NOT FOUND");
			done();
		});
	});

	QUnit.test("Delete test case for negative neighborhood ID", function (assert) {
		var done = assert.async();
		TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.Negative_Nh_Id, null, UserInput.authorization, "DELETE").then(function (result) {
			assert.ok(result != null, "Response should not be null");
			if (result.status == 500) {
				assert.ok(result.status == 500, " It seems server side error" + result.responseText);
				done();
				return;
			}
			assert.equal(result[0].error, "IsNegative", "NeighborhoodId is negative");
			done();
		});
	});

})(QUnit.module, QUnit.test);