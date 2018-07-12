function undecidedAssertEqual(assert, value, forNow, neat, message) {
    if (Globals.assertHow == "forNow")
        assert.equal(value, forNow, message);
    else
        assert.equal(value, neat, message);
}

//check whether number is > n
function isValidNumber(number, n) {
    if (!n)
        n = 0;
    return $.isNumeric(number) && number > n;
}

//Check whether there are 1 or more elements
function hasOneOrMoreElements(object) {
    return object.length > 0;
}

//Check whether object is null
function isNullObject(object) {
    return object === null;
}

//Check whether object has null attributes
function hasNullAttributes(object, props) {
    var hasNullAttributes = false;
    for (var i = 0; i < props.length; i++) {
        if (object[props[i]] == null)
            hasNullAttributes = true;
    }
    return hasNullAttributes;
}

//Check whether object has valid properties
function hasValidProperties(object, props) {
    var hasValidProps = true;
    for (var i = 0; i < props.length; i++) {
        if (!object.hasOwnProperty(props[i]))
            hasValidProps = false;
    }
    return hasValidProps;
}

var TestUtils = (function () {
    function sendRequest(URL, Data, authorization, requestType) {
		console.log("Request Type :"+requestType+"\n");
        var Response = new $.Deferred();
        var json;
        console.log("Fetching URL :" + URL + "\n");
        if (requestType.toString() == "GET") {
            $.ajax({
                url: URL,
                type: "GET",
                dataType: "json",
                headers: {
                    'Authorization': authorization,
                    'Accept': 'application/json'
                },
                success: function (result) {
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result) + "\n\n\n");
                    
                },
                error: function(err){
                    console.log("inside error function");
                    console.log(err);
                    Response.resolve(err);
                }
            });
        }
        if (requestType.toString() == "DELETE") {
            $.ajax({
                url: URL,
                type: "DELETE",
                dataType: "json",
                contentType: "application/json",
                headers: {
                    'Authorization': authorization,
                    'Accept': 'application/json'
                },
                async: false,
                success: function (result) {
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result) + "\n\n\n");
                    console.log("\n");
                },
                error: function(err){
                    console.log("inside error function");
                    console.log(err);
                    Response.resolve(err);
                }
            });
        }
        if (requestType.toString() == "POST" || requestType.toString() == "PUT") {
            $.ajax({
                url: URL,
                type: requestType,
                dataType: "json",
                data: JSON.stringify(Data),
                contentType: "application/json",
                headers: {
                    'Authorization': authorization,
                    'Accept': 'application/json'
                },
                success: function (result) {
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result) + "\n\n\n");
                },
                /*statusCode : {
                    500 : function() {
                        Response.resolve(500);
                    }
                }*/
                error: function(err){
                    console.log("inside error function");
                    console.log(err);
                    Response.resolve(err);
                }
            });
        }
        return Response.promise();
    }

    function sendPutRequest(URL, Data, authorization) {
		console.log("Request Type : PUT\n");
        var Response = new $.Deferred();
        console.log("Fetching :" + URL + "\n");
        var res = $.ajax({
            url: URL,
            type: "PUT",
            dataType: "json",
            data: JSON.stringify(Data),
            contentType: "application/json",
            headers: {
                'Authorization': authorization,
                'Accept': 'application/json'
            },
            async: false
        }).responseText;
        console.log("Response  : " + res + "\n\n\n");
        Response.resolve(res);
        return Response.promise();;
    }

    function sendDeleteRequest(URL, authorization) {
		console.log("Request Type : DELETE\n");
        var Response = new $.Deferred();   
        console.log("Fetching :" + URL + "\n");
        var res = $.ajax({
            url: URL,
            type: "DELETE",
            dataType: "json",
            contentType: "application/json",
            headers: {
                'Authorization': authorization,
                'Accept': 'application/json'
            },
            async: false
        }).responseText;
        console.log("Response  : " + res + "\n\n\n");
        Response.resolve(res);
        return Response.promise();;
    }

    function sendRequestMissingAuthorization(URL, Data, requestType) {
		console.log("Request Type :"+requestType+"\n");
        var Response = new $.Deferred();
        var json;
        console.log("Fetching :" + URL + "\n");
        if (requestType.toString() == "GET") {
            $.ajax({
                url: URL,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result) + "\n\n\n");
                   
                },
                error: function(err){
                    console.log("inside error function");
                    console.log(err);
                    Response.resolve(err);
                }
            });
        }
        if (requestType.toString() == "DELETE") {
            var Response = new $.Deferred();
            $.ajax({
                url: URL,
                type: "DELETE",
                dataType: "json",
                contentType: "application/json",                
                async: false,
                success: function (result) {
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result)+"\n\n\n");
                  
                },
                error: function(err){
                    console.log("inside error function");
                    console.log(err);
                    Response.resolve(err);
                }
            });
        }
        if (requestType.toString() == "POST" || requestType.toString() == "PUT") {
            $.ajax({
                url: URL,
                type: requestType,
                dataType: "json",
                data: JSON.stringify(Data),
                contentType: "application/json",
                success: function (result) {
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result) + "\n\n\n");
                    
                },
                error: function(err){
                    console.log("inside error function");
                    console.log(err);
                    Response.resolve(err);
                }
            });
        }
        return Response.promise();
    }

    return {
        sendPutRequest: sendPutRequest,
        sendRequest: sendRequest,
        sendDeleteRequest: sendDeleteRequest,
        sendRequestMissingAuthorization: sendRequestMissingAuthorization
    };
})();

