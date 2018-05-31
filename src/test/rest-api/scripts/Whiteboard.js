(function( module, test ) {

    module('Whiteboard');
    QUnit.test("Reading Whiteboard of Collaboration ID=1001", function(assert){
        var done = assert.async();
        var input = $( "#test-input" ).focus();
        var flag=false;
        var count_flag=false;

        TestUtils.sendGetRequest( Globals.baseURL + "rest/collaboration/"+Globals.Whiteboard_Id+"/whiteboard").then(function(result){
            if(result.length > 0)
            {	
                count_flag=true;
            }
            if (result.length > 0)
            {					
                for(var i=0;i<result.length;i++)
                {
                    if(result[i].name !=null && result[i].id != null && result[i].wbList!=null )
                    {						
                        flag=true;
                    }
                    else
                    {
                        flag = false;
                        break;
                    }                        
                }
                assert.ok(flag,"Properties Should not be null !")
            }	               
            assert.ok(count_flag,"Retrieved the WhiteBoard Info");
            done();
        });
    });
        
    QUnit.test("Reading Whiteboard of not existed Collaboration ID=999", function(assert){
        var done = assert.async();
        TestUtils.sendGetRequest( Globals.baseURL + "rest/collaboration/999/whiteboard").then(function(result){
            assert.ok(result != null,"Response Should not be null");
            assert.ok(result[0].error != null,"Response should not be null !");
            assert.equal(result[0].error,"Collaboration ID NOT FOUND","Collaboration Id Not Found");
            done();
        });
    });    
        
    QUnit.test("Reading Whiteboard of not existed Collaboration with Negative ID= -999", function(assert){
        var done = assert.async();
        var input = $( "#test-input" ).focus();
        TestUtils.sendGetRequest( Globals.baseURL + "rest/collaboration/-999/whiteboard").then(function(result){
            assert.ok(result != null,"Response should not be null !");
            assert.equal(result[0].error,"IsNegative","Collaboration Id is Negative");
            done();
        });
    });
    
    QUnit.test( "Posting new Whiteboard", function( assert ) {
        var done = assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
		var data = {		
		"name": "ApiTest"+x
        }
        TestUtils.sendPostRequest(Globals.baseURL + "rest/collaboration/1002/whiteboard",data).then(function(result){
			assert.equal(typeof result , "number" , "response should be Integer");
            assert.ok(result != null , "Whiteboard Id should not be null")
            assert.ok(result > 1000 , "Whiteboard Created Successfully")
			done();
		});
      });

    QUnit.test( "Posting existed Whiteboard", function( assert ) {
		var done = assert.async();
		var data = {		
		"name": "ApiTest"
        }
        TestUtils.sendPostRequest(Globals.baseURL + "rest/collaboration/1002/whiteboard",data).then(function(result){
            assert.ok(result != null,"Response should not be null !");
            assert.equal(result[0].error,"Whiteboard already Exists with this name.","Whiteboard already Exists with this name.")
            done();
		});
    });
      
    QUnit.test( "Posting new Whiteboard with Blank Name", function( assert ) {
		var done = assert.async();
		var data = {		
		"name": ""
        }
        TestUtils.sendPostRequest(Globals.baseURL + "rest/collaboration/1002/whiteboard",data).then(function(result){
            assert.ok(result != null,"Response should not be null !");
            assert.equal(result[0].error,"IsBlank","Error: Whiteboard name is blank")
			done();
		});
    });

    QUnit.test( "Posting new Whiteboard with invalid Collaboration Id", function( assert ) {
		var done = assert.async();
		var data = {		
		"name": "APii"
        }
        TestUtils.sendPostRequest(Globals.baseURL + "rest/collaboration/9999/whiteboard",data).then(function(result){
            assert.ok(result != null,"Response should not be null !");
            assert.equal(result[0].error,"Collaboration does not exist for this Collaboration Id"," Error : Collaboration Is not exist")
            done();
		});
    });
   
})( QUnit.module, QUnit.test );