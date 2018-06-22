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

import boardwalk.rest.CollaborationManagement;
import boardwalk.rest.NeighborhoodManagement;

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
    public Response collaborationCollabIdDelete(Integer collabId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    
//    @GET
//    @Path("/{collabId}/whiteboard")
    @Override
    public Response collaborationCollabIdWhiteboardGet(Integer collabId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
 		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
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
		  	 	collabList = CollaborationManagement.collaborationCollabIdWhiteboardGet(collabId, ErrResps);
		    	
		    	if (collabList.size() > 0)
		        	return Response.ok().entity(collabList ).build();
		    	else
		    		return Response.ok().entity(ErrResps).build();   	
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
    }    
    
    
//    @POST
//    @Path("/{collabId}/whiteboard")
    @Override
    public Response collaborationCollabIdWhiteboardPost(Integer collabId, Whiteboard wb, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
 		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		 System.out.println("collabId ->" + collabId);
		 if (collabId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 
	  	if (wb.getName().trim().equals(""))
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Whiteboard.Name"); 
			erb.setProposedSolution("Whiteboard Name cannot be Blank.");
			erbs.add(erb);
	  	}
		 
	   	if (erbs.size() == 0)
	   	{
	   			int wbId;
		  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
		  	 	wbId = CollaborationManagement.collaborationCollabIdWhiteboardPost(collabId, wb, ErrResps);
		    	
		    	if (ErrResps.size() > 0)
		    		return Response.ok().entity(ErrResps).build();   	
		    	else
		        	return Response.ok().entity(wbId).build();
		    	
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

//    @DELETE
//    @Path("/{collabId}/whiteboard/{whiteboardId}")
    @Override
    public Response collaborationCollabIdWhiteboardWhiteboardIdDelete(Integer collabId, Integer whiteboardId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
