//Undecided asserts
function undecidedAssertEqual(assert, value, forNow, neat, message) {
  if(Globals.assertHow == "forNow") {
    assert.equal(value, forNow, message);
  } else {
    assert.equal(value, neat, message);
  }
}

//check whether number is > n
function isValidNumber(number, n) {
  if(!n) {
    n = 0;
  }
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
  for(var i = 0; i < props.length; i++) {
    if(object[props[i]] == null) {
      hasNullAttributes = true;
    }
  }
  return hasNullAttributes;
}

//Check whether object has valid properties
function hasValidProperties(object, props) {
  var hasValidProps = true;
  for(var i = 0; i < props.length; i++) {
    if(!object.hasOwnProperty(props[i])) {
      hasValidProps = false;
    }
  }
  return hasValidProps;
}


var TestUtils=(function(){

    function sendGetRequest(URL)
    {
        var authorization=getAuthorization("j",0,"JSAddin");
        var Response=new $.Deferred();
            console.log("***********************************************************************************************");
            console.log("Fetching :" + URL);
			$.ajax({
				url: URL,
		        type: "GET",
                dataType: "json",
                headers: {
                    'Authorization': Globals.authorization,
                    'Accept': 'application/json'
                },
		        success: function(result){
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result));
                    console.log("***********************************************************************************************");
                    console.log("\n");
				}
			});
		
		return Response.promise();
	}
	
	function sendPostRequest(URL,Data)
	{
        var Response=new $.Deferred();
            console.log("***********************************************************************************************");
            console.log("Fetching :" + URL);
			$.ajax({
                url:URL,
				type: "POST",
				dataType: "json",
				data: JSON.stringify(Data),
                contentType: "application/json",
                headers: {
                    'Authorization': Globals.authorization,
                    'Accept': 'application/json'
                },
				success: function(result){			
                    Response.resolve(result);
                    console.log("Response  : " + JSON.stringify(result));
                    console.log("***********************************************************************************************");
                    console.log("\n");
				}
			});
		return Response.promise();
    }

    function sendPutRequest(URL,Data)
	{
        var Response=new $.Deferred();
            console.log("***********************************************************************************************");
            console.log("Fetching :" + URL);
            var res = $.ajax({
                url: URL,
			    type: "PUT",
			    dataType: "json",
			    data: JSON.stringify(Data),
                contentType: "application/json",
                headers: {
                    'Authorization': Globals.authorization,
                    'Accept': 'application/json'
                },
			    async: false
            }).responseText;
            console.log("Response  : " + res);
            console.log("***********************************************************************************************");
            console.log("\n");
            Response.resolve(res);
		
		return Response.promise();;
    }
    
    function sendDeleteRequest(URL)
	{
        var Response=new $.Deferred();
            console.log("***********************************************************************************************");
            console.log("Fetching :" + URL);
            var res = $.ajax({
                url: URL,
			    type: "DELETE",
			    dataType: "json",
                contentType: "application/json",
                headers: {
                    'Authorization': Globals.authorization,
                    'Accept': 'application/json'
                },
			    async: false
            }).responseText;
            console.log("Response  : " + res);
            console.log("***********************************************************************************************");
            console.log("\n");
            Response.resolve(res);
		
		return Response.promise();;
    }

    function sendPutRequestJson(URL,Data)
	{
		
        console.log("***********************************************************************************************");
        console.log("Fetching :" + URL);
        var Response=new $.Deferred();
		$.ajax({
            url:URL,
			type: "PUT",
			dataType: "json",
			data: JSON.stringify(Data),
            contentType: "application/json",
			headers: {
			'Authorization': Globals.authorization,
			'Accept': 'application/json'
			},
			success: function(result){			
				Response.resolve(result);
				console.log("Response  : " + JSON.stringify(result));
				console.log("***********************************************************************************************");
				console.log("\n");
				}
			});
		
		return Response.promise();
    }
    return  {
        sendGetRequest: sendGetRequest, 
        sendPostRequest: sendPostRequest, 
        sendPutRequest: sendPostRequest,
        sendDeleteRequest: sendDeleteRequest,
        sendPutRequestJson : sendPutRequestJson
    };
})();
