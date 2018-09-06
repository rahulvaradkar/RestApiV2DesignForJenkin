package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.ErrorRequestObject;
import io.swagger.model.Whiteboard;

import java.util.ArrayList;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import boardwalk.connection.BoardwalkConnection;
import boardwalk.rest.CollaborationManagement;
import boardwalk.rest.NeighborhoodManagement;
import boardwalk.rest.bwAuthorization;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-10T09:36:40.808Z")
public class CollaborationApiServiceImpl extends CollaborationApiService {
	
//    @DELETE
//    @Path("/{collabId}")
    @Override
    public Response collaborationCollabIdDelete(Integer collabId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
    	BoardwalkConnection bwcon = null;
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		ArrayList<Integer> statusCode = new ArrayList<Integer>();
		 
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();				//401: Missing Authorization
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();		//401: Authorization Failed
    		}
    	}
		 
		System.out.println("collabId ->" + collabId);
		if (collabId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 
		if (erbs.size() == 0)
	   	{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	CollaborationManagement.collaborationCollabIdDelete(collabId, ErrResps, authBase64String, bwcon, memberNh, statusCode);

	  	 	//404:	Collaboration ID NOT FOUND
	  	 	//200 : Success. Collaboration deleted successfully.
	  	 	if (ErrResps.size() > 0)
	  	 	{
				int scode = statusCode.get(0);
				return Response.status(scode).entity(ErrResps).build();   	
	  	 	}
	    	else
	    		return Response.status(200).entity("Collaboration Deleted Successfully").build();		//200: Collaboration deleted successfully
	   	}
	   	else
	   	{
	       	return Response.status(400).entity(erbs).build();		//400: Bad Request. Negative collabId
	   	}   
//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    
    
//    @GET
//    @Path("/{collabId}/whiteboard")
    @Override
    public Response collaborationCollabIdWhiteboardGet(Integer collabId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
    	BoardwalkConnection bwcon = null;
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		ArrayList<Integer> statusCode = new ArrayList<Integer>();
		 
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();				//401: Missing Authorization
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();		//401: Authorization Failed
    		}
    	}
		 
		System.out.println("collabId ->" + collabId);
		if (collabId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 
	   	if (erbs.size() == 0)
	   	{
   			ArrayList<Collaboration> collabList;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	collabList = CollaborationManagement.collaborationCollabIdWhiteboardGet(collabId, ErrResps, authBase64String, bwcon, memberNh, statusCode);

	  	 	//200:	Success. Returns collabList with List of whiteboard
	  	 	//404:	Collaboration ID NOT FOUND

	  	 	if (ErrResps.size() > 0)
	  	 	{
				int scode = statusCode.get(0);
				return Response.status(scode).entity(ErrResps).build();   	
	  	 	}
	    	else
	    		return Response.status(200).entity(collabList).build();		//200: Success. Returns collabList for collabId including all Whiteboards in it
	   	}
	   	else
	   	{
	       	return Response.status(400).entity(erbs).build();				//400: Bad Request. Negative collabId
	   	}    
    }    
    
    
//    @POST
//    @Path("/{collabId}/whiteboard")
    @Override
    public Response collaborationCollabIdWhiteboardPost(Integer collabId, Whiteboard wb, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
    	BoardwalkConnection bwcon = null;
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		ArrayList<Integer> statusCode = new ArrayList<Integer>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();		//401: Missing Authorization
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();		//401: Authorization Failed
    		}
    	}
		
		System.out.println("collabId ->" + collabId);
		if (collabId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 
		String wbName = null;
		wbName = wb.getName();
		
	  	if (wbName == null)
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("Whiteboard.Name"); 
			erb.setProposedSolution("Whiteboard Name is Missing. Enter Whiteboard Name and try again");
			erbs.add(erb);
	  	}
	  	else if (wbName.trim().equals(""))
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Whiteboard.Name"); 
			erb.setProposedSolution("Whiteboard Name cannot be Blank.");
			erbs.add(erb);
	  	}
		 
	   	if (erbs.size() == 0)
	   	{
			int wbId;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	wbId = CollaborationManagement.collaborationCollabIdWhiteboardPost(collabId, wb, ErrResps, authBase64String, bwcon, memberNh, statusCode);
	    	
		  	//200:	Success. Whiteboard created successfully. Returns WhiteboardID
		  	//404:	Collaboration ID NOT FOUND
		  	//412:	Whiteboard Exists. Precondition failed. The server does not meet one of the preconditions that the requester put on the request 	
	
	  	 	if (ErrResps.size() > 0)
	  	 	{
				int scode = statusCode.get(0);
				return Response.status(scode).entity(ErrResps).build();   	
	  	 	}
	    	else
	    		return Response.status(200).entity(wbId).build();		//200: Whiteboard created successfully. Returns WhiteboardID
	   	}
	   	else
	   	{
	       	return Response.status(400).entity(erbs).build();		//400: Bad Request. Negative collabId. Null or Missing or Empty Whiteboard Name.
	   	}    
//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    
//    @DELETE
//    @Path("/{collabId}/whiteboard/{whiteboardId}")
    @Override
    public Response collaborationCollabIdWhiteboardWhiteboardIdDelete(Integer collabId, Integer whiteboardId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
    	BoardwalkConnection bwcon = null;
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		ArrayList<Integer> statusCode = new ArrayList<Integer>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();		//401: Missing Authorization
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();		//401: Authorization Failed
    		}
    	}
		
		System.out.println("collabId ->" + collabId);
		if (collabId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 
	  	if (whiteboardId <= 0)
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("whiteboardId"); 
			erb.setProposedSolution("You must enter an Existing Whiteboard ID. It should be a Positive Number.");
			erbs.add(erb);
	  	}
		 
	   	if (erbs.size() == 0)
	   	{
			int wbId;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	CollaborationManagement.collaborationCollabIdWhiteboardWhiteboardIdDelete(collabId, whiteboardId, ErrResps, authBase64String, bwcon, memberNh, statusCode);
	    	
	  	 	//200: Success. Whiteboard deleted successfully.
	  	 	//404:	Collaboration ID NOT FOUND
	  	 	//404: Whiteboard not found

	  	 	if (ErrResps.size() > 0)
	  	 	{
				int scode = statusCode.get(0);
				return Response.status(scode).entity(ErrResps).build();   	
	  	 	}
	    	else
	    		return Response.status(200).entity("Whiteboard: [" + whiteboardId + "] under Collaboration:[" + collabId + "] is successfully Deleted.").build();		//200: Whiteboard Deleted successfully. 
	   	}
	   	else
	   	{
	       	return Response.status(400).entity(erbs).build();		//400 : Bad Request. Negative collabId, Negative whiteboardId 
	   	}    
    	//return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
