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
    function sendRequest(URL, Data, authorization, requestType)
	{
        var Response=new $.Deferred();
        var json;
        console.log("***********************************************************************************************");
        console.log("Fetching :" + URL);
        if(requestType.toString() == "GET")
        {
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
        if(requestType.toString() == "DELETE")
        {
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
        if(requestType.toString() == "POST" || requestType.toString() == "PUT")
        {
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

    function sendPutRequest(URL,Data,authorization)
	{
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
    
    function sendDeleteRequest(URL, authorization)
	{
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

    function sendRequestMissingAuthorization(URL,Data,requestType)
    {
        var Response=new $.Deferred();
        var json;
        console.log("***********************************************************************************************");
        console.log("Fetching :" + URL);
        if(requestType.toString() == "GET")
        {          
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
        if(requestType.toString() == "DELETE")
        {
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
        if(requestType.toString() == "POST" || requestType.toString() == "PUT")
        {
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