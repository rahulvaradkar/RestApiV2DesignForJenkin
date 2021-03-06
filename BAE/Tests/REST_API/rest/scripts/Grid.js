/**
 * Author : Jaydeep Bobade Tekvision
 * Module : Grid
 */

(function (module, test) {

    module("Grid");
    var cuboid_id = 0;
    var cuboid_name = "";
    var cuboid_id_ = 0;
    var cuboid_name_ = "";

    var columnArray_Submit;
    QUnit.test("Creating Grid", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 1000000000) + 1);
        cuboid_name = "GOOD_CUBOID_" + x;
        var data = {
            "name": "GOOD_CUBOID_" + x,
            "description": "CHECK FOR SUCCESS",
            "wbId": WhiteboardInput.WB_Id,
            "collabId": CollaborationInput.Collab_Id_1,
            "memberId": UserInput.Member_Id_1,
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            cuboid_id = result.gridId;
            var flag = false;
            if (result.collabId != null && result.gridId != null && result.name != null && result.memberId != null && result.wbId != null && result.description != null) {
                flag = true;
            }            
            assert.equal(typeof result.collabId, "number", "Collab Id should be number");
            assert.ok(result.collabId >= 1000, "Collab should greater than 1000");
            assert.equal(typeof result.gridId, "number", "Grid Id Id should be number");
            assert.ok(result.gridId > 2000000, "Grid Id should greater than 2000000");
            assert.equal(typeof result.memberId, "number", "Member should be number");
            assert.ok(result.memberId >= 1000, "Member Id should greater than 1000 or 1000");
            assert.equal(typeof result.wbId, "number", "Whiteboard Id should be number");
            assert.ok(result.wbId >= 1000, "Whiteboard Id should greater than 1000 or 100");
            assert.equal(typeof result.description, "string", "description should be String");
            assert.equal(typeof result.name, "string", "Name should be String");

            assert.ok(flag, "Should have 6 Properties or should not have any Property Null");
            done();
        })
    });

    QUnit.test("Grid already exists", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
            "name": "GOOD_CUBOID",
            "description": "CHECK FOR SUCCESS",
            "wbId": WhiteboardInput.WB_Id,
            "collabId": CollaborationInput.Collab_Id_1,
            "memberId": UserInput.Member_Id_1
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.ok(result[0].error, "SQLException:2627, Cause:Violation of UNIQUE KEY constraint 'UQ_NC_BW_TBL'. Cannot insert duplicate key in object 'dbo.BW_TBL'.", "Grid is already exists");
            done();
        });
    });

    QUnit.test("User having Multiple Memberships creating Grid using MEMBERSHIP-2", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000) + 1);
        cuboid_name_ = "GOOD_CUBOID_" + x;
        var data = {
            "name": cuboid_name_,
            "description": "CHECK FOR SUCCESS",
            "wbId": WhiteboardInput.WB_Id,
            "collabId": CollaborationInput.Collab_Id_1,
            "memberId": UserInput.Member_Id_2
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            var flag = false;
            cuboid_id_ = result.gridId;
            if (result.collabId != null && result.gridId != null && result.name != null && result.memberId != null && result.wbId != null && result.description != null) {
                flag = true;
            }
            assert.ok(flag, "Should have 6 Properties or should not have any Property Null");
            assert.ok(result.gridId > 2000000, "Grid Id should greater than 2000000");
            done();
        });
    });

    QUnit.test("Invalid Input Checks. Negative Numbers and Blank Values in required fields", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000) + 1);
        var data = {
            "name": "",
            "description": "",
            "wbId": WhiteboardInput.Negative_WbId,
            "collabId": CollaborationInput.Negative_CollabId,
            "memberId": UserInput.Negative_MemberId
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "IsNegative", "Collab Id is Blank");
            assert.equal(result[1].error, "IsNegative", "Whiteboard Id is Blank");
            assert.equal(result[3].error, "IsBlank", "Name is Blank");
            assert.equal(result[2].error, "IsBlank", "description is Blank");
            done();
        });
    });

    QUnit.test("InValid Collaboration ID", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000) + 1);
        var data = {
            "name": "GOOD_CUBOID_" + x,
            "description": "CHECK FOR SUCCESS",
            "wbId": WhiteboardInput.WB_Id,
            "collabId": 9999,
            "memberId": UserInput.Member_Id_1
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Collaboration ID NOT FOUND", "InValid Collaboration ID");
            done();
        });
    });

    QUnit.test("InValid Whiteboard ID", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000) + 1);
        var data = {
            "name": "GOOD_CUBOID_" + x,
            "description": "CHECK FOR SUCCESS",
            "wbId": 9999,
            "collabId": CollaborationInput.Collab_Id_1,
            "memberId": UserInput.Member_Id_1
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid", data, UserInput.authorization, "POST").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Whitebaord ID NOT FOUND", "InValid Whiteboard ID");
            done();
        });
    });

    QUnit.test("Exporting Grid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
                "cells": GridInput.cells,
                "columnArray": GridInput.columnArray,
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,
                "rowArray": GridInput.rowArray,
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        Cuboid_Submit = cuboid_id;
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            var resultJSON = result;
            assert.equal(Object.keys(resultJSON).length, 8, "cuboid object should have only 8 properties !");
            assert.ok(result.cells != null, "Cells  Element Should not be null");

            assert.ok(result.columnArray != null, "columnArray Element Should not be null");
            assert.ok(Array.isArray(resultJSON.columnArray), "columnArray should be an Array !");

            assert.ok(result.columnCellArrays != null, "columnCellArrays Element Should not be null");
            assert.ok(Array.isArray(resultJSON.columnCellArrays), "columns cells should be an Array !");
            GridUtils.assertColumnCellArrays(result.columnCellArrays, assert, result.columnArray.length, "export");

            assert.ok(Array.isArray(resultJSON.columns), "columns property should be an Array !");
            GridUtils.assertColumns(result.columns, assert, result.columnArray.length);
            assert.ok(result.info != null, "info Element Should not be null");
            GridUtils.assertGridInfo(result.info, assert, "export");

            assert.ok(Array.isArray(resultJSON.rowArray), "rowArray should be an Array !");
            assert.ok(result.rowArray != null, "rowArray Element Should not be null");
            GridUtils.assertRows(result.rows, assert, result.rowArray.length);
            assert.ok(result.rows != null, "rows Element Should not be null");
            done();
        });
    });

    QUnit.test("Exporting Grid with Missing Authorization", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
                "cells": GridInput.cells,
                "columnArray": GridInput.columnArray,
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,
                "rowArray": GridInput.rowArray,
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, "PUT").then(function (result) {
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

    QUnit.test("Exporting Grid with Invalid Authorization", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
                "cells": GridInput.cells,
                "columnArray": GridInput.columnArray,
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,
                "rowArray": GridInput.rowArray,
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, UserInput.invalidAuthorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Authentication_Connection_Failure", "Authentication_Connection_Failure");
            done();
        });
    });

    QUnit.test("Exporting Grid with Duplicate Column names", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
                "cells": GridInput.cells,
                "columnArray": GridInput.columnArray,
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns_DuplicateColumns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,
                "rowArray": GridInput.rowArray,
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "SQLException:2627, Cause:Violation of UNIQUE KEY constraint 'UQ_NC_BW_COLUMN'. Cannot insert duplicate key in object 'dbo.BW_COLUMN'. The duplicate key value is (" + cuboid_id + ", state).", "Duplicate Column Names");
            done();
        });
    });

    QUnit.test("Missing columns element while Exporting Grid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
                "cells": GridInput.cells,
                "columnArray": GridInput.columnArray,
                "columnCellArrays": GridInput.columnCellArrays,                
                "GridChangeBuffer": GridInput.GridChangeBuffer,
                "rowArray": GridInput.rowArray,
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Missing element columns:[]", "Missing element columns:[]");
            done();
        });
    });

    QUnit.test("Missing Cell element while Exporting Grid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
                "columnArray": GridInput.columnArray,
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,
                "rowArray": GridInput.rowArray,
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id_, data, UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Missing element cells:[]", "Missing element cells:[]");
            done();
        });
    });

    QUnit.test("Missing rows element while Exporting Grid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
                "cells": GridInput.cells,
                "columnArray": GridInput.columnArray,
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,
                "rowArray": GridInput.rowArray,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }

        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id_, data, UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Missing element rows:[]", "Missing element rows:[]");
            done();
        });
    });

    QUnit.test("Missing rowArray while Exporting Grid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {
                "cells": GridInput.cells,
                "columnArray": GridInput.columnArray,
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,                
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Missing element rowArray:[]", "Missing element rowArray:[]");
            done();
        });
    });

    QUnit.test("Missing columnArray element while Exporting Grid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data ={
                "cells": GridInput.cells,                
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,
                "rowArray": GridInput.rowArray,
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id_, data, UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Missing element columnArray:[]", "Missing element columnArray:[]");
            done();
        });
    });

    QUnit.test("Missing cells, rowArray and columnArray element while Exporting Grid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        var data = {                              
                "columnCellArrays": GridInput.columnCellArrays,
                "columns": GridInput.columns,
                "GridChangeBuffer": GridInput.GridChangeBuffer,                
                "rows": GridInput.rows,
                "info": {
                    "exportTid": GridInput.exportTid,
                    "importTid": GridInput.importTid, "collabId": CollaborationInput.Collab_Id_1,
                    "wbId": WhiteboardInput.WB_Id, "memberId": UserInput.Member_Id_1
                }
            }
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id_, data, UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Missing element cells:[]", "Missing element cells:[]");
            assert.equal(result[1].error, "Missing element rowArray:[]", "Missing element rowArray:[]");
            assert.equal(result[2].error, "Missing element columnArray:[]", "Missing element columnArray:[]");
            done();
        });
    });

    QUnit.test("Importing Grid", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.ok(result.cells != null, "Cells  Element Should not be null");
            assert.ok(result.columnArray != null, "columnArray Element Should not be null");
            assert.ok(result.columnCellArrays != null, "columnCellArrays Element Should not be null");
            GridUtils.assertColumnCellArrays(result.columnCellArrays, assert, result.columnArray.length, "import");
            GridUtils.assertColumns(result.columns, assert, result.columnArray.length);
            assert.ok(result.info != null, "info Element Should not be null");
            GridUtils.assertGridInfo(result.info, assert, "import");
            assert.ok(result.rowArray != null, "rowArray Element Should not be null");
            GridUtils.assertRows(result.rows, assert, result.rowArray.length);
            assert.ok(result.rows != null, "rows Element Should not be null");
            done();
        });
    });

    QUnit.test("Importing Grid with Invalid Authorization", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, UserInput.invalidAuthorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Authentication_Connection_Failure", "Authentication_Connection_Failure");
            done();
        });
    });

    QUnit.test("Importing Grid with Missing Authorization", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, "GET").then(function (result) {
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

    QUnit.test("Importing Grid with ImportTid is Missing", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?view=LATEST&mode=1&baselineId=-1", null, UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "importTid is missing in GET Request", "importTid is missing in GET Request");
            done();
        });
    });

    QUnit.test("Importing Grid with view is Missing", function (assert) {
		console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&mode=1&baselineId=-1", null, UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "View is missing in GET Request", "View is missing in GET Request");
            done();
        });
    });

    QUnit.test("Importing Grid with Invalid view", function (assert) {
		console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST11&mode=1&baselineId=-1", null, UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Invalid View in GET Request", "Invalid View in GET Request");
            done();
        });
    });

    QUnit.test("Importing Grid with mode is Missing", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST&baselineId=-1", null, UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Mode is missing in GET Request", "Mode is missing in GET Request");
            done();
        });
    });

    QUnit.test("Importing Grid with Invalid mode", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST&mode=123&baselineId=-1", null, UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Invalid Mode in GET Request", "Invalid Mode in GET Request");
            done();
        });
    });

    QUnit.test("Importing Grid with BaselineId is Missing", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST&mode=1", null, UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "BaselineId is missing in GET Request", "BaselineId is missing in GET Request");
            done();
        });
    });

    QUnit.test("Importing Grid with Wrong username and wrong Password", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, getAuthorization("nhj", 8, "JSAddin"), "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Authentication_Connection_Failure", "Enter Correct Username and Password");
            done();
        });
    });

    QUnit.test("Importing Grid with Wrong authorization format", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done = assert.async();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, getAuthorization("j", 0, ""), "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            assert.equal(result[0].error, "Invalid Authorization Format", "Check Authorization Format");
            done();
        });
    });

})(QUnit.module, QUnit.test);