var GridUtils = (function () {

    function assertGridInfo(info, assert, operation) {
        if (operation.toString() == "import") {
            assert.equal(typeof info.colCount, "number", "colCount Should be number");
            assert.ok(info.colCount >= 0, "colCount should be greater than 0 or equal to 0");
            assert.equal(typeof info.criteriaTableId, "number", "criteriaTableId should be number");
            assert.ok(info.criteriaTableId >= -1, "criteriaTableId should greater than 0 or if table is not present should be -1");
            assert.equal(typeof info.importTid, "number", "importTid should be number");
            assert.ok(info.importTid >= 1000, "importTid should be greater than 1000 or 1000");
            assert.equal(typeof info.maxTxId, "number", "maxTxId should be number");
            assert.ok(info.maxTxId >= 1000, "maxTxId should be greater than 1000 or 1000");
            assert.equal(typeof info.mode, "number", "mode should be number");
            assert.ok(info.mode == 1 || info.mode == 0, "mode should be 0 or 1");
            assert.equal(typeof info.neighborhoodHeirarchy, "object", "neighborhoodHeirarchy should be array");
            assert.equal(Object.keys(info.neighborhoodHeirarchy).length, 5, "neighborhoodHeirarchy should have 5 properties");
            assert.equal(typeof info.nhId, "number", "nhId should be number");
            assert.ok(info.nhId > 0, "nhId should be greater than 0");
            assert.equal(typeof info.purpose, "string", "purpose should be string");
            assert.equal(typeof info.rowCount, "number", "rowCount should be number");
            assert.ok(info.rowCount >= 0, "rowCount should be 0 or greater than 0");
            assert.equal(typeof info.userId, "number", "userId should be number");
            assert.ok(info.userId >= 1000, "userId should be greater than 1000 or 1000");
        }
        if (operation.toString() == "export") {
            assert.equal(typeof info.exportTid, "number", "exportTid should be number");
            //assert.ok(info.exportTid == -1, "exportTid should be greater than -1");
            assert.equal(typeof info.serverName, "string", "serverName should be number");
            assert.equal(typeof info.serverURL, "string", "serverURL should be number");
        }
        assert.equal(typeof info.collabId, "number", "collabId Should be number");
        assert.ok(info.collabId >= 1000, "collabId should be greater than 1000 or 1000");
        assert.equal(typeof info.id, "number", "id(cuboid id) should be number");
        assert.ok(info.id > 2000000, "id(cuboid id) greater than 2000000");
        assert.equal(typeof info.memberId, "number", "memberId should be number");
        assert.ok(info.memberId >= 1000, "memberId should be greater than 1000 or 1000");
        assert.equal(typeof info.exportTid, "number", "exportTid should be number");
        assert.ok(info.exportTid >= 1000, "exportTid should be greater than 1000 or 1000");
        assert.equal(typeof info.name, "string", "name(cuboid name) should be number");
        assert.ok(info.name != null, "name(cuboid name) should not be null");
        assert.equal(typeof info.view, "string", "view should be string");
        assert.equal(typeof info.wbId, "number", "wbId should be number");
        assert.ok(info.wbId >= 1000, "wbId should be greater than 1000 or 1000");

    }

    function assertColumnCellArrays(columnCellArrays, assert, columnLength, operation) {
        for (var i = 0; i < columnLength; i++) {
            if (operation.toString() == "import") {
                assert.ok(columnCellArrays[i].cellFormulas != null, "cellFormulas Element Should not be null of column " + (i + 1));
                assert.equal(typeof columnCellArrays[i].cellFormulas, "object", "cellFormulas Element should be object of column " + (i + 1));
                assert.ok(columnCellArrays[i].cellAccess != null, "cellAccess Element Should not be null of column " + (i + 1));
                assert.equal(typeof columnCellArrays[i].cellAccess, "object", "cellAccess Element should be object of column " + (i + 1));
                assert.ok(columnCellArrays[i].colSequence != null, "colSequence Element Should not be null of column " + (i + 1));
                assert.equal(typeof columnCellArrays[i].colSequence, "number", "colSequence Element should be number of column " + (i + 1));
                assert.ok(columnCellArrays[i].columnId != null, "columnId Element Should not be null of column " + (i + 1));
                assert.equal(typeof columnCellArrays[i].columnId, "number", "columnId Element should be number of column " + (i + 1));
            }
            assert.ok(columnCellArrays[i].cellValues != null, "cellValues Element Should not be null of column " + (i + 1));
            assert.equal(typeof columnCellArrays[i].cellValues, "object", "cellValues Element should be object of column " + (i + 1));
        }
    }

    function assertColumns(columns, assert, columnLength) {
        var previousColumnSequence = -1;
        var previousColumnid = -1;
        for (var i = 0; i < columnLength; i++) {
            assert.ok(columns[i].active != null, "active Element Should not be null of column " + (i + 1));
            assert.ok(columns[i].id != null, "id(column id) Element Should not be null of column " + (i + 1));
            assert.equal(typeof columns[i].id, "number", "id(column id) Element should be number of column " + (i + 1));
            assert.ok(columns[i].name != null, "name(column name) Element Should not be null of column " + (i + 1));
            assert.equal(typeof columns[i].name, "string", "name(column name) Element should be string of column " + (i + 1));
            assert.ok(columns[i].tid != null, "tid Element Should not be null of column " + (i + 1));
            assert.equal(typeof columns[i].tid, "number", "tid Element should be number of column " + (i + 1));
            assert.ok(columns[i].tid >= 1000, "tid should be greater than 1000 or 1000 of column " + (i + 1));

            assert.ok(columns[i].previousColumnid != null, "previousColumnid should not be null of column " + (i + 1));
            assert.equal(typeof columns[i].previousColumnid, "number", "previousColumnid should be number of column " + (i + 1));
            assert.equal(columns[i].previousColumnid, previousColumnid, "previousColumnid should be id od previous column of column " + (i + 1));
            previousColumnid = columns[i].id;

            assert.ok(columns[i].previousColumnSequence != null, "previousColumnSequence should not be null of column " + (i + 1));
            assert.equal(typeof columns[i].previousColumnSequence, "number", "previousColumnSequence should be number of column " + (i + 1));
            assert.equal(columns[i].previousColumnSequence, previousColumnSequence, "previousColumnSequence should be id od previous column of column " + (i + 1));
            previousColumnSequence = columns[i].seqNo;

            assert.ok(columns[i].seqNo != null, "seqNo should not be of column " + (i + 1));
            assert.equal(typeof columns[i].seqNo, "number", "previousColumnSequence should be number of column " + (i + 1));
        }
    }

    function assertRows(rows, assert, rowLength) {       
        for (var i = 0; i < rowLength; i++) {
            assert.ok(rows[i].active != null, "active Element Should not be null of row " + (i + 1));
            assert.ok(rows[i].id != null, "id(row id) Element Should not be null of row " + (i + 1));
            assert.equal(typeof rows[i].id, "number", "id(row id) Element should be number of row " + (i + 1));

            assert.ok(rows[i].tid != null, "tid Element Should not be null of row " + (i + 1));
            assert.equal(typeof rows[i].tid, "number", "tid Element should be number of row " + (i + 1));
            assert.ok(rows[i].tid >= 1000, "tid should be greater than 1000 or 1000 of row " + (i + 1));

            assert.ok(rows[i].previousRowid != null, "previousRowid should not be null of row " + (i + 1));
            assert.equal(typeof rows[i].previousRowid, "number", "previousRowid should be number of row " + (i + 1));
            
            assert.ok(rows[i].previousRowSequence != null, "previousRowSequence should not be null of row " + (i + 1));
            assert.equal(typeof rows[i].previousRowSequence, "number", "previousRowSequence should be number of row " + (i + 1));
            
            assert.ok(rows[i].seqNo != null, "seqNo should not be of row " + (i + 1));
            assert.equal(typeof rows[i].seqNo, "number", "previousColumnSequence should be number of row " + (i + 1));
        }
    }


    return {
        assertGridInfo: assertGridInfo,
        assertColumnCellArrays: assertColumnCellArrays,
        assertColumns: assertColumns,
        assertRows: assertRows
    };
})();

