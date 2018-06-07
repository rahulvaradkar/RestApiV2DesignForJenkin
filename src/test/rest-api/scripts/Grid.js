/**
 * Author : Jaydeep Bobade Tekvision
 * Module : Grid
 */

(function( module, test ) {
    
    module("Grid");
    var cuboid_id=0;
    var cuboid_name="";
    var cuboid_id_=0;
    var cuboid_name_="";
    QUnit.test( "Creating Grid", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000000) + 1);
        cuboid_name="GOOD_CUBOID_"+x;    
		var data = {		
        "name":"GOOD_CUBOID_"+x,  
        "description": "CHECK FOR SUCCESS",  
        "wbId": Globals.Whiteboard_Id,  
        "collabId":Globals.Collab_Id,  
        "memberId": Globals.Member_Id_1,  
        "id": 0
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/grid", data, Globals.authorization, "POST").then(function(result){
            cuboid_id=result.gridId;            
            var flag=false;                                
			if(result.collabId !=null && result.gridId != null && result.name !=null && result.memberId != null && result.wbId != null && result.description !=null) 
			{						
				flag=true;
            }
            assert.ok(result != null, "Response should not be null");

            assert.equal(typeof result.collabId, "number", "Collab Id should be number");
            assert.ok(result.collabId > 1000, "Collab should greater than 1000");
            assert.equal(typeof result.gridId, "number", "Grid Id Id should be number");
            assert.ok(result.gridId > 2000000,"Grid Id should greater than 2000000");
            assert.equal(typeof result.memberId, "number", "Member should be number");
            assert.ok(result.memberId > 1000,"Member Id should greater than 1000");
            assert.equal(typeof result.wbId, "number", "Whiteboard Id should be number");
            assert.ok(result.wbId > 1000,"Whiteboard Id should greater than 1000");
            assert.equal(typeof result.description, "string", "description should be String");
            assert.equal(typeof result.name, "string", "Name should be String");
            
            assert.ok(flag,"Should have 6 Properties or should not have any Property Null");           
            done();
        })
      });
      
    QUnit.test( "Grid already exists", function( assert ) {
		var done = assert.async();
		var data = {		
            "name":"GOOD_CUBOID",  
            "description": "CHECK FOR SUCCESS",  
            "wbId": Globals.Whiteboard_Id,  
            "collabId": Globals.Collab_Id,  
            "memberId":  Globals.Member_Id_1,  
            "id": 0
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/grid", data, Globals.authorization, "POST").then(function(result){           
            assert.ok(result[0].error,"SQLException:2627, Cause:Violation of UNIQUE KEY constraint 'UQ_NC_BW_TBL'. Cannot insert duplicate key in object 'dbo.BW_TBL'.","Grid is already exists");
            done();
        });	
    });

    QUnit.test( "User having Multiple Memberships creating Grid using MEMBERSHIP-1 ", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 200000) + 1);
        cuboid_name_= "GOOD_CUBOID_"+x;
		var data = {		
        "name":"GOOD_CUBOID_"+x,  
        "description": "CHECK FOR SUCCESS",  
        "wbId": Globals.Whiteboard_Id,  
        "collabId": Globals.Collab_Id,
        "memberId":  Globals.Member_Id_1,  
        "id": 0
        }
        
        TestUtils.sendRequest(Globals.baseURL + "rest/grid", data, Globals.authorization, "POST").then(function(result){
            var flag=false;
            cuboid_id_=result.gridId;
            if(result.collabId !=null && result.gridId != null && result.name !=null && result.memberId != null && result.wbId != null && result.description !=null) 
            {						
                flag=true;
            }                                 
            assert.ok(result !=null,"Response should not be null");
            assert.ok(flag,"Should have 6 Properties or should not have any Property Null");
            assert.ok(result.gridId > 2000000,"Grid CReated Successfully");
            done();
        });			
    });

    QUnit.test( "User having Multiple Memberships creating Grid using MEMBERSHIP-2", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000) + 1);
		var data = {		
        "name":"GOOD_CUBOID_"+x,  
        "description": "CHECK FOR SUCCESS",  
        "wbId": Globals.Whiteboard_Id,  
        "collabId": Globals.Collab_Id,  
        "memberId":  Globals.Member_Id_2,  
        "id": 0
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/grid", data, Globals.authorization, "POST").then(function(result){
            var flag=false;
            if(result.collabId !=null && result.gridId != null && result.name !=null && result.memberId != null && result.wbId != null && result.description !=null)
            {
                flag=true;
            }
            assert.ok(result !=null,"Response should not be null");
            assert.ok(flag,"Should have 6 Properties or should not have any Property Null");
            assert.ok(result.gridId > 2000000,"Grid CReated Successfully");
            done();
        });
    });

    QUnit.test( "Invalid Input Checks. Negative Numbers and Blank Values in required fields", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000) + 1);
		var data = {
        "name":"",
        "description": "",
        "wbId": -1002,
        "collabId": -1001,
        "memberId": -1007,
        "id": 0
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/grid", data, Globals.authorization,"POST").then(function(result){           
            assert.ok(result != null,"Response should not be null");
            assert.equal(result[0].error,"IsNegative","Collab Id is Blank");
            assert.equal(result[1].error,"IsNegative","Whiteboard Id is Blank");
            assert.equal(result[3].error,"IsBlank","Name is Blank");
            assert.equal(result[2].error,"IsBlank","description is Blank");                  
            done();
        });
    });

      QUnit.test( "InValid Collaboration ID", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000) + 1);
		var data = {		
        "name":"GOOD_CUBOID_"+x,  
        "description": "CHECK FOR SUCCESS",  
        "wbId": Globals.Whiteboard_Id,  
        "collabId": 9999,  
        "memberId":  Globals.Member_Id_1,  
        "id": 0
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/grid", data, Globals.authorization, "POST").then(function(result){
            assert.ok(result !=null,"Response should not be null");               
            assert.equal(result[0].error,"Collaboration ID NOT FOUND","InValid Collaboration ID");
            done();
        });
    });

      QUnit.test( "InValid Whiteboard ID", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 100000) + 1);
		var data = {		
        "name":"GOOD_CUBOID_"+x,  
        "description": "CHECK FOR SUCCESS",
        "wbId": 9999,
        "collabId": Globals.Collab_Id,
        "memberId":  Globals.Member_Id_1,
        "id": 0
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/grid", data, Globals.authorization, "POST").then(function(result){
            assert.ok(result !=null,"Response should not be null");
            assert.equal(result[0].error,"Whitebaord ID NOT FOUND","InValid Whiteboard ID");
            done();
        });	
      });

    QUnit.test("Exporting Grid", function( assert ) {
        var done = assert.async();       
		var data = {
            "cells": [],
            "columnArray": [-1],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},
            "rowArray": [0],
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }
           TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id, data, Globals.authorization, "PUT").then(function(result){
                var resultJSON=result;
                assert.equal(Object.keys(resultJSON).length, 8, "cuboid object should have only 8 properties !");
                assert.ok(Array.isArray(resultJSON.cells), "cells should be an Array !");
                assert.ok(Array.isArray(resultJSON.columnArray), "columnArray should be an Array !");
                assert.ok(Array.isArray(resultJSON.columnCellArrays), "columns cells should be an Array !");
                assert.ok(Array.isArray(resultJSON.columns), "columns property should be an Array !");
                assert.ok(Array.isArray(resultJSON.rows), "rows should be an Array !");
                assert.ok(Array.isArray(resultJSON.rowArray), "rowArray should be an Array !");
                done();
           });
    });

    QUnit.test("Exporting Grid with Missing Authorization", function( assert ) {
        var done = assert.async();       
		var data = {
            "cells": [],
            "columnArray": [-1],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},
            "rowArray": [0],
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }
           TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/grid?gridId=" + cuboid_id, data, "PUT").then(function(result){
                assert.ok(result != null , "Response should not be null")
                assert.equal(result[0].error, "Missing Authorization in Header", "Missing Authorization in Header")			
                done();
           });
    });

    QUnit.test("Exporting Grid with Invalid Authorization", function( assert ) {
        var done = assert.async();       
		var data = {
            "cells": [],
            "columnArray": [-1],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},
            "rowArray": [0],
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }
           TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id, data, Globals.invalidAuthorization, "PUT").then(function(result){
                assert.ok(result != null , "Response should not be null")
                assert.equal(result[0].error, "Invalid Authorization. User is not a member of Neighborhood Path.", "Invalid Authorization. User is not a member of Neighborhood Path.")			
                done();
           });
    });

    QUnit.test("Exporting Grid with Duplicate Column names", function( assert ) {
        var done = assert.async();       
		var data = {
            "cells": [],
            "columnArray": [-1],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},
            "rowArray": [0],
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }
           TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id, data, Globals.authorization,"PUT").then(function(result){
                assert.ok(result != null , "Response should not be null")
                assert.equal(result[0].error, "SQLException:2627, Cause:Violation of UNIQUE KEY constraint 'UQ_NC_BW_COLUMN'. Cannot insert duplicate key in object 'dbo.BW_COLUMN'.", "Duplicate Column Names")			           
                done();
           });
    });

    QUnit.test("Missing columns element while Exporting Grid", function( assert ) {
        var done = assert.async();       
		var data = {
            "cells": [],
            "columnArray": [-1],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "GridChangeBuffer": {},
            "rowArray": [0],
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }
           TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id, data, Globals.authorization, "PUT").then(function(result){
            assert.ok(result != null,"Response Should not be null");
            assert.equal(result[0].error,"Missing element columns:[]","Missing element columns:[]");
            done();
           });
    });

    QUnit.test("Missing Cell element while Exporting Grid", function( assert ) {
        var done = assert.async();       
		var data = {
            "columnArray": [-1],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},
            "rowArray": [0],
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name_,
             "serverName": "serverName",
             "id": cuboid_id_,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
        }   

        TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id_, data, Globals.authorization, "PUT").then(function(result){
            assert.ok(result != null,"Response Should not be null");
            assert.equal(result[0].error,"Missing element cells:[]","Missing element cells:[]");
            done();
       });		
    });

    QUnit.test("Missing Row element while Exporting Grid", function( assert ) {
        var done = assert.async();       
		var data = {
            "cells": [],
            "columnArray": [-1],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},            
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }

        TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id_, data, Globals.authorization, "PUT").then(function(result){
            assert.ok(result != null,"Response Should not be null");
            assert.equal(result[0].error,"Missing element rowArray:[]","Missing element rowArray:[]");
            done();
       });		
    });

    QUnit.test("Missing rowArray while Exporting Grid", function( assert ) {
        var done = assert.async();       
		var data = {
            "cells": [],
            "columnArray": [-1],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},           
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }
           TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id, data, Globals.authorization,"PUT").then(function(result){
                assert.ok(result != null,"Response Should not be null");
                assert.equal(result[0].error,"Missing element rowArray:[]","Missing element rowArray:[]");
                done();
           });
    });

    QUnit.test("Missing columnArray element while Exporting Grid", function( assert ) {
        var done = assert.async();       
		var data = {
            "cells": [],
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},
            "rowArray": [0],
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }
        TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id_, data, Globals.authorization, "PUT").then(function(result){
            assert.ok(result != null,"Response Should not be null");
            assert.equal(result[0].error,"Missing element columnArray:[]","Missing element columnArray:[]");
            done();
       });		
    });

    QUnit.test("Missing cells, rowArray and columnArray element while Exporting Grid", function( assert ) {
        var done = assert.async();       
		var data = {
            "columnCellArrays": [{"cellValues": ["Maharashtra"],"columnId": -1},{"cellValues": ["Pune"],"columnId": -1}],
            "columns": [{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
            {"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
            ],
            "GridChangeBuffer": {},           
            "rows": [{"seqNo":1,"active": true,"previousRowSequence": -1,"id": -1,"previousRowid": -1,"tid": -1}],
            "info": {
             "filter": "filter",
             "view": "view",
             "asOfTid":-1,
             "neighborhoodHeirarchy": {
              "nh_level_2": -1,
              "nh_level_3": -1,
              "nh_Level_0": -1,
              "nh_Level_1": -1,
              "levels": 4
             },
             "exportTid":-1,
             "serverURL": "serverURL",
             "name": cuboid_name,
             "serverName": "serverName",
             "id": cuboid_id,
             "importTid": -1,
             "collabId": Globals.Collab_Id,
             "wbId": Globals.Whiteboard_Id,
             "memberId":  Globals.Member_Id_1,
             "baselineId": -1
            }
           }
        TestUtils.sendRequest(Globals.baseURL + "rest/grid?gridId=" + cuboid_id_, data, Globals.authorization, "PUT").then(function(result){
            assert.ok(result != null,"Response Should not be null");
            assert.equal(result[0].error,"Missing element cells:[]","Missing element cells:[]");
            assert.equal(result[1].error,"Missing element rowArray:[]","Missing element rowArray:[]");
            assert.equal(result[2].error,"Missing element columnArray:[]","Missing element columnArray:[]");
            done();
       });		
    });


    QUnit.test("Importing Grid",function(assert){
        var done=assert.async();
        TestUtils.sendRequest(Globals.baseURL+"rest/grid/"+cuboid_id+"?importTid="+Globals.importTid+"&view=LATEST&mode=1&baselineId=-1",null, Globals.authorization, "GET").then(function(result){
            assert.ok(result != null, "Response Should not be null");
            assert.ok(result.cells != null,"Cells  Element Should not be null");
            assert.ok(result.columnArray != null,"columnArray Element Should not be null");
            assert.ok(result.columnCellArrays != null,"columnCellArrays Element Should not be null");          
            for(var i = 0; i < result.columnArray.length; i++)
            {
                assert.ok(result.columnCellArrays[i].cellFormulas != null,"cellFormulas Element Should not be null");
                assert.ok(result.columnCellArrays[i].cellAccess != null,"cellAccess Element Should not be null");
                assert.ok(result.columnCellArrays[i].cellValues != null,"cellValues Element Should not be null");
                assert.ok(result.columnCellArrays[i].colSequence != null,"colSequence Element Should not be null");
                assert.ok(result.columnCellArrays[i].columnId != null,"columnId Element Should not be null");
            }
            assert.ok(result.gridChangeBuffer != null,"gridChangeBuffer Element Should not be null");
            assert.ok(result.info != null,"info Element Should not be null");
            assert.ok(result.rowArray != null,"rowArray Element Should not be null");
            assert.ok(result.rows != null,"rows Element Should not be null");
            done();
        });       
    });

    QUnit.test("Importing Grid with Invalid Authorization",function(assert){
        var done=assert.async();
        TestUtils.sendRequest(Globals.baseURL+"rest/grid/"+cuboid_id+"?importTid="+Globals.importTid+"&view=LATEST&mode=1&baselineId=-1",null, Globals.invalidAuthorization, "GET").then(function(result){               
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "Invalid Authorization. User is not a member of Neighborhood Path.","Invalid Authorization. User is not a member of Neighborhood Path.");
            done();
        });
    });

    QUnit.test("Importing Grid with Missing Authorization",function(assert){
        var done=assert.async();
        TestUtils.sendRequestMissingAuthorization(Globals.baseURL+"rest/grid/"+cuboid_id+"?importTid="+Globals.importTid+"&view=LATEST&mode=1&baselineId=-1", null, "GET").then(function(result){               
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "Missing Authorization in Header","Missing Authorization in Header");
            done();
        });
    });

    QUnit.test("Importing Grid with ImportTid is Missing",function(assert){
        var done=assert.async();
        TestUtils.sendRequest(Globals.baseURL+"rest/grid/"+cuboid_id+"?view=LATEST&mode=1&baselineId=-1",null, Globals.authorization, "GET").then(function(result){
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "importTid is missing in GET Request","importTid is missing in GET Request");
            done();
        });
    });

    QUnit.test("Importing Grid with Wrong username and wrong Password",function(assert){
        var done=assert.async();
        TestUtils.sendRequest(Globals.baseURL+"rest/grid/"+cuboid_id+"?importTid="+Globals.importTid+"&view=LATEST&mode=1&baselineId=-1",null, getAuthorization("nhj",8,"JSAddin"), "GET").then(function(result){
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "Authentication_Connection_Failure","Enter Correct Username and Password");
            done();
        });
    });

    QUnit.test("Importing Grid with Wrong authorization format",function(assert){
        var done=assert.async();
        TestUtils.sendRequest(Globals.baseURL+"rest/grid/"+cuboid_id+"?importTid="+Globals.importTid+"&view=LATEST&mode=1&baselineId=-1",null, getAuthorization("j",0,""), "GET").then(function(result){
            assert.ok(result != null, "Response should not be null");
            assert.equal(result[0].error, "Invalid Authorization Format","Check Authorization Format");
            done();
        });
    });

    
})( QUnit.module, QUnit.test );