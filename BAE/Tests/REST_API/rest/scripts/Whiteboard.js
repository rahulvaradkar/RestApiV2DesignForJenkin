(function (module, test) {

    module('Whiteboard');
    var wb_Id = 0;
    var wb_name;
    QUnit.test("Reading Whiteboard of Collaboration ID = 1000", function (assert) {
        var done = assert.async();
        var input = $("#test-input").focus();
        var flag = false;
        var count_flag = false;

        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Collab_Id_1 + "/whiteboard", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            if (result.length > 0) {
                count_flag = true;
            }
            assert.ok(count_flag, "Retrieved the WhiteBoard Info");
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
                    assert.equal(typeof result[i].id, "number", "Id Should be Number");
                    assert.ok(result[i].id >= 1000, "Id Should be greater than or equal to 1000")
                }
                assert.ok(flag, "Properties Should not be null !")
            }
            assert.ok(count_flag, "Retrieved the WhiteBoard Info");
            done();
        });
    });

    QUnit.test("Reading Whiteboard of not existed Collaboration ID=999", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Non_Existing_CollabId + "/whiteboard", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.ok(result[0].error != null, "Response should not be null !");
            assert.equal(result[0].error, "Collaboration ID NOT FOUND", "Collaboration Id Not Found");
            done();
        });
    });

    QUnit.test("Reading Whiteboard of Collaboration with Negative ID= -999", function (assert) {
        var done = assert.async();
        var input = $("#test-input").focus();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Negative_CollabId + "/whiteboard", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "IsNegative", "Collaboration Id is Negative");
            done();
        });
    });

    QUnit.test("Posting new Whiteboard", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        wb_name = "ApiTest" + x;
        var data = {
            "name": wb_name
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Collab_Id_2 + "/whiteboard", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            wb_Id = result;
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(typeof result, "number", "response should be Integer");
            assert.ok(result != null, "Whiteboard Id should not be null")
            assert.ok(result > 1000, "Whiteboard Created Successfully")
            done();
        });
    });

    QUnit.test("Posting new Whiteboard with Missing Authorization", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var data = {
            "name": "ApiTest" + x
        }
        TestUtils.sendRequestMissingAuthorization(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Collab_Id_2 + "/whiteboard", data, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in Header")
            done();
        });
    });

    QUnit.test("Posting existed Whiteboard", function (assert) {
        var done = assert.async();
        var data = {
            "name": wb_name
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Collab_Id_2 + "/whiteboard", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "Whiteboard already Exists with this name.", "Whiteboard already Exists with this name.")
            done();
        });
    });

    QUnit.test("Posting new Whiteboard with Blank Name", function (assert) {
        var done = assert.async();
        var data = {
            "name": ""
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Collab_Id_2 + "/whiteboard", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "IsBlank", "Error: Whiteboard name is blank")
            done();
        });
    });

    QUnit.test("Posting new Whiteboard with invalid Collaboration Id", function (assert) {
        var done = assert.async();
        var data = {
            "name": "APii"
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Non_Existing_CollabId + "/whiteboard", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "Collaboration does not exist for this Collaboration Id", " Error : Collaboration Is not exist")
            done();
        });
    });

    QUnit.test("Deleting Whiteboard", function (assert) {
        var done = assert.async();
        TestUtils.sendDeleteRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Collab_Id_2 + "/whiteboard/" + wb_Id, GlobalData.UserInput.authorization).then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(typeof result, "string", "response should be string !");
            var getResponse = result;
            var checkSubString = getResponse.indexOf("successfully Deleted.");
            assert.ok((checkSubString >= 0), "Whiteboard Deleted Successfully");
            done();
        });
    });

    QUnit.test("Deleting Whiteboard with Missing authorization", function (assert) {
        var done = assert.async();
        TestUtils.sendRequestMissingAuthorization(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Collab_Id_2 + "/whiteboard/" + wb_Id, null, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in Header")
            done();
        });
    });

    QUnit.test("Deleting Whiteboard with non existing collaboration Id", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Non_Existing_CollabId + "/whiteboard/" + wb_Id, null, GlobalData.UserInput.authorization, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "Collaboration ID NOT FOUND", "Collaboration ID NOT FOUND")
            done();
        });
    });

    QUnit.test("Deleting Whiteboard with non existing whiteboard Id", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Collab_Id_2 + "/whiteboard/" + GlobalData.WhiteboardInput.Invalid_WbId, null, GlobalData.UserInput.authorization, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "Whiteboard ID NOT FOUND", "Whiteboard ID NOT FOUND")
            done();
        });
    });

    QUnit.test("Deleting Whiteboard with negative collaboration Id and Whiteboard Id", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + GlobalData.CollaborationInput.Negative_CollabId + "/whiteboard/" + GlobalData.WhiteboardInput.Negative_WbId, null, GlobalData.UserInput.authorization, "DELETE").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500){
                assert.ok(result.status == 500 ," It seems server side error" + result.responseText);
                done();
                return ;
            }
            assert.equal(result[0].error, "IsNegative", "Collab ID is Negative");
            assert.equal(result[1].error, "IsNegative", "Whiteboard ID is Negative");
            done();
        });
    });
})(QUnit.module, QUnit.test);