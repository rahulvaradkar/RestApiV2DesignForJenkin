(function( module, test ) {

    // Relation
    module('Relation');
    QUnit.test("Reading Relation of Neighborhood ID=1", function( assert ) {
        var done = assert.async();
        var input = $( "#test-input" ).focus();
         var count_flag=false;
        var flag=false;


        TestUtils.for_GETMethod(Globals.baseURL +  "rest/neighborhood/1/relation").then(function(result){
			if(result.length > 0)
                {
                    count_flag=true;
                }
                if (count_flag)
                {					
                    for(var i=0;i<result.length;i++)
                    {
                        if(result[i].name !=null && result[i].relatedNeighborhoodId != null )
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
                assert.equal(count_flag,true,"Retrieved the Relation");
                done();
		  });        
    });   
     
    QUnit.test( "Creating Relation", function( assert ) {
		var done = assert.async();
		var data = {		
        "name": "APIRelation",
        "relatedneighborhoodId":[{"id":2},{"id":3}]
        }
        
        TestUtils.for_POSTMethod(Globals.baseURL + "rest/neighborhood/1/relation",data).then(function(result){
            assert.ok(result != null ,"Response Should not be");
            assert.equal(result[0].error,"All Neighborhood Relations Created Successfully","Relation Created Successfully");
            done();
        });
      }); 
})( QUnit.module, QUnit.test );