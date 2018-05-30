(function( module, test ) {

    module('Collaborations');
        //Collaborations
    QUnit.test("Reading Collaborations of Neighborhood ID=1", function(assert){
        var done = assert.async();
        var input = $( "#test-input" ).focus();
        var flag=false;
        var count_flag=false;

        TestUtils.for_GETMethod( Globals.baseURL+"rest/neighborhood/1/collaboration").then(function(result){
            if(result.length>0)
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
                assert.ok(flag,"Parameters should not be null");
            }
            assert.ok(count_flag,"Retrieved the Collaborations Of Neighborhood");
            done();
        });
    });

    QUnit.test("Reading Collaborations of Neighborhood when collaboration are not exist", function(assert){
        var done = assert.async();       
        var count_flag=false;

        TestUtils.for_GETMethod( Globals.baseURL+"rest/neighborhood/30/collaboration").then(function(result){
            if(result.length == 0 )
            {
                count_flag=true;
            }                
            assert.ok(count_flag,"Collaborations don't Exist");
            done();
        });      
    });
               
})( QUnit.module, QUnit.test );