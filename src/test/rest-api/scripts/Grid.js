(function( module, test ) {

    // Relation
    module("Grid");
    var cuboid_id=0;
    var cuboid_name="";
    var cuboid_id_=0;
    var cuboid_name_="";
    QUnit.test( "Creating Grid", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        cuboid_name="GOOD_CUBOID_"+x;    
		var data = {		
        "name":"GOOD_CUBOID_"+x,  
        "description": "CHECK FOR SUCCESS",  
        "wbId": Globals.Whiteboard_Id,  
        "collabId":Globals.Collab_Id,  
        "memberId": Globals.Member_Id_1,  
        "id": 0
        }
        TestUtils.sendPostRequest(Globals.baseURL + "rest/grid",data).then(function(result){
            cuboid_id=result.gridId;            
            var flag=false;                                
			if(result.collabId !=null && result.gridId != null && result.name !=null && result.memberId != null && result.wbId != null && result.description !=null) 
			{						
				flag=true;
			}                        
            assert.ok(result !=null,"Response should not be null");
            assert.ok(flag,"Should have 6 Properties or should not have any Property Null");
            assert.ok(result.gridId > 2000000,"Grid Created Successfully");
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
        TestUtils.sendPostRequest(Globals.baseURL + "rest/grid",data).then(function(result){
			var cString = result[0].error;
			var checkSubString = cString.indexOf("Violation of UNIQUE KEY constraint");
			assert.ok(checkSubString > 0,"Grid is already exists");
            assert.ok(result[0].error.includes("Violation of UNIQUE KEY constraint"),"Grid is already exists");
            done();
        });	
    });

    QUnit.test( "User having Multiple Memberships creating Grid using MEMBERSHIP-1 ", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 1000) + 1);
        cuboid_name_= "GOOD_CUBOID_"+x;
		var data = {		
        "name":"GOOD_CUBOID_"+x,  
        "description": "CHECK FOR SUCCESS",  
        "wbId": Globals.Whiteboard_Id,  
        "collabId": Globals.Collab_Id,  
        "memberId":  Globals.Member_Id_1,  
        "id": 0
        }
        
        TestUtils.sendPostRequest(Globals.baseURL + "rest/grid",data).then(function(result){
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
        TestUtils.sendPostRequest(Globals.baseURL + "rest/grid",data).then(function(result){
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
        TestUtils.sendPostRequest(Globals.baseURL + "rest/grid",data).then(function(result){
           var flag=false;
    
			var cString = result[0].error;
			var checkSubString1 = ("initial"+cString).indexOf("IsBlank");
			var checkSubString2 = ("initial"+cString).indexOf("IsNegative");
        
            if((checkSubString1 > 6) || (checkSubString2 > 6 ));                                                                 
            {
                flag=true;
            }
            assert.equal(result != null,true,"Response should not be null");
            assert.equal(flag,true,"Parameters passed are blank or negative");                
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
        TestUtils.sendPostRequest(Globals.baseURL + "rest/grid",data).then(function(result){
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
        TestUtils.sendPostRequest(Globals.baseURL + "rest/grid",data).then(function(result){
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
           TestUtils.sendPutRequestJson(Globals.baseURL + "rest/grid?gridId=" + cuboid_id,data).then(function(result){
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

        TestUtils.sendPutRequestJson(Globals.baseURL + "rest/grid?gridId=" + cuboid_id_,data).then(function(result){
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

        TestUtils.sendPutRequestJson(Globals.baseURL + "rest/grid?gridId=" + cuboid_id_,data).then(function(result){
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
        TestUtils.sendPutRequestJson(Globals.baseURL + "rest/grid?gridId=" + cuboid_id_,data).then(function(result){
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
        TestUtils.sendPutRequestJson(Globals.baseURL + "rest/grid?gridId=" + cuboid_id_,data).then(function(result){

            assert.ok(result != null,"Response Should not be null");
            assert.equal(result[0].error,"Missing element cells:[]","Missing element cells:[]");
            assert.equal(result[1].error,"Missing element rowArray:[]","Missing element rowArray:[]");
            assert.equal(result[2].error,"Missing element columnArray:[]","Missing element columnArray:[]");
            done();
       });		
    });
})( QUnit.module, QUnit.test );