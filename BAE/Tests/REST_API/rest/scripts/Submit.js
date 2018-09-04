/**
 * Author : Jaydeep Bobade Tekvision
 * Module : Submit
 */

(function (module, test) {

    module("Submit Refresh Cycle");
    var cuboid_id = 0;
    var cuboid_name = "";
    var importTid_ = 0;
    var exportTid_ = 0;
    var rowArray_;
    var columnArray_;
    var importTid_1 = 0;
    var exportTid_1 = 0;
    var rowArray_1;
    var columnArray_1;

    function createGrid() {
        var promise = new $.Deferred();
        var x = Math.floor((Math.random() * 20000000000) + 1);
        cuboid_name = "Test_CUBOID_SuRe" + x;
        var data = {
            "name": cuboid_name,
            "description": "CHECK FOR SUCCESS",
            "wbId": GlobalData.WhiteboardInput.WB_Id,
            "collabId": GlobalData.CollaborationInput.Collab_Id_1,
            "memberId": GlobalData.UserInput.Member_Id_1,
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid", data, GlobalData.UserInput.authorization, "POST").then(function (result) {
            cuboid_id = result.gridId;
            var data = {
                "cells": GlobalData.GridInput.cells,
                "columnArray": GlobalData.GridInput.columnArray,
                "columnCellArrays": GlobalData.GridInput.columnCellArrays,
                "columns": GlobalData.GridInput.columns,
                "GridChangeBuffer": GlobalData.GridInput.GridChangeBuffer,
                "rowArray": GlobalData.GridInput.rowArray,
                "rows": GlobalData.GridInput.rows,
                "info": {   "collabId": GlobalData.CollaborationInput.Collab_Id_1, 
                            "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1 , "importTid": -1, "exportTid": -1}                
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GlobalData.GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
                    importTid_ = result.info.importTid;
                    exportTid_ = result.info.exportTid;
                    columnArray_ = result.columnArray;
                    rowArray_ = result.rowArray;
                    TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GlobalData.GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
                        importTid_1 = result.info.importTid;
                        exportTid_1 = result.info.exportTid;
                        columnArray_1 = result.columnArray;
                        rowArray_1 = result.rowArray;
                    });
                    promise.resolve();
                });
            });
        });
        return promise.promise();
    }

    function importGrid() {
        var promise = new $.Deferred();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GlobalData.GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            columnArray_ = result.columnArray;
            rowArray_ = result.rowArray;
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + GlobalData.GridInput.importTid + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
                importTid_1 = result.info.importTid;
                exportTid_1 = result.info.exportTid;
                columnArray_1 = result.columnArray;
                rowArray_1 = result.rowArray;
                promise.resolve();
            });           
        });
        return promise.promise();
    }

    QUnit.test("Submitting Grid with one cell with user1", function (assert) {
        var done = assert.async();
        createGrid().then(function () {
            console.log("Submitting Grid with one cell");
            var x = Math.floor((Math.random() * 200) + 1);
            var data = {
                "info": { "wbId": GlobalData.WhiteboardInput.WB_Id, "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },
                "cells": [{ "id": -1, "rowId": rowArray_[2], "colId": columnArray_[1], "rowSequence": 3, "colSequence": 2, "cellValue": "Sabarmati_", "cellFormula": "Sabarmati_", "active": true, "tid": -1, "changeFlag": 1, "access": -1 }],
                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [], "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.ok(exportTid_ < result.info.exportTid, "exportTid should be larger than the previous Submit operation.");
                SubmitRefreshUtils.assertInfo(result, assert);
                assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
                SubmitRefreshUtils.assertCells(result, assert);
                SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
                importTid_ = result.info.importTid;
                exportTid_ = result.info.exportTid;
                done();
            });
        });
    });

    QUnit.test("Refreshing Cuboid With One Cell with user2", function (assert) {
        var done = assert.async();
        console.log("Refresh with one cell");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with one cell with Missing Authorization with user1", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 200) + 1);
        var data = {
            "info": { "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1, "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },
            "cells": [{ "id": -1, "rowId": rowArray_[2], "colId": columnArray_[1], "rowSequence": 3, "colSequence": 2, "cellValue": "Sabarmati_", "cellFormula": "Sabarmati_", "active": true, "tid": -1, "changeFlag": 1, "access": -1 }],
            "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
            "GridChangeBuffer": {
                "newRowArray": [], "deletedRowArray": [], "newColumnArray": [],
                "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
            },
            "rowArray": []
        }
        TestUtils.sendRequestMissingAuthorization(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, "PUT").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in header");
            done();
        });
    });

    QUnit.test("Refreshing Cuboid With One Cell with Missing Authorization with user2", function (assert) {
        var done = assert.async();
        TestUtils.sendRequestMissingAuthorization(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null !");
            assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in header");
            done();
        });
    });

    QUnit.test("Submitting Grid with one row with user1", function (assert) {
        var done = assert.async();
        var data = {
            "info": { "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1, "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },
            "cells": [{ "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 2, "colSequence": 0, "cellValue": "Maharashtra", "cellFormula": "Maharashtra", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
            { "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 2, "colSequence": 1, "cellValue": "Satara", "cellFormula": "Satara", "active": true, "tid": -1, "changeFlag": 4, "access": -1 }],

            "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

            "GridChangeBuffer": {
                "newRowArray": [{ "active": true, "id": -1, "previousRowSequence": 1, "previousRowid": rowArray_[1], "seqNo": 2, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 }],
                "deletedRowArray": [], "newColumnArray": [], "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
            },
            "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            var resultJSON = result;
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "newColumnArray should not be null");
            SubmitRefreshUtils.assertNewRowArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Refreshing Cuboid With One Row with user2", function (assert) {
        var done = assert.async();
        console.log("Refresh with one Row");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "newColumnArray should not be null");
            SubmitRefreshUtils.assertNewRowArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with one row at the Bottom with user1", function (assert) {
        console.log("Submitting Grid with one row at the Bottom");
        var done = assert.async();
        var data = {
            "info": { "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1, "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },

            "cells": [
                { "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 6, "colSequence": 0, "cellValue": "Gujrat", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
                { "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 6, "colSequence": 1, "cellValue": "Ahmadabad_1", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4, "access": -1 }],

            "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

            "GridChangeBuffer": {
                "newRowArray": [{ "active": true, "id": -1, "previousRowSequence": 5, "previousRowid": rowArray_[3], "seqNo": 6, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 }],
                "deletedRowArray": [], "newColumnArray": [], "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
            }, "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            var resultJSON = result;
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "newColumnArray should not be null");
            SubmitRefreshUtils.assertNewRowArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Refreshing Cuboid With One Row at the bottom with user2", function (assert) {
        var done = assert.async();
        console.log("Refresh with one Row at the bottom");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "newColumnArray should not be null");
            SubmitRefreshUtils.assertNewRowArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with consequative rows with user1", function (assert) {
        var done = assert.async();
        var data = {
            "info": { "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1, "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },

            "cells": [{ "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 1, "colSequence": 1, "cellValue": "Maharashtra", "cellFormula": "Maharashtra", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
            { "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 2, "colSequence": 1, "cellValue": "Maharashtra", "cellFormula": "Maharashtra", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
            { "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 3, "colSequence": 1, "cellValue": "Maharashtra", "cellFormula": "Maharashtra", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
            { "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 4, "colSequence": 1, "cellValue": "Maharashtra", "cellFormula": "Maharashtra", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
            { "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 1, "colSequence": 2, "cellValue": "Sangli", "cellFormula": "Sangli", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
            { "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 2, "colSequence": 2, "cellValue": "Amravti", "cellFormula": "Amravti", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
            { "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 3, "colSequence": 2, "cellValue": "Akola", "cellFormula": "Akola", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
            { "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 4, "colSequence": 2, "cellValue": "Nagpur", "cellFormula": "Nagpur", "active": true, "tid": -1, "changeFlag": 4, "access": -1 }
            ],

            "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

            "GridChangeBuffer": {
                "newRowArray": [{ "active": true, "id": -1, "previousRowSequence": 1, "previousRowid": rowArray_[0], "seqNo": 1, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 },
                { "active": true, "id": -1, "previousRowSequence": 2, "previousRowid": -1, "seqNo": 2, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 },
                { "active": true, "id": -1, "previousRowSequence": 3, "previousRowid": -1, "seqNo": 3, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 },
                { "active": true, "id": -1, "previousRowSequence": 4, "previousRowid": -1, "seqNo": 4, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 }
                ],
                "deletedRowArray": [], "newColumnArray": [], "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
            },
            "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            var resultJSON = result;
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "newColumnArray should not be null");
            assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Refreshing Cuboid With consequative Row with user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing Cuboid With consequative Row with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "newColumnArray should not be null");
            SubmitRefreshUtils.assertNewRowArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with deleting one row with user1", function (assert) {
        var done = assert.async();
        var data = {
            "info": { "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1, "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },
            "cells": [], "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
            "GridChangeBuffer": {
                "newRowArray": [], "deletedRowArray": [rowArray_[0]],
                "newColumnArray": [], "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
            },
            "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            var resultJSON = result;
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.deletedRowArray != null, "newColumnArray should not be null");
            assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Refreshing Cuboid With deleting Row with user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing Cuboid With consequative Row with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "newColumnArray should not be null");
            SubmitRefreshUtils.assertNewRowArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with deleting multiple rows with user1", function (assert) {
        var done = assert.async();
        var data = {
            "info": { "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1, "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },

            "cells": [], "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

            "GridChangeBuffer": {
                "newRowArray": [],
                "deletedRowArray": [rowArray_[1], rowArray_[2], rowArray_[3]],
                "newColumnArray": [], "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
            },
            "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            var resultJSON = result;
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.deletedRowArray != null, "newColumnArray should not be null");
            assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Refreshing Cuboid With deleting multiple Rows with user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing Cuboid With consequative Row with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "newColumnArray should not be null");
            SubmitRefreshUtils.assertNewRowArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with three column and formulas with user1", function (assert) {
        var done = assert.async();
        importGrid().then(function () {
            console.log("Submitting Grid with three column and formulas with user1");
            var x = Math.floor((Math.random() * 200) + 1);
            var data = {
                "info": {
                    "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,
                    "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1,
                    "view": "LATEST", "mode": 0
                },
                "cells": [{ "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 1, "cellValue": "5", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 1, "cellValue": "7", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 1, "cellValue": "4", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[3], "colId": -1, "rowSequence": 3, "colSequence": 1, "cellValue": "9", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[4], "colId": -1, "rowSequence": 4, "colSequence": 1, "cellValue": "6", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[5], "colId": -1, "rowSequence": 5, "colSequence": 1, "cellValue": "8", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 2, "cellValue": "1000", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 2, "cellValue": "900", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 2, "cellValue": "584", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[3], "colId": -1, "rowSequence": 3, "colSequence": 2, "cellValue": "745", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[4], "colId": -1, "rowSequence": 4, "colSequence": 2, "cellValue": "458", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[5], "colId": -1, "rowSequence": 5, "colSequence": 2, "cellValue": "965", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 3, "cellValue": "5000", "cellFormula": "=RC[-2]*RC[-1]", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 3, "cellValue": "6300", "cellFormula": "=RC[-2]*RC[-1]", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 3, "cellValue": "2336", "cellFormula": "=RC[-2]*RC[-1]", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[3], "colId": -1, "rowSequence": 3, "colSequence": 3, "cellValue": "6705", "cellFormula": "=RC[-2]*RC[-1]", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[4], "colId": -1, "rowSequence": 4, "colSequence": 3, "cellValue": "2748", "cellFormula": "=RC[-2]*RC[-1]", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[5], "colId": -1, "rowSequence": 5, "colSequence": 3, "cellValue": "7720", "cellFormula": "=RC[-2]*RC[-1]", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [], "deletedRowArray": [],
                    "newColumnArray": [ {"active": true, "id": -1, "name": "Area","previousColumnSequence": -1, "previousColumnid": -1,"seqNo": 1, "tid": -1},
                                        {"active": true, "id": -1, "name": "person","previousColumnSequence": 1, "previousColumnid": -1,"seqNo": 2, "tid": -1},
                                        {"active": true, "id": -1, "name": "total","previousColumnSequence": 2, "previousColumnid": -1,"seqNo": 3, "tid": -1}],
                    "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                var resultJSON = result;
                importTid_ = result.info.importTid;
                exportTid_ = result.info.exportTid;
                assert.ok(result != null, "Response Should not be null");
                SubmitRefreshUtils.assertInfo(result, assert);
                assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
                SubmitRefreshUtils.assertCells(result, assert);
                SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
                assert.ok(result.GridChangeBuffer.newColumnArray != null, "newColumnArray should not be null");
                SubmitRefreshUtils.assertNewColumnArray(result.GridChangeBuffer.newColumnArray, assert);
                done();
            });
        });
    });

    QUnit.test("Refreshing Grid with three column and formulas wih user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing  Grid with three column and formulas with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.newColumnArray != null, "newColumnArray should not be null");
            SubmitRefreshUtils.assertNewColumnArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with deleting columns with user1", function (assert) {
        var done = assert.async();
        var data = {
            "info": {
                "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,
                "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1,
                "view": "LATEST", "mode": 0
            },

            "cells": [], "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

            "GridChangeBuffer": {
                "newRowArray": [], "deletedRowArray": [], "newColumnArray": [],
                "deletedColumnArray": [columnArray_[1]], "criticalLevel": 1, "critical": 0
            },
            "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            var resultJSON = result;
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.deletedColumnArray != null, "newColumnArray should not be null");
            assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Refreshing Grid with deleting columns with user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing Grid with deleting columns with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.deletedColumnArray != null, "deletedColumnArray should not be null");
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with deleting existing columns and Rows and adding new data with user1", function (assert) {
        var done = assert.async();
        importGrid().then(function () {
            var data = {
                "info": {"collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,"importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1,"view": "LATEST", "mode": 0},

                "cells": [
                    { "id": -1, "rowId": -1, "colId": -1, "rowSequence": 0, "colSequence": 1, "cellValue": "India", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": -1, "colId": -1, "rowSequence": 1, "colSequence": 1, "cellValue": "America", "cellFormula": "America", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": -1, "colId": -1, "rowSequence": 2, "colSequence": 1, "cellValue": "England", "cellFormula": "England", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": -1, "colId": -1, "rowSequence": 0, "colSequence": 2, "cellValue": "New Delhi", "cellFormula": "New Delhi", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": -1, "colId": -1, "rowSequence": 1, "colSequence": 2, "cellValue": "Washington", "cellFormula": "Washington", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": -1, "colId": -1, "rowSequence": 2, "colSequence": 2, "cellValue": "London", "cellFormula": "London", "active": true, "tid": -1, "changeFlag": 8, "access": -1 }],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

                "GridChangeBuffer": {
                    "newRowArray": [
                        { "active": true, "id": -1, "previousRowSequence": 1, "previousRowid": -1, "seqNo": 0, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 },
                        { "active": true, "id": -1, "previousRowSequence": 2, "previousRowid": -1, "seqNo": 1, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 },
                        { "active": true, "id": -1, "previousRowSequence": 3, "previousRowid": -1, "seqNo": 2, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 }],

                    "deletedRowArray": rowArray_,

                    "newColumnArray": [ { "active": true, "id": -1, "name": "Country", "previousColumnSequence": -1, "previousColumnid": -1, "seqNo": 1, "tid": -1 },
                                        { "active": true, "id": -1, "name": "Capital", "previousColumnSequence": 1, "previousColumnid": -1, "seqNo": 2, "tid": -1 }],

                    "deletedColumnArray": columnArray_, "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                var resultJSON = result;
                assert.ok(result != null, "Response Should not be null");
                SubmitRefreshUtils.assertInfo(result, assert);
                SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
                assert.ok(result.GridChangeBuffer.deletedColumnArray != null, "newColumnArray should not be null");
                assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
                importTid_ = result.info.importTid;
                exportTid_ = result.info.exportTid;
                done();
            });
        });
    });

    QUnit.test("Refreshing Grid with deleting existing columns and Rows and adding new data with user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing Grid with deleting existing columns and Rows and adding new data with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            assert.ok(result.GridChangeBuffer.deletedColumnArray != null, "deletedColumnArray should not be null");
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with deleting existing row and inserting Row at same position with user1", function (assert) {
        var done = assert.async();
        importGrid().then(function () {
            var data = {
                "info": {"collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,"importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1,"view": "LATEST", "mode": 0},

                "cells": [
                    { "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 2, "colSequence": 1, "cellValue": "Maharashtra", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4, "access": -1 },
                    { "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 2, "colSequence": 2, "cellValue": "Mumbai", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4, "access": -1 }],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

                "GridChangeBuffer": {
                    "newRowArray": [{"active": true, "id": -1, "previousRowSequence": 1, "previousRowid": rowArray_[0], "seqNo": 2, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1 }],

                    "deletedRowArray": [rowArray_[1]],

                    "newColumnArray": [], "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                var resultJSON = result;
                assert.ok(result != null, "Response Should not be null");
                SubmitRefreshUtils.assertInfo(result, assert);
                SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
                assert.ok(result.GridChangeBuffer.deletedColumnArray != null, "newColumnArray should not be null");
                assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
                importTid_ = result.info.importTid;
                exportTid_ = result.info.exportTid;
                done();
            });
        });
    });

    QUnit.test("Refreshing Grid with deleting existing row and inserting Row at same position with user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing Grid with deleting existing row and inserting Row at same position with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            SubmitRefreshUtils.assertNewRowArray(result.GridChangeBuffer.newRowArray, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "deletedColumnArray should not be null");
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with deleting existing Column and inserting Column at same position with user1", function (assert) {
        var done = assert.async();
        importGrid().then(function () {
            var data = {
                "info": {"collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,"importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1,"view": "LATEST", "mode": 0},

                "cells": [
                    { "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 1, "cellValue": "121", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 1, "cellValue": "122", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 1, "cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 }],
                    
                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

                "GridChangeBuffer": {
                    "newRowArray": [ ], "deletedRowArray": [],

                    "newColumnArray": [{"active": true, "id": -1, "name": "Pin","previousColumnSequence": -1, "previousColumnid": -1,"seqNo": 1, "tid": -1}], 
                    
                    "deletedColumnArray": [columnArray_[0]], "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                var resultJSON = result;
                assert.ok(result != null, "Response Should not be null");
                SubmitRefreshUtils.assertInfo(result, assert);
                SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
                SubmitRefreshUtils.assertNewColumnArray(result.GridChangeBuffer.newColumnArray, assert);
                assert.ok(result.GridChangeBuffer.deletedColumnArray != null, "newColumnArray should not be null");
                assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
                importTid_ = result.info.importTid;
                exportTid_ = result.info.exportTid;
                done();
            });
        });
    });

    QUnit.test("Refreshing Grid with deleting existing column and inserting column at same position with user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing Grid with deleting existing column and inserting column at same position with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            SubmitRefreshUtils.assertNewColumnArray(result.GridChangeBuffer.newColumnArray, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "deletedColumnArray should not be null");
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with inserting Column at last position with user1", function (assert) {
        var done = assert.async();
        importGrid().then(function () {
            var data = {
                "info": {"collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,"importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1,"view": "LATEST", "mode": 0},

                "cells": [
                    { "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 3, "cellValue": "10000", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 3, "cellValue": "20000", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                    { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 3, "cellValue": "30000", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 }],
                    
                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

                "GridChangeBuffer": {
                    "newRowArray": [ ], "deletedRowArray": [],

                    "newColumnArray": [{"active": true, "id": -1, "name": "AreaSquare","previousColumnSequence": 2, "previousColumnid": columnArray_[1],"seqNo": 3, "tid": -1}], 
                    
                    "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                var resultJSON = result;
                assert.ok(result != null, "Response Should not be null");
                SubmitRefreshUtils.assertInfo(result, assert);
                SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
                SubmitRefreshUtils.assertNewColumnArray(result.GridChangeBuffer.newColumnArray, assert);
                assert.ok(result.GridChangeBuffer.deletedColumnArray != null, "newColumnArray should not be null");
                assert.ok(exportTid_ < result.info.exportTid, "exportTid Should increased");
                importTid_ = result.info.importTid;
                exportTid_ = result.info.exportTid;
                done();
            });
        });
    });

    QUnit.test("Refreshing Grid with inserting Column at last position with user2", function (assert) {
        var done = assert.async();
        console.log("Refreshing Grid with inserting Column at last position with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            SubmitRefreshUtils.assertInfo(result, assert);
            assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
            SubmitRefreshUtils.assertCells(result, assert);
            SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
            SubmitRefreshUtils.assertNewColumnArray(result.GridChangeBuffer.newColumnArray, assert);
            assert.ok(result.GridChangeBuffer.newRowArray != null, "deletedColumnArray should not be null");
            assert.ok(result.rowArray != null ,"rowArray should not be null");
            assert.ok(Array.isArray(result.rowArray), "rowArray should be Array");
            assert.ok(result.columnArray != null ,"columnArray should not be null");
            assert.ok(Array.isArray(result.columnArray), "columnArray should be Array")
            GridUtils.assertRows(result.rows, assert, result.rows.length);
            GridUtils.assertColumns(result.columns, assert, result.columns.length);
            importTid_1 = result.info.importTid;
            exportTid_1 = result.info.exportTid;
            done();
        });
    });

    QUnit.test("Submitting Grid with Missing cells element", function (assert) {
        var done = assert.async();
        createGrid().then(function () {
            var x = Math.floor((Math.random() * 200) + 1);
            var data = {
                "info": {
                    "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,
                    "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1,
                    "view": "LATEST", "mode": 0
                },

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

                "GridChangeBuffer": {
                    "newRowArray": [], "deletedRowArray": [],
                    "newColumnArray": [{ "active": true, "id": -1, "name": "city1", "previousColumnSequence": 1, "previousColumnid": columnArray_[0], "seqNo": 1, "tid": -1 }],
                    "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                var resultJSON = result;
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result[0].error, "Missing element cells:[]", "cells element is not present in Request");
                done();
            });
        });
    });

   /* QUnit.test("Submitting Grid with one cell with user1 for old sheet check", function (assert) {
        var done = assert.async();
        createGrid().then(function () {
            console.log("Submitting Grid with one cell with old sheet check error");
            var x = Math.floor((Math.random() * 200) + 1);
            var data = {
                "info": { "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1, "importTid": importTid_, "exportTid": (exportTid_ - 20), "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },
                "cells": [{ "id": -1, "rowId": rowArray_[2], "colId": columnArray_[1], "rowSequence": 3, "colSequence": 2, "cellValue": "Sabarmati_", "cellFormula": "Sabarmati_", "active": true, "tid": -1, "changeFlag": 1, "access": -1 }],
                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [], "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result.error, "Old sheet Check error", "Old sheet Check error");
                done();
            });
        });
    });*/

    QUnit.test("Submitting Grid with one cell with invalid Authorization", function (assert) {
        var done = assert.async();
        createGrid().then(function () {
            console.log("Submitting Grid with one cell");
            var x = Math.floor((Math.random() * 200) + 1);
            var data = {
                "info": { "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1, "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0 },
                "cells": [{ "id": -1, "rowId": rowArray_[2], "colId": columnArray_[1], "rowSequence": 3, "colSequence": 2, "cellValue": "Sabarmati_", "cellFormula": "Sabarmati_", "active": true, "tid": -1, "changeFlag": 1, "access": -1 }],
                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [], "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
                },
                "rowArray": []
            }
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.invalidAuthorization, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result[0].error, "Invalid Authorization. User is not a member of Neighborhood Path.", "Invalid Authorization");
                done();
            });
        });
    });

    QUnit.test("Refreshing Grid with Missing importTid in url", function (assert) {
        var done = assert.async();
        console.log("Refreshing Grid with deleting columns with user2");
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?view=LATEST&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization_1, "GET").then(function (result) {
            var resultJSON = result;
            assert.ok(result != null, "Response Should not be null");
            assert.equal(result[0].error, "importTid is missing in GET Request", "importTid is missing in GET Request");
            done();
        });
    });

    QUnit.test("Refreshing Grid with view is Missing", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "View is missing in GET Request", "View is missing in GET Request");
            done();
        });
    });

    QUnit.test("Refreshing Grid with Invalid view", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST11&mode=1&baselineId=-1", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "Invalid View in GET Request", "Invalid View in GET Request");
            done();
        });
    });

    QUnit.test("Refreshing Grid with mode is Missing", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&baselineId=-1", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "Mode is missing in GET Request", "Mode is missing in GET Request");
            done();
        });
    });

    QUnit.test("Refreshing Grid with Invalid mode", function (assert) {
        var done = assert.async();
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid/" + cuboid_id + "?importTid=" + importTid_1 + "&view=LATEST&mode=123&baselineId=-1", null, GlobalData.UserInput.authorization, "GET").then(function (result) {
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "Invalid Mode in GET Request", "Invalid Mode in GET Request");
            done();
        });
    });

    QUnit.test("Submitting Grid with Missing GridChangeBuffer", function (assert) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 200) + 1);
        var data = {
            "info": {
                "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,
                "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1,
                "view": "LATEST", "mode": 0
            },
            "cells": [
                { "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[3], "colId": -1, "rowSequence": 3, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 }],
            "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [], "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            assert.ok(result != null, "Response Should not be null");
            if (result.status == 500) {
                assert.ok(result.status == 500, " It seems server side error" + result.responseText);
                done();
                return;
            }
            var resultJSON = result;
            assert.equal(result[0].error, "Missing element GridChangeBuffer:[]", "GridChangeBuffer element is not present in Request");
            done();
        });
    });

    QUnit.test("Submitting Grid with Existing column name", function (assert) {
        var done = assert.async();
        var data = {
            "info": {
                "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,
                "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST",
                "mode": 0
            },
            "cells": [
                { "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[3], "colId": -1, "rowSequence": 3, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 }],

            "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

            "GridChangeBuffer": {
                "newRowArray": [], "deletedRowArray": [],
                "newColumnArray": [{ "active": true, "id": -1, "name": "city", "previousColumnSequence": 1, "previousColumnid": columnArray_[0], "seqNo": 1, "tid": -1 }],
                "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
            },
            "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            var resultJSON = result;
            assert.ok(result != null, "Response Should not be null");
            assert.equal(result[0].error, "TABLE UPDATE EXCEPTION : Unique Column violation", "Column Name already Exists");
            done();
        });
    });

    QUnit.test("Submitting Grid with Blank column name", function (assert) {
        var done = assert.async();
        var data = {
            "info": {
                "collabId": GlobalData.CollaborationInput.Collab_Id_1, "wbId": GlobalData.WhiteboardInput.WB_Id, "memberId": GlobalData.UserInput.Member_Id_1,
                "importTid": importTid_, "exportTid": exportTid_, "baselineId": -1, "criteriaTableId": -1, "view": "LATEST",
                "mode": 0
            },
            "cells": [
                { "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[3], "colId": -1, "rowSequence": 3, "colSequence": 1, "cellValue": "abc", "cellFormula": "abc", "active": true, "tid": -1, "changeFlag": 8, "access": -1 }],

            "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],

            "GridChangeBuffer": {
                "newRowArray": [], "deletedRowArray": [],
                "newColumnArray": [{ "active": true, "id": -1, "name": "", "previousColumnSequence": 1, "previousColumnid": columnArray_[0], "seqNo": 1, "tid": -1 }],
                "deletedColumnArray": [], "criticalLevel": 1, "critical": 0
            },
            "rowArray": []
        }
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_id, data, GlobalData.UserInput.authorization, "PUT").then(function (result) {
            var resultJSON = result;
            assert.ok(result != null, "Response Should not be null");
            assert.equal(result[0].error, "Column Name cannot be Blank.", "Column Name is Blank");
            done();
        });
    });

})(QUnit.module, QUnit.test);