var SubmitRefreshUtils = (function(){

    function assertInfo(result, assert){
        assert.equal(8, Object.keys(result).length,"Response should strictly have 8 attributes !");
        assert.equal(22, Object.keys(result.info).length, "Info Object should strictly have 22 attributes !");
        assert.ok(result.info.id != null, "Id should not be null !");
        assert.ok(Number.isInteger(result.info.id), "Id should be an integer !");
        assert.ok(typeof result.info.name == 'string', "Name should be some string !");
        assert.ok(result.info.name != null,"name should not be null !");
        assert.ok(result.info.importTid != null,"Import id should not be null !");
        assert.ok(Number.isInteger(result.info.importTid),"Import id should be integer !");
        //assert.ok(Number.isInteger(result.info.exportTid),"Export id should be integer !");
        //assert.ok(result.info.exportTid != null,"Export id should not be null !");
        //assert.ok(Number.isInteger(result.info.asOfTid), "asOfTid should be integer !");
        assert.ok(Number.isInteger(result.info.collabId),"collabId should be integer !");
        assert.ok(Number.isInteger(result.info.wbId),"wbId should be integer !");
        assert.ok(Number.isInteger(result.info.memberId),"memberId should be integer !");
        assert.ok(Number.isInteger(result.info.rowCount),"rowCount should be integer !");
    }

    function assertCells(result, assert){
        for(var i=0; i < result.cells.length; i++){
            assert.equal(11, Object.keys(result.cells[i]).length, "Every changed cell should strictly have 11 properties !");
            assert.ok(result.cells[i].rowId != null,"Cell must have rowId !");
            assert.ok(Number.isInteger(result.cells[i].rowId),"rowId should be integer !");
            assert.ok(result.cells[i].colId != null,"Cell must have colId !");
            assert.ok(Number.isInteger(result.cells[i].colId),"colId should be integer !");
            //assert.ok(result.cells[i].rowSequence != null,"Cell must have rowSequence !");
            //assert.ok(Number.isInteger(result.cells[i].rowSequence),"rowSequence should be integer !");
            assert.ok(result.cells[i].cellValue != null,"cell value must not be null");
            //assert.ok(typeof result.cells[i].active == 'boolean',"active property must be of boolean type !");
            assert.ok(result.cells[i].tid != null,"Cell must have transaction id !");
            //assert.ok(Number.isInteger(result.cells[i].rowSequence),"transaction id should be integer !");
        }
    }

    function assertGridChangeBuffer(result, assert){
        assert.equal(6, Object.keys(result.GridChangeBuffer).length, "Grid change buffer should strictly contain 6 properties !");
        assert.ok(Array.isArray(result.GridChangeBuffer.newRowArray), "newRowArray should be of type array !");
        assert.ok(Array.isArray(result.GridChangeBuffer.deletedRowArray), "deletedRowArray should be of type array !");
        assert.ok(Array.isArray(result.GridChangeBuffer.newColumnArray), "newColumnArray should be of type array !");
        assert.ok(Array.isArray(result.GridChangeBuffer.deletedColumnArray), "deletedColumnArray should be of type array !");
    }

    function assertNewRowArray(newRowArray, assert){
        for(var i=0; i < newRowArray.length; i++){
            assert.equal(12, Object.keys(newRowArray[i]).length, "New Row Array should strictly contain 12 properties !");
            assert.ok(Number.isInteger(newRowArray[i].id),"Row Id Should be Integer");
            assert.ok(newRowArray[i].id > 0,"newRowArray should be positive Integer");
            assert.ok(Number.isInteger(newRowArray[i].seqNo),"seqNo Should be Integer");
            assert.ok(newRowArray[i].seqNo > 0,"seqNo should be positive Integer");
            assert.ok(Number.isInteger(newRowArray[i].previousRowid),"previousRowid Should be Integer");
            //assert.ok(newRowArray[i].previousRowid > 0,"previousRowid should be positive Integer");
            assert.ok(Number.isInteger(newRowArray[i].previousRowSequence),"previousRowSequence Should be Integer");
            assert.ok(Number.isInteger(newRowArray[i].tid),"tid Should be Integer");
            assert.ok(newRowArray[i].tid > 0,"tid should be positive Integer");
            assert.ok(Number.isInteger(newRowArray[i].ownerId),"ownerId Should be Integer");
            assert.ok(newRowArray[i].ownerId > 0,"ownerId should be positive Integer");
            assert.ok(Number.isInteger(newRowArray[i].creationTid),"creationTid Should be Integer");
            assert.ok(newRowArray[i].creationTid > 0,"creationTid should be positive Integer");
            assert.ok(Number.isInteger(newRowArray[i].createrId),"createrId Should be Integer");
            assert.ok(newRowArray[i].createrId > 0,"createrId should be positive Integer");
            assert.ok(Number.isInteger(newRowArray[i].ownershipAssignedTid),"ownershipAssignedTid Should be Integer");
            assert.ok(newRowArray[i].ownershipAssignedTid > 0,"ownershipAssignedTid should be positive Integer");
        }
    }

    function assertNewColumnArray(newColumnArray, assert){
        for(var i=0; i < newColumnArray.length; i++){
            assert.equal(7, Object.keys(newColumnArray[i]).length, "New Column Array should strictly contain 12 properties !");
            assert.ok(Number.isInteger(newColumnArray[i].id),"Column Id Should be Integer");
            assert.ok(newColumnArray[i].id > 0,"Column id should be positive Integer");
            assert.ok(Number.isInteger(newColumnArray[i].seqNo),"seqNo Should be Integer");
            assert.ok(Number.isInteger(newColumnArray[i].previousColumnid),"previousColumnid Should be Integer");
            assert.ok(Number.isInteger(newColumnArray[i].previousColumnSequence),"previousColumnSequence Should be Integer");
            assert.ok(Number.isInteger(newColumnArray[i].tid),"tid Should be Integer");
            assert.ok(newColumnArray[i].tid > 0,"tid should be positive Integer");
           // assert.ok(typeof newColumnArray.name == 'string', "Name should be some string !");
        }
    }

    return {
        assertInfo: assertInfo,
        assertCells : assertCells,
        assertGridChangeBuffer : assertGridChangeBuffer,
        assertNewRowArray : assertNewRowArray,
        assertNewColumnArray : assertNewColumnArray
    };
})();