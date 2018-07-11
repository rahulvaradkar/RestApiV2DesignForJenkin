(function (module, test) {

    module("Row Access");

    var cuboid_Id = 2003247;
    var criteria_Table_Id = 2003248;
    var importTid_ = 0;
    var exportTid_ = 0;
    var rowArray_;
    var columnArray_;
    var importTid_1 = 0;
    var exportTid_1 = 0;
    var rowArray_1;
    var columnArray_1;

    function importGrid() {
        var promise = new $.Deferred();
        TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_Id + "?importTid=-1&view=LATEST&mode=1&baselineId=-1", null, UserInput.authorization, "GET").then(function (result) {
            importTid_ = result.info.importTid;
            exportTid_ = result.info.exportTid;
            columnArray_ = result.columnArray;
            rowArray_ = result.rowArray;
            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid/" + cuboid_Id + "?importTid=-1&view=LATEST&mode=1&baselineId=-1", null, UserInput.authorization_1, "GET").then(function (result) {
                importTid_1 = result.info.importTid;
                exportTid_1 = result.info.exportTid;
                columnArray_1 = result.columnArray;
                rowArray_1 = result.rowArray;
                promise.resolve();
            });            
        });
        return promise.promise();
    }

    QUnit.test("User1 has Full Access and User2 has some criteria try to submit with new rows with user2 with Accessible Value", function (assert) {
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
                        "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 2, "colSequence": 0,
                        "cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,
                        "access": -1
                    },
                    {
                        "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 2, "colSequence": 1,
                        "cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,
                        "access": -1
                    },
                    {
                        "id": -1, "rowId": -1, "colId": columnArray_[2], "rowSequence": 2, "colSequence": 2,
                        "cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,
                        "access": -1
                    },
                    {
                        "id": -1, "rowId": -1, "colId": columnArray_[3], "rowSequence": 2, "colSequence": 3,
                        "cellValue": "Mumbai", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,
                        "access": -1
                    }
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

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_Id, data, UserInput.authorization_1, "PUT").then(function (result) {
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

    QUnit.test("User1 has Full Access and User2 has some criteria try to submit with new rows with user2 with Non-Accessible Value", function (assert) {
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
                        "id": -1, "rowId": -1, "colId": columnArray_[0], "rowSequence": 2, "colSequence": 0,
                        "cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,
                        "access": -1
                    },
                    {
                        "id": -1, "rowId": -1, "colId": columnArray_[1], "rowSequence": 2, "colSequence": 1,
                        "cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,
                        "access": -1
                    },
                    {
                        "id": -1, "rowId": -1, "colId": columnArray_[2], "rowSequence": 2, "colSequence": 2,
                        "cellValue": "123", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,
                        "access": -1
                    },
                    {
                        "id": -1, "rowId": -1, "colId": columnArray_[3], "rowSequence": 2, "colSequence": 3,
                        "cellValue": "Delhi", "cellFormula": "", "active": true, "tid": -1, "changeFlag": 4,
                        "access": -1
                    }
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

            TestUtils.sendRequest(Globals.baseURL + "rest/v1/grid?gridId=" + cuboid_Id, data, UserInput.authorization_1, "PUT").then(function (result) {
                var resultJSON = result;               
                assert.equal(result[0].error, "Access Filter violation ((12018)", "user Trying to submit unaccessible values");
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