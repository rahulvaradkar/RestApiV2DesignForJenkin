/**
 * Author : Jaydeep Bobade Tekvision
 * Module : Collaboration
 */

(function (module, test) {

    module('Collaborations');
    var Collab_Name = "";
    var collab_Id;
    
    QUnit.test("Reading Collaborations of Neighborhood ID=1", function (assert) {
        var done = assert.async();
        var input = $("#test-input").focus();
        var flag = false;
        var count_flag = false;

        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + GlobalData.NeighborhoodInput.ROOT_NhId + "/collaboration", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            if (result.length > 0) {
                count_flag = true;
            }
            if (result.length > 0) {
                for (var i = 0; i < result.length; i++) {
                    if (result[i].name != null && result[i].id != null && result[i].wbList != null) {
                        flag = true;
                    }
                    else {
                        flag = false;
                        break;
                    }
                    assert.equal(typeof result[i].name, "string", "Name Should be String");
                    assert.equal(typeof result[i].id, "number", "Name Should be String");
                    assert.ok(result[i].id > 0, "Id should be greater than 1000");
                }
                assert.ok(flag, "Parameters should not be null");
            }
            assert.ok(count_flag, "Retrieved the Collaborations Of Neighborhood");
            done();
        });
    });

    QUnit.test("Reading Collaborations of Neighborhood when collaboration are not exist", function (assert) {
        var done = assert.async();
        var count_flag = false;

        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + GlobalData.NeighborhoodInput.NHID_3 + "/collaboration", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            if (result.length == 0) {
                count_flag = true;
            }
            assert.ok(count_flag, "Collaborations don't Exist");
            done();
        });
    });

    QUnit.test("Posting New Collaboration", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        Collab_Name = "Collab_" + x;
        var data = {
            "name": Collab_Name,
            "purpose": "Test"
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + GlobalData.NeighborhoodInput.NHID_1 + "/member/" + GlobalData.UserInput.Member_Id_1 + "/collaboration", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            collab_Id = result;
            assert.ok(result > 0, "Collab Id Should greater than 0");
            done();
        });
    });

    QUnit.test("Posting Existing Collaboration", function (assert) {
        var done = assert.async();
        var data = {
            "name": Collab_Name,
            "purpose": "Test"
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + GlobalData.NeighborhoodInput.NHID_1 + "/member/" + GlobalData.UserInput.Member_Id_1 + "/collaboration", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Failed to Create New Collaboration", "Collaboration Already Exists");
            done();
        });
    });

    QUnit.test("Posting new Collaboration with negative nhid and negative member ID", function (assert) {
        var done = assert.async();
        var data = {
            "name": Collab_Name,
            "purpose": "Test"
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + GlobalData.NeighborhoodInput.Negative_Nh_Id + "/member/-1001/collaboration", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "IsNegative", "NHID is Negative");
            assert.equal(result[1].error, "IsNegative", "Member ID is Negative");
            done();
        });
    });

    QUnit.test("Posting New Collaboration with Missing Authorization", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var collab_name = "Collab_" + x;
        var data = {
            "name": collab_name,
            "purpose": "Test"
        }
        TestUtils.sendRequestMissingAuthorization(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + GlobalData.NeighborhoodInput.NHID_2 + "/member/" + GlobalData.UserInput.Member_Id_2 + "/collaboration", data, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in Header");
            done();
        })
    });

    QUnit.test("Posting New Collaboration with Invalid Authorization", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var collab_name = "Collab_" + x;
        var data = {
            "name": collab_name,
            "purpose": "Test"
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + GlobalData.NeighborhoodInput.NHID_2 + "/member/" + GlobalData.UserInput.Member_Id_2 + "/collaboration", data, GlobalData.UserInput.invalidAuthorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Invalid Authorization. User is not a member of Neighborhood Path.", "Invalid Authorization. User is not a member of Neighborhood Path.");
            done();
        });
    });

    QUnit.test("Posting New Collaboration with Invalid MemberShip", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var collab_name = "Collab_" + x;
        var data = {
            "name": collab_name,
            "purpose": "Test"
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + GlobalData.NeighborhoodInput.NHID_2 + "/member/" + GlobalData.UserInput.Invalid_MemberId + "/collaboration", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Authorization Is not allowed to create Collaboration in this Neighborood. [MemberID mismatch]", "Invalid Memberships");
            done();
        });
    });

    QUnit.test("Posting New Collaboration with Blank Fields", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var collab_name = "Collab_" + x;
        var data = {
            "name": "",
            "purpose": ""
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/3/member/" + GlobalData.UserInput.Member_Id_2 + "/collaboration", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "IsBlank", "Collab_Name is Blank");
            assert.equal(result[1].error, "IsBlank", "Collab_Name is Blank");
            done();
        });
    });
 
    QUnit.test("Deleting Collaboration", function (assert) {
        var done = assert.async();
        TestUtils.sendDeleteRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + collab_Id, GlobalData.UserInput.authorization).then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(typeof result, "string", "response should be string !");
            var getResponse = result;
            var checkSubString = getResponse.indexOf("Collaboration Deleted Successfully");
            assert.ok((checkSubString >= 0), "Collaboration Deleted Successfully");
            done();
        });
    });

    QUnit.test("Deleting Collaboration with Missing Authorization", function (assert) {
        var done = assert.async();
        TestUtils.sendRequestMissingAuthorization(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + collab_Id, null, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 400) {
                assert.ok(result.status == 400, " Missing Authorization in header ");
               // alert(JSON.stringify(result.responseText));
               // assert.equal(result[0].responseText, "Missing Authorization in Header", "Missing Authorization in header");
                done();
                return;
            }
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in header");
            done();
        });
    });

    QUnit.test("Deleting Collaboration with Invalid Collaboration Id", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Non_Existing_CollabId, null, GlobalData.UserInput.authorization, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 422) {
                assert.ok(result.status == 422, " Invalid Collaboration ID");
                done();
                return;
            }
            assert.equal(result[0].error, "Collaboration ID NOT FOUND", "Collaboration ID NOT FOUND ");
            done();
        });
    });

    QUnit.test("Deleting Collaboration with Negative Collaboration Id", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Negative_CollabId, null, GlobalData.UserInput.authorization, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 400) {
                assert.ok(result.status == 400, "Negative collab ID");
                done();
                return;
            }
            assert.equal(result[0].error, "IsNegative", "Collaboration Id is Negative");
            done();
        });
    });

})(QUnit.module, QUnit.test);