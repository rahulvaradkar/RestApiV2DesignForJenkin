(function (module, test) {

    module("Access Control");

    var importTid_ = 0;
    var exportTid_ = 0;
    var rowArray_;
    var columnArray_;
    var importTid_1 = 0;
    var exportTid_1 = 0;
    var rowArray_1;
    var column_Id=0

    function importGrid(authorization) {
        var promise = new $.Deferred();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + Access_Control.Access_Cuboid_Id + "?importTid=-1&view=LATEST&mode=1&baselineId=-1", null, authorization, "GET").then(function (result) {
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            columnArray_ = result.columnArray;
            rowArray_ = result.rowArray;
            promise.resolve();
        });
        return promise.promise();
    }

    QUnit.test("User has Access to add rows Adding Row with User1", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_1).then(function () {
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User1_Member_Id_1, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [
                    {"id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 2, "colSequence": 0,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 2, "colSequence": 1,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[2], "rowSequence": 2, "colSequence": 2,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[3], "rowSequence": 2, "colSequence": 3,"cellValue": "Mumbai", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1}
                ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [{
                        "active": true, "id": -1, "previousRowSequence": 1, "previousRowid": rowArray_[1],
                        "seqNo": 2, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1,
                        "createrId": -1, "ownershipAssignedTid": -1
                    }],
                    "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data,Access_Control.Row_Access_User_1, "PUT").then(function (result) {
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
    });

    QUnit.test("User1 dont have access to delete rows trying to Delete rows using User1", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_1).then(function () {
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User1_Member_Id_1, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [ ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ rowArray_[0] ], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_1, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result[0].error, "TABLE UPDATE EXCEPTION (12010): No access to Delete a Row", "User can't delete Rows");
                done();
            });
        });
    });

    QUnit.test("User1 hace access to R/W trying to submit with one cell", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_1).then(function () {
            var x = Math.floor((Math.random() * 10000000) + 1);
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User1_Member_Id_1, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [{ "id": -1, "rowId": rowArray_[2], "colId": columnArray_[3], "rowSequence": 3, "colSequence": 2, 
                    "cellValue": "NewDelhi_" + x, "cellFormula": "Sabarmati_", 
                    "active": true, "tid": -1, "changeFlag": 1, "access": -1 } ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ ], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_1, "PUT").then(function (result) {
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

    QUnit.test("User does not has Access to add rows Adding Row with User2", function (assert) {
	//	console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_2).then(function () {
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User2_Member_Id_2, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [
                    {"id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 2, "colSequence": 0,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 2, "colSequence": 1,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[2], "rowSequence": 2, "colSequence": 2,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[3], "rowSequence": 2, "colSequence": 3,"cellValue": "Mumbai", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1}
                ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [{
                        "active": true, "id": -1, "previousRowSequence": 1, "previousRowid": rowArray_[1],
                        "seqNo": 2, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1,
                        "createrId": -1, "ownershipAssignedTid": -1
                    }],
                    "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data,Access_Control.Row_Access_User_2, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result[0].error, "TABLE UPDATE EXCEPTION (12012): No access to Add a Row", "User can't Add Rows");
                done();
            });
        });
    });

    QUnit.test("User have access to delete rows trying to Delete rows using User2", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_2).then(function () {
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User2_Member_Id_2, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [ ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ rowArray_[0] ], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_2, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.ok(result.GridChangeBuffer.deletedRowArray != null , "deletedRowArray should not be null");
                assert.ok(Array.isArray(result.GridChangeBuffer.deletedRowArray),"deletedRowArray should be Array");
                done();
            });
        });
    });

    QUnit.test("User hace access to R/W trying to submit with one cell with user2", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_2).then(function () {
            var x = Math.floor((Math.random() * 10000000) + 1);
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User2_Member_Id_2, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [{ "id": -1, "rowId": rowArray_[2], "colId": columnArray_[3], "rowSequence": 3, "colSequence": 2, 
                    "cellValue": "NewDelhi_" + x, "cellFormula": "Sabarmati_", 
                    "active": true, "tid": -1, "changeFlag": 1, "access": -1 } ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ ], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_2, "PUT").then(function (result) {
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

    QUnit.test("User does not has Access to add rows Adding Row with User3", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_3).then(function () {
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User1_Member_Id_3, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [
                    {"id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 2, "colSequence": 0,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 2, "colSequence": 1,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[2], "rowSequence": 2, "colSequence": 2,"cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1},
                    {"id": -1, "rowId": -1, "colId": columnArray_[3], "rowSequence": 2, "colSequence": 3,"cellValue": "Mumbai", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,"access": -1}
                ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [{
                        "active": true, "id": -1, "previousRowSequence": 1, "previousRowid": rowArray_[1],
                        "seqNo": 2, "tid": -1, "rowName": " ", "ownerId": -1, "ownerName": " ", "creationTid": -1,
                        "createrId": -1, "ownershipAssignedTid": -1
                    }],
                    "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data,Access_Control.Row_Access_User_3, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result[0].error, "TABLE UPDATE EXCEPTION (12012): No access to Add a Row", "User can't Add Rows");
                done();
            });
        });
    });

    QUnit.test("User dont have access to delete rows trying to Delete rows using User3", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_3).then(function () {
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User2_Member_Id_3, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [ ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ rowArray_[0] ], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_3, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result[0].error, "TABLE UPDATE EXCEPTION (12010): No access to Delete a Row", "User can't delete Rows");
                done();
            });
        });
    });

    QUnit.test("User have access to R/W trying to submit with one cell", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_3).then(function () {
            var x = Math.floor((Math.random() * 10000000) + 1);
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User2_Member_Id_3, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [{ "id": -1, "rowId": rowArray_[2], "colId": columnArray_[3], "rowSequence": 3, "colSequence": 2, 
                    "cellValue": "NewDelhi_" + x, "cellFormula": "Sabarmati_", 
                    "active": true, "tid": -1, "changeFlag": 1, "access": -1 } ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ ], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_3, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.ok(exportTid_ < result.info.exportTid, "exportTid should be larger than the previous Submit operation.");
                SubmitRefreshUtils.assertInfo(result, assert);
                assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
                SubmitRefreshUtils.assertCells(result, assert);
                SubmitRefreshUtils.assertGridChangeBuffer(result, assert);
                importTid_1 = result.info.importTid;
                exportTid_1 = result.info.exportTid;
                done();
            });
        });
    });

    QUnit.test("User dont have access to add Column trying to submit with one column", function (assert) {
	//	console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_1).then(function () {
            var x = Math.floor((Math.random() * 10000000) + 1);
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User1_Member_Id_1, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [{ "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 4, "cellValue": "5", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 4, "cellValue": "7", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 4, "cellValue": "4", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[3], "colId": -1, "rowSequence": 3, "colSequence": 4, "cellValue": "9", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[4], "colId": -1, "rowSequence": 4, "colSequence": 4, "cellValue": "6", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[5], "colId": -1, "rowSequence": 5, "colSequence": 4, "cellValue": "8", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ ], 
                    "newColumnArray": [{"active": true, "id": -1, "name": "Area","previousColumnSequence": 3, 
                                    "previousColumnid": columnArray_[3],"seqNo": 4, "tid": -1}],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_1, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result[0].error, "TABLE UPDATE EXCEPTION (12010): No access to add a new Column", "User can't Add Column");
                done();
            });
        });
    });

    QUnit.test("User dont have access to Delete Column trying to submit with deleting column", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_1).then(function () {
            var x = Math.floor((Math.random() * 10000000) + 1);
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User1_Member_Id_1, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [],
                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],"deletedRowArray": [ ], "newColumnArray": [ ],
                    "deletedColumnArray": [ columnArray_[0] ], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_1, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.equal(result[0].error, "TABLE UPDATE EXCEPTION (12010): No access to Delete a Column", "User can't Delete Column");
                done();
            });
        });
    });

    QUnit.test("User have access to add Column trying to submit with one column", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_2).then(function () {
            var x = Math.floor((Math.random() * 10000000) + 1);
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User2_Member_Id_2, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [{ "id": -1, "rowId": rowArray_[0], "colId": -1, "rowSequence": 0, "colSequence": 4, "cellValue": "5", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[1], "colId": -1, "rowSequence": 1, "colSequence": 4, "cellValue": "7", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[2], "colId": -1, "rowSequence": 2, "colSequence": 4, "cellValue": "4", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[3], "colId": -1, "rowSequence": 3, "colSequence": 4, "cellValue": "9", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[4], "colId": -1, "rowSequence": 4, "colSequence": 4, "cellValue": "6", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                { "id": -1, "rowId": rowArray_[5], "colId": -1, "rowSequence": 5, "colSequence": 4, "cellValue": "8", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 8, "access": -1 },
                ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ ], 
                    "newColumnArray": [{"active": true, "id": -1, "name": "Area_" + x,"previousColumnSequence": 3, 
                                    "previousColumnid": columnArray_[3],"seqNo": 4, "tid": -1}],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_2, "PUT").then(function (result) {
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

    QUnit.test("User  have access to Delete Column trying to submit with deleting column", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
        importGrid(Access_Control.Row_Access_User_2).then(function () {
            var x = Math.floor((Math.random() * 10000000) + 1);
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User2_Member_Id_2, "importTid": importTid_, "exportTid": exportTid_,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [],
                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],"deletedRowArray": [ ], "newColumnArray": [ ],
                    "deletedColumnArray": [ columnArray_[4] ], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_2, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.ok(result.GridChangeBuffer.deletedColumnArray != null,"deletedColumnArray should not be null");
                assert.ok(Array.isArray(result.GridChangeBuffer.deletedColumnArray),"deletedColumnArray should be Array");
                done();
            });
        });
    });

    QUnit.test("User have access to R/W trying to submit with one cell with criticalUpdate", function (assert) {
		//console.log("========= " + assert.test.testName + "==============\n");
        var done=assert.async();
    
            var x = Math.floor((Math.random() * 10000000) + 1);
            var data = {
                "info": {
                    "collabId": Access_Control.Collab_Id, "wbId": Access_Control.WB_Id,
                    "memberId": Access_Control.User2_Member_Id_3, "importTid": importTid_1, "exportTid": exportTid_1,
                    "baselineId": -1, "criteriaTableId": -1, "view": "LATEST", "mode": 0
                },
                "cells": [{ "id": -1, "rowId": rowArray_[2], "colId": columnArray_[3], "rowSequence": 3, "colSequence": 2, 
                    "cellValue": "NewDelhi_" + x, "cellFormula": "Sabarmati_", 
                    "active": true, "tid": -1, "changeFlag": 1, "access": -1 } ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [ ],
                    "deletedRowArray": [ ], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + Access_Control.Access_Cuboid_Id, data, Access_Control.Row_Access_User_3, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
               assert.equal(result[0].error, "TABLE UPDATE EXCEPTION ((12017)","CriticalUpdate is there. Please refresh");
                done();
            });
    });

