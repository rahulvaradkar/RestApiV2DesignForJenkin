(function( module, test ) {

    module('Collaborations');
        //Collaborations
    QUnit.test("Reading Collaborations of Neighborhood ID=1", function(assert){
        var done = assert.async();
        //var input = $( "#test-input" ).focus();
        var flag=false;
        var count_flag=false;
        $.ajax({url: Globals.baseURL + "rest/neighborhood/1/collaboration",
            type: "GET",
            dataType: "json",
            success: function(result){
                console.log("fetching the url : http://localhost:8080/BAE_ReST/rest/neighborhood/1/collaboration");
                console.log(JSON.stringify(result[0]));
                console.log("\n");
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
                    assert.equal(flag,true,"Parameters should not be null");
                }	
                assert.equal(count_flag,true,"Retrieved the Relation");
                done();
            }
         });
    });

    QUnit.test("Reading Collaborations of Neighborhood when collaborations don't exist", function(assert){
        var done = assert.async();
        //var input = $( "#test-input" ).focus();
        var flag=false;
        var count_flag=false;
        $.ajax({url: Globals.baseURL + "rest/neighborhood/30/collaboration",
            type: "GET",
            dataType: "json",
            success: function(result){
                console.log("fetching the url : http://localhost:8080/BAE_ReST/rest/neighborhood/30/collaboration");
                console.log((result));
                console.log("\n");
                if(result.length == 0)
                {
                    count_flag=true;
                }                
                assert.equal(count_flag,true,"No Collaborations are Exist");
                done();
            }
         });
    });
               
})( QUnit.module, QUnit.test );