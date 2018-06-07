(function( module, test ) {

    module('Collaborations');
    var Collab_Name="";
        //Collaborations
    QUnit.test("Reading Collaborations of Neighborhood ID=1", function(assert){
        var done = assert.async();
        var input = $( "#test-input" ).focus();
        var flag=false;
        var count_flag=false;

        TestUtils.sendRequest( Globals.baseURL+"rest/neighborhood/1/collaboration", null, Globals.authorization, "GET").then(function(result){
            if(result.length > 0)
            {
                count_flag=true;
            }
            if (result.length > 0)
            {
                for(var i = 0; i < result.length; i++)
                {
                    if(result[i].name != null && result[i].id != null && result[i].wbList != null )
                    {
                        flag=true;
                    }
                    else
                    {
                        flag = false;
                        break;
                    }
                    assert.equal(typeof result[i].name, "string", "Name Should be String");
                    assert.equal(typeof result[i].id, "number", "Name Should be String");
                    assert.ok(result[i].id > 0, "Id should be greater than 1000");
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

        TestUtils.sendRequest( Globals.baseURL+"rest/neighborhood/30/collaboration",null, Globals.authorization, "GET").then(function(result){
            if(result.length == 0 )
            {
                count_flag=true;
            }                
            assert.ok(count_flag,"Collaborations don't Exist");
            done();
        });      
    });
    
    QUnit.test("Posting New Collaboration",function(assert){
        var done=assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        Collab_Name="Collab_"+x;
        var data={
            "name" : Collab_Name,
            "Purpose" : "Test"
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/neighborhood/3/member/" + Globals.Member_Id_2 + "/collaboration", data, Globals.authorization, "POST").then(function(result){
            assert.ok(result != null,"Response should not be null");
            assert.equal()           
            assert.ok(result > 0,"Collab Id Should greater than 0");
            done();
        });
    });

    QUnit.test("Posting Existing Collaboration",function(assert){
        var done=assert.async();
        var data={
            "name" : Collab_Name,
            "Purpose" : "Test"
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/neighborhood/3/member/" + Globals.Member_Id_2 + "/collaboration", data, Globals.authorization, "POST").then(function(result){
            assert.ok(result != null,"Response Should not be null");
			assert.equal(result[0].error,"Failed to Create New Collaboration","Collaboration Already Exists");
			done();
        });
    });

    QUnit.test("Posting new Collaboration with negative nhid and negative member ID",function(assert){
        var done=assert.async();       
        var data={
            "name" : Collab_Name,
            "Purpose" : "Test"
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/neighborhood/-3/member/-1001/collaboration", data, Globals.authorization, "POST").then(function(result){
            assert.ok(result != null,"Response Should not be null");
            assert.equal(result[0].error,"IsNegative","NHID is Negative");
            assert.equal(result[1].error,"IsNegative","Member ID is Negative");
			done();
        });
    });

    QUnit.test("Posting New Collaboration with Missing Authorization",function(assert){
        var done=assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var collab_name="Collab_"+x;
        var data={
            "name" : collab_name,
            "Purpose" : "Test"
        }
        TestUtils.sendRequestMissingAuthorization(Globals.baseURL + "rest/neighborhood/3/member/" + Globals.Member_Id_2 + "/collaboration", data, "POST").then(function(result){
            assert.ok(result != null,"Response Should not be null");
			assert.equal(result[0].error,"Missing Authorization in Header","Missing Authorization in Header");
			done();
        })
    });

    QUnit.test("Posting New Collaboration with Invalid Authorization",function(assert){
        var done=assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var collab_name="Collab_"+x;
        var data={
            "name" : collab_name,
            "Purpose" : "Test"
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/neighborhood/3/member/" + Globals.Member_Id_2 + "/collaboration", data, Globals.invalidAuthorization, "POST").then(function(result){
            assert.ok(result != null,"Response Should not be null");
			assert.equal(result[0].error,"Invalid Authorization. User is not a member of Neighborhood Path.","Invalid Authorization. User is not a member of Neighborhood Path.");
			done();
        });
    });

    QUnit.test("Posting New Collaboration with Invalid MemberShip",function(assert){
        var done=assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var collab_name="Collab_"+x;
        var data={
            "name" : collab_name,
            "Purpose" : "Test"
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/neighborhood/3/member/1002/collaboration", data, Globals.authorization, "POST").then(function(result){
            assert.ok(result != null,"Response Should not be null");
			assert.equal(result[0].error,"Authorization Is not allowed to create Collaboration in this Neighborood. [MemberID mismatch]","Invalid Memberships");
			done();
        });
    });

    QUnit.test("Posting New Collaboration with Blank Fields",function(assert){
        var done=assert.async();
        var x = Math.floor((Math.random() * 10000) + 1);
        var collab_name="Collab_"+x;
        var data={
            "name" : "",
            "Purpose" : ""
        }
        TestUtils.sendRequest(Globals.baseURL + "rest/neighborhood/3/member/" + Globals.Member_Id_2 + "/collaboration", data, Globals.authorization, "POST").then(function(result){
            assert.ok(result != null,"Response Should not be null");
            assert.equal(result[0].error,"IsBlank","Collab_Name is Blank");
            assert.equal(result[1].error,"IsBlank","Collab_Name is Blank");
			done();
        });
    });


})( QUnit.module, QUnit.test );