/*
    QUnit.test("User1 has Full Access and User2 has some criteria try to submit with Change Cell Values with user2 with Non-Accessible Value", function (assert) {
        var done=assert.async();
        importGrid().then(function () {
            var data = {
                "info": {
                    "collabId": CollaborationInput.Collab_Id_1, "wbId": WhiteboardInput.WB_Id,
                    "memberId": UserInput.Member_Id_2, "importTid": importTid_1, "exportTid": exportTid_1,
                    "baselineId": -1, "criteriaTableId": criteria_Table_Id, "view": "LATEST", "mode": 0
                },
                "cells": [
                    {
                        "id": -1, "rowId": rowArray_1[4], "colId": columnArray_1[3], "rowSequence": 2, "colSequence": 3,
                        "cellValue": "Delhi", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 2,
                        "access": -1
                    }
                ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [],
                    "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_Id, data, UserInput.authorization_1, "PUT").then(function (result) {
                var resultJSON = result;               
                assert.equal(result.error, "Access Filter violation ((12018)", "user Trying to submit unaccessible values");
                done();
            });
        });
    });
/*
    module("Column Access");

    QUnit.test("Changing value of accessible Columns", function (assert) {
        var done=assert.async();
        importGrid().then(function () {
            var data = {
                "info": {
                    "collabId": CollaborationInput.Collab_Id_1, "wbId": WhiteboardInput.WB_Id,
                    "memberId": UserInput.Member_Id_2, "importTid": importTid_1, "exportTid": exportTid_1,
                    "baselineId": -1, "criteriaTableId": criteria_Table_Id, "view": "LATEST", "mode": 0
                },
                "cells": [
                    {
                        "id": -1, "rowId": rowArray_1[4], "colId": columnArray_1[0], "rowSequence": 2, "colSequence": 3,
                        "cellValue": "125", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 2,
                        "access": -1
                    }
                ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [],
                    "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_Id, data, UserInput.authorization_1, "PUT").then(function (result) {
                assert.ok(result != null, "Response Should not be null");
                assert.ok(exportTid_ < result.info.exportTid, "exportTid should be larger than the previous Submit operation.");
                SubmitRefreshUtils.assertInfo(result, assert);
                assert.ok(Array.isArray(result.cells), "cells should be an array of objects !");
                SubmitRefreshUtils.assertCells(result, assert);
                SubmitRefreshUtils.assertGridChangeBuffer(result, assert);            
                done();
            });
        });
    });

    QUnit.test("Changing value of read only Columns Columns", function (assert) {
        var done=assert.async();
        importGrid().then(function () {
            var data = {
                "info": {
                    "collabId": CollaborationInput.Collab_Id_1, "wbId": WhiteboardInput.WB_Id,
                    "memberId": UserInput.Member_Id_2, "importTid": importTid_1, "exportTid": exportTid_1,
                    "baselineId": -1, "criteriaTableId": criteria_Table_Id, "view": "LATEST", "mode": 0
                },
                "cells": [
                    {
                        "id": -1, "rowId": rowArray_1[4], "colId": columnArray_1[2], "rowSequence": 2, "colSequence": 3,
                        "cellValue": "125", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 2,
                        "access": -1
                    }
                ],

                "columnArray": [], "columnCellArrays": [], "columns": [], "rows": [],
                "GridChangeBuffer": {
                    "newRowArray": [],
                    "deletedRowArray": [], "newColumnArray": [],
                    "deletedColumnArray": [], "criticalLevel": -1, "critical": 0
                },
                "rowArray": []
            }

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_Id, data, UserInput.authorization_1, "PUT").then(function (result) {             
                assert.ok(result[0].error, "TABLE UPDATE EXCEPTION (12016): You are trying to update a cell for which you do not have access", "User is Inserting Value in Read only column which is not accessible");               
                done();
            });
        });
    });*/
})(QUnit.module, QUnit.test);