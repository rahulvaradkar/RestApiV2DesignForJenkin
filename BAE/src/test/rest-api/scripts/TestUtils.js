function undecidedAssertEqual(assert, value, forNow, neat, message) {
    if(Globals.assertHow == "forNow") 
        assert.equal(value, forNow, message);
    else     
        assert.equal(value, neat, message);
}
  
//check whether number is > n
function isValidNumber(number, n) {
    if(!n)     
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
    for(var i = 0; i < props.length; i++) 
    {
        if(object[props[i]] == null)
            hasNullAttributes = true;
    }
    return hasNullAttributes;
}

//Check whether object has valid properties
function hasValidProperties(object, props) {
    var hasValidProps = true;
    for(var i = 0; i < props.length; i++) 
    {
        if(!object.hasOwnProperty(props[i])) 
            hasValidProps = false;      
    }
    return hasValidProps;
}

var TestUtils=(function(){
    function sendRequest(URL, Data, authorization, requestType) {
        var Response=new $.Deferred();
        var json;
        console.log("***********************************************************************************************");
        console.log("Fetching :" + URL);
        if(requestType.toString() == "GET") {
            $.ajax({
                url : URL,
                type : "GET",
                dataType : "json",
                headers : {
                    'Authorization': authorization,
                    'Accept': 'application/json'
                },
                success : function(result){
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result));
                    console.log("***********************************************************************************************");
                    console.log("\n");
                }
            });
        }
        if(requestType.toString() == "DELETE") {
            $.ajax({
                url : URL,
                type : "DELETE",
                dataType : "json",
                contentType : "application/json",
                headers : {
                    'Authorization': authorization,
                    'Accept': 'application/json'
                },
                async : false,
                success : function(result){			
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result));
                    console.log("***********************************************************************************************");
                    console.log("\n");
                }
            });  
        }
        if(requestType.toString() == "POST" || requestType.toString() == "PUT") {
            $.ajax({
                url : URL,
                type : requestType,
                dataType : "json",
                data : JSON.stringify(Data),
                contentType : "application/json",
                headers : {
                    'Authorization': authorization,
                    'Accept': 'application/json'
                },
                success : function(result){
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result));
                    console.log("***********************************************************************************************");
                    console.log("\n");
                }
            });
        }
		return Response.promise();
    }

    function sendPutRequest(URL,Data,authorization) {
        var Response=new $.Deferred();
        console.log("***********************************************************************************************");
        console.log("Fetching :" + URL);
        var res = $.ajax({
            url : URL,
			type : "PUT",
			dataType : "json",
			data : JSON.stringify(Data),
            contentType : "application/json",
            headers : {
                'Authorization': authorization,
                'Accept': 'application/json'
            },
			async : false
        }).responseText;
        console.log("Response  : " + res);
        console.log("***********************************************************************************************");
        console.log("\n");
        Response.resolve(res);
		return Response.promise();;
    }
    
    function sendDeleteRequest(URL, authorization) {
        var Response=new $.Deferred();
        console.log("***********************************************************************************************");
        console.log("Fetching :" + URL);
        var res = $.ajax({
            url : URL,
			type : "DELETE",
			dataType : "json",
            contentType : "application/json",
            headers : {
                'Authorization': authorization,
                'Accept': 'application/json'
            },
			async : false
        }).responseText;
        console.log("Response  : " + res);
        console.log("***********************************************************************************************");
        console.log("\n");
        Response.resolve(res);
		return Response.promise();;
    }    

    function sendRequestMissingAuthorization(URL,Data,requestType) {
        var Response=new $.Deferred();
        var json;
        console.log("***********************************************************************************************");
        console.log("Fetching :" + URL);
        if(requestType.toString() == "GET") {          
			$.ajax({
				url : URL,
		        type : "GET",
                dataType : "json",
		        success : function(result){
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result));
                    console.log("***********************************************************************************************");
                    console.log("\n");
				}
			});
        }
        if(requestType.toString() == "DELETE") {
            var Response=new $.Deferred();
            $.ajax({
                url : URL,
			    type : "DELETE",
			    dataType : "json",
                contentType : "application/json",
                async : false,
                dataType : "json",
		        success : function(result){
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result));
                    console.log("***********************************************************************************************");
                    console.log("\n");
				}
            });
        }
        if(requestType.toString() == "POST" || requestType.toString() == "PUT") {
			$.ajax({
                url : URL,
				type : requestType,
				dataType : "json",
				data : JSON.stringify(Data),
                contentType : "application/json",
				success : function(result){
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result));
                    console.log("***********************************************************************************************");
                    console.log("\n");
				}
			});
        }        
        return Response.promise();
    }   

    return  {   
        sendPutRequest: sendPutRequest,
        sendRequest : sendRequest,
        sendDeleteRequest: sendDeleteRequest,
        sendRequestMissingAuthorization : sendRequestMissingAuthorization
    };
})();

var GridUtils=(function(){

    function assertGridInfo(info, assert, operation) {
        if(operation.toString() == "import") {
            assert.equal(typeof info.colCount, "number", "colCount Should be number");
            assert.ok(info.colCount >= 0,"colCount should be greater than 0 or equal to 0");
            assert.equal(typeof info.criteriaTableId, "number", "criteriaTableId should be number");
            assert.ok(info.criteriaTableId >= -1,"criteriaTableId should greater than 0 or if table is not present should be -1");
            assert.equal(typeof info.importTid, "number", "exportTid should be number");
            assert.ok(info.importTid > 1000, "exportTid should be greater than 1000");
            assert.equal(typeof info.maxTxId, "number", "exportTid should be number");
            assert.ok(info.maxTxId > 1000, "exportTid should be greater than 1000");
            assert.equal(typeof info.mode, "number", "mode should be number");
            assert.ok(info.mode == 1 || info.mode == 0, "mode should be 0 or 1");
            assert.equal(typeof info.neighborhoodHeirarchy, "object", "neighborhoodHeirarchy should be array");
            assert.equal(Object.keys(info.neighborhoodHeirarchy).length, 5,"neighborhoodHeirarchy should have 5 properties");
            assert.equal(typeof info.nhId, "number", "nhId should be number");
            assert.ok(info.nhId > 0, "nhId should be greater than 0");
            assert.equal(typeof info.purpose, "string", "purpose should be string");
            assert.equal(typeof info.rowCount, "number", "rowCount should be number");
            assert.ok(info.rowCount >= 0, "rowCount should be 0 or greater than 0");
            assert.equal(typeof info.userId, "number", "userId should be number");
            assert.ok(info.userId > 1000, "userId should be greater than 1000");
        }
        if(operation.toString() == "export") {
            assert.equal(typeof info.importTid, "number", "exportTid should be number");
            assert.ok(info.importTid == -1, "exportTid should be greater than -1");
            assert.equal(typeof info.serverName, "string", "serverName should be number");
            assert.equal(typeof info.serverURL, "string", "serverURL should be number");
        }
        assert.equal(typeof info.collabId, "number", "collabId Should be number");
        assert.ok(info.collabId > 1000,"collabId should be greater than 1000");
        assert.equal(typeof info.id, "number", "id(cuboid id) should be number");
        assert.ok(info.id > 2000000,"id(cuboid id) greater than 2000000");
        assert.equal(typeof info.memberId, "number", "memberId should be number");
        assert.ok(info.memberId > 1000, "memberId should be greater than 1000");
        assert.equal(typeof info.exportTid, "number", "exportTid should be number");
        assert.ok(info.exportTid > 1000, "exportTid should be greater than 1000");
        assert.equal(typeof info.name, "string", "name(cuboid name) should be number");
        assert.ok(info.name != null,"name(cuboid name) should not be null");
        assert.equal(typeof info.view, "string", "view should be string");
        assert.equal(typeof info.wbId, "number", "wbId should be number");
        assert.ok(info.wbId > 1000, "wbId should be greater than 1000");

    }

    function assertColumnCellArrays(columnCellArrays, assert, columnLength, operation) {
        for(var i = 0; i < columnLength; i++) {
            if(operation.toString() == "import") {
                assert.ok(columnCellArrays[i].cellFormulas != null,"cellFormulas Element Should not be null of column "+ (i+1));
                assert.equal(typeof columnCellArrays[i].cellFormulas, "object", "cellFormulas Element should be object of column "+ (i+1));
                assert.ok(columnCellArrays[i].cellAccess != null,"cellAccess Element Should not be null of column "+ (i+1));
                assert.equal(typeof columnCellArrays[i].cellAccess, "object", "cellAccess Element should be object of column "+ (i+1));
                assert.ok(columnCellArrays[i].colSequence != null,"colSequence Element Should not be null of column "+ (i+1));
                assert.equal(typeof columnCellArrays[i].colSequence, "number", "colSequence Element should be number of column "+ (i+1));
                assert.ok(columnCellArrays[i].columnId != null,"columnId Element Should not be null of column "+ (i+1));
                assert.equal(typeof columnCellArrays[i].columnId, "number", "columnId Element should be number of column "+ (i+1));
            }
            assert.ok(columnCellArrays[i].cellValues != null,"cellValues Element Should not be null of column "+ (i+1));
            assert.equal(typeof columnCellArrays[i].cellValues, "object", "cellValues Element should be object of column "+ (i+1));           
        }
    }

    function assertColumns(columns, assert, columnLength) {
        var  previousColumnSequence = -1;
        var previousColumnid = -1;
        for(var i = 0; i < columnLength; i++) {
            assert.ok(columns[i].active != null,"active Element Should not be null of column "+ (i+1));
            assert.ok(columns[i].id != null,"id(column id) Element Should not be null of column "+ (i+1));
            assert.equal(typeof columns[i].id, "number", "id(column id) Element should be number of column "+ (i+1));
            assert.ok(columns[i].name != null,"name(column name) Element Should not be null of column "+ (i+1));
            assert.equal(typeof columns[i].name, "string", "name(column name) Element should be string of column "+ (i+1));
            assert.ok(columns[i].tid != null,"tid Element Should not be null of column "+ (i+1));
            assert.equal(typeof columns[i].tid, "number", "tid Element should be number of column "+ (i+1));
            assert.ok(columns[i].tid > 1000, "tid should be greater than 1000 of column "+ (i+1));
            
            assert.ok(columns[i].previousColumnid != null,"previousColumnid should not be null of column "+ (i+1));
            assert.equal(typeof columns[i].previousColumnid, "number", "previousColumnid should be number of column "+ (i+1));
            assert.equal(columns[i].previousColumnid, previousColumnid,"previousColumnid should be id od previous column of column "+ (i+1));
            previousColumnid=columns[i].id;

            assert.ok(columns[i].previousColumnSequence != null,"previousColumnSequence should not be null of column "+ (i+1));
            assert.equal(typeof columns[i].previousColumnSequence, "number", "previousColumnSequence should be number of column "+ (i+1));
            assert.equal(columns[i].previousColumnSequence, previousColumnSequence,"previousColumnSequence should be id od previous column of column "+ (i+1));
            previousColumnSequence=columns[i].seqNo;

            assert.ok(columns[i].seqNo != null, "seqNo should not be of column "+ (i+1));
            assert.equal(typeof columns[i].seqNo, "number", "previousColumnSequence should be number of column "+ (i+1));
        }
    }

    function assertRows(rows, assert, rowLength) {
        var  previousRowSequence = -1;
        var previousRowid = -1;
        for(var i = 0; i < rowLength; i++) {
            assert.ok(rows[i].active != null,"active Element Should not be null of row "+ (i+1));
            assert.ok(rows[i].id != null,"id(row id) Element Should not be null of row "+ (i+1));
            assert.equal(typeof rows[i].id, "number", "id(row id) Element should be number of row "+ (i+1));        
            
            assert.ok(rows[i].tid != null,"tid Element Should not be null of row "+ (i+1));
            assert.equal(typeof rows[i].tid, "number", "tid Element should be number of row "+ (i+1));
            assert.ok(rows[i].tid > 1000, "tid should be greater than 1000 of row "+ (i+1));
            
            assert.ok(rows[i].previousRowid != null,"previousRowid should not be null of row "+ (i+1));
            assert.equal(typeof rows[i].previousRowid, "number", "previousRowid should be number of row "+ (i+1));
            assert.equal(rows[i].previousRowid, previousRowid,"previousRowid should be id od previous row of row "+ (i+1));
            previousRowid=rows[i].id;

            assert.ok(rows[i].previousRowSequence != null,"previousRowSequence should not be null of row "+ (i+1));
            assert.equal(typeof rows[i].previousRowSequence, "number", "previousRowSequence should be number of row "+ (i+1));
            assert.equal(rows[i].previousRowSequence, previousRowSequence,"previousRowSequence should be id od previous row of row "+ (i+1));
            previousRowSequence=rows[i].seqNo;

            assert.ok(rows[i].seqNo != null, "seqNo should not be of row "+ (i+1));
            assert.equal(typeof rows[i].seqNo, "number", "previousColumnSequence should be number of row "+ (i+1));
        }
    }


    return {
        assertGridInfo : assertGridInfo,
        assertColumnCellArrays : assertColumnCellArrays,
        assertColumns : assertColumns,
        assertRows : assertRows
    };
})();