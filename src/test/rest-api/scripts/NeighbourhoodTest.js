(function( module, test ) {

module('Neighbourhood');
    //Neighbourhood
    var nhid=0;
    var nhid_=0;  

	QUnit.test( "Reading Neighborhood with ID=1", function( assert ) {
		var done = assert.async();
		TestUtils.sendGetRequest(Globals.baseURL + "rest/neighborhood/1").then(function(result){
			var flag=false;
			if(result.length>0)
			{
				count_flag=true;
			}
			if (result.length > 0)
			{					
				for(var i=0;i<result.length;i++)
				{
					if(result[i].id !=null && result[i].level != null && result[i].name !=null && result[i].parentId!=null && result[i].secure !=null)
					{						
						flag=true;
					}
					else
					{
						flag = false;
						break;
					}
				}
				assert.equal(flag,true,"Parameters should not be null");
			}		
			assert.equal(count_flag,true,"Retrieved the Neighborhood of ID=1");
			done();
		});		
    });

    QUnit.test( "Posting new Neighborhood at level 2", function( assert ) {
		var done = assert.async();
		var data = {
		"level":"2",
		"name": "BoardwalkAPI_12",
		"id":"0",
		"parentId": "30",
		"secure": "true"
		}
		TestUtils.sendPostRequest(Globals.baseURL + "rest/neighborhood",data).then(function(result){
			nhid_=result[0].id;
			assert.notEqual(result.length,0,"object should not be empty !");
			assert.ok(result[0].id > 0 ,"Neighborhood ID shoul greater than 0");
			assert.equal(Object.keys(result[0]).length, 5,"neighborhood should have 5 properties !");
			done();
		});			
      });

	QUnit.test("Delete test case for neighborhood at level 2", function(assert){
        var done=assert.async();       
  		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/neighborhood/" + nhid_).then(function(res){
   		assert.equal(typeof res, "string", "response should be string !");
        assert.notEqual(res.length, 0, "should not be null !");
   		var getResponse = res;
   		var checkSubString = getResponse.indexOf("Neighborhood Deleted Successfully");
   		assert.ok((checkSubString >= 0),"record must be updated successfully !");
        
         done();
 		 });
    });
	QUnit.test( "Posting new Neighborhood at level 0", function( assert ) {
		var done = assert.async();
		var data = {
			"level":"-1",
		"name": "api_Test_1",
		"id":"0",
		"parentId": "-1",
		"secure": "false"
		}
		TestUtils.sendPostRequest(Globals.baseURL + "rest/neighborhood",data).then(function(result){
			nhid=result[0].id;
			assert.notEqual(result.length,0,"object should not be empty !");
			assert.ok(result[0].id > 0 ,"Neighborhood ID shoul greater than 0");
			assert.equal(Object.keys(result[0]).length, 5,"neighborhood should have 5 properties !");
			done();
		});				
      });
      
      QUnit.test("Delete test case for neighborhood", function(assert){
        var done=assert.async();		
		TestUtils.sendDeleteRequest(Globals.baseURL + "rest/neighborhood/" + nhid).then(function(res){
			assert.equal(typeof res, "string", "response should be string !");
	        assert.notEqual(res.length, 0, "should not be null !");
        	assert.equal(res.includes("Neighborhood Deleted Successfully"), true, "record must be updated successfully !");
        	done();
		});
    });
	
})( QUnit.module, QUnit.test );