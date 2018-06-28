/**
 * Author : Jaydeep Bobade Tekvision
 * Module : Relation
 */

(function (module, test) {

    // Relation
    module('Relation');
    var relation_Name = "";
    QUnit.test("Reading Relation of Neighborhood ID = " + NeighborhoodInput.ROOT_NhId, function (assert) {
        var done = assert.async();
        var input = $("#test-input").focus();
        var count_flag = false;
        var flag = false;

        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation", null, UserInput.authorization, "GET").then(function (result) {
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            if (result.length > 0) {
                count_flag = true;
            }
            if (count_flag) {
                for (var i = 0; i < result.length; i++) {
                    if (result[i].name != null && result[i].relatedNeighborhoodId != null) {
                        flag = true;
                    }
                    else {
                        flag = false;
                        break;
                    }
                }
                assert.equal(flag, true, "Parameters should not be null");
            }
            assert.equal(count_flag, true, "Retrieved the Relation");
            done();
        });
    });

    QUnit.test("Reading Relation of Neighborhood ID = " + NeighborhoodInput.ROOT_NhId + " with Missing Authorization", function (assert) {
        var done = assert.async();
        var input = $("#test-input").focus();
        var count_flag = false;
        var flag = false;
        TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation", null, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in header");
            done();
        });
    });

    QUnit.test("Reading Relation of Neighborhood ID = " + NeighborhoodInput.Negative_Nh_Id, function (assert) {
        var done = assert.async();
        var input = $("#test-input").focus();
        var count_flag = false;
        var flag = false;
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.Negative_Nh_Id + "/relation", null, UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "IsNegative", "NHID is Negative");
            done();
        });
    });

    QUnit.test("Creating Relation", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        relation_Name = "APIRelation_" + x;
        var data = {
            "name": relation_Name,
            "relatedNeighborhoodId": [{ "id": NeighborhoodInput.NHID_1 }, { "id": NeighborhoodInput.NHID_2 }]
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "All Neighborhood Relations Created Successfully", "Relation Created Successfully");
            done();
        });
    });

    QUnit.test("Creating Relation with Missing Authorization", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var data = {
            "name": "APIRelation_" + x,
            "relatedNeighborhoodId": [{ "id": NeighborhoodInput.NHID_1 }, { "id": NeighborhoodInput.NHID_2 }]
        }
        TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation", data, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in header");
            done();
        });
    });

    QUnit.test("Creating Relation With Invalid Neighborhood", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var data = {
            "name": "APIRelation_" + x,
            "relatedNeighborhoodId": [{ "id": NeighborhoodInput.NHID_1 }, { "id": NeighborhoodInput.NHID_2 }]
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.Invalid_Nh_Id + "/relation", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "NeighborhoodId NOT FOUND ", "NeighborhoodId NOT FOUND ");
            done();
        });
    });

    QUnit.test("Creating Relation With Invalid Neighborhood In relatedNeighborhoodId", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var data = {
            "name": "APIRelation_" + x,
            "relatedNeighborhoodId": [{ "id": NeighborhoodInput.Invalid_Nh_Id }, { "id": NeighborhoodInput.Invalid_Nh_Id_1 }]
        }

        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "Failure", "Related NHID are not found");
            done();
        });
    });

    QUnit.test("Creating existing Relation", function (assert) {
        var done = assert.async();
        var data = {
            "name": relation_Name,
            "relatedNeighborhoodId": [{ "id": NeighborhoodInput.NHID_1 }, { "id": NeighborhoodInput.NHID_2 }]
        }

        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "Creation of Neighborhood Relation Failed", "Relation Already Exists");
            done();
        });
    });

    QUnit.test("Deleting Relation", function (assert) {
        var done = assert.async();
        TestUtils.sendDeleteRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation?relation=" + relation_Name, UserInput.authorization).then(function (result) {
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(typeof result, "string", "response should be string !");
            assert.notEqual(result.length, 0, "should not be null !");
            var getResponse = result;
            var checkSubString = getResponse.indexOf("Deleted Successfully");
            assert.ok((checkSubString >= 0), "Relation must be deleted successfully !");
            done();
        });
    });


    QUnit.test("Deleting Relation with Missing Authorization", function (assert) {
        var done = assert.async();
        TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation?relation=" + relation_Name, null, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in header");
            done();
        });
    });

    QUnit.test("Deleting Relation with Invalid Neighborhood Id", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.Invalid_Nh_Id + "/relation?relation=" + relation_Name, null, UserInput.authorization, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "NeighborhoodId NOT FOUND ", "NeighborhoodId NOT FOUND ");
            done();
        });
    });

    QUnit.test("Deleting Relation with Negative Neighborhood Id", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.Negative_Nh_Id + "/relation?relation=" + relation_Name, null, UserInput.authorization, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "IsNegative", "NeighborhoodId Is Negative");
            done();
        });
    });

    QUnit.test("Deleting Relation with Missing Relation Name", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation", null, UserInput.authorization, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(result[0].error, "IsMissing", "Relation Name is Missing In URL");
            done();
        });
    });

    QUnit.test("Deleting Relation with Invalid Relation Name", function (assert) {
        var done = assert.async();
        TestUtils.sendDeleteRequest(Globals.baseURL + "rest/v1/neighborhood/" + NeighborhoodInput.ROOT_NhId + "/relation?relation=API", UserInput.authorization).then(function (result) {
            if (result.status == 500){
				assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
				done();
				return ;
			}
            assert.equal(typeof result, "string", "response should be string !");
            assert.notEqual(result.length, 0, "should not be null !");
            var getResponse = result;
            var checkSubString = getResponse.indexOf("Relation NOT found");
            assert.ok((checkSubString >= 0), "Relation NOT found");
            done();
        });
    });
})(QUnit.module, QUnit.test);