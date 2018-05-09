package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.CellBuffer;
import java.util.Date;
import io.swagger.model.ErrorAddRows;
import io.swagger.model.ErrorDeleteObject;
import io.swagger.model.ErrorDeleteRows;
import io.swagger.model.ErrorReadObject;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.ErrorUpdateObject;
import io.swagger.model.Grid;
import io.swagger.model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import boardwalk.rest.GridManagement;
import boardwalk.rest.NeighborhoodManagement;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-07T10:23:52.356Z")
public class GridApiServiceImpl extends GridApiService {
    @Override
    public Response gridDelete(Integer tableId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridGet(String tableId, CellBuffer cellBufferRequest, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridPost(Grid grid, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (grid.getMemberId() <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("Grid.memberId"); 
			erb.setProposedSolution("You must enter an Existing Membership ID. It should be a Positive Number.");
			erbs.add(erb);
		}

		if (grid.getCollabId() <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("Grid.collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 
		if (grid.getWbId() <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("Grid.wbId"); 
			erb.setProposedSolution("You must enter an Existing Whiteboard ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		if (grid.getDescription().trim().equals(""))
		{	
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Grid.description"); 
			erb.setProposedSolution("You must enter Grid Description. It should not be BLANK.");
			erbs.add(erb);
		}
		if (grid.getName().trim().equals(""))
		{	
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Grid.Name"); 
			erb.setProposedSolution("You must enter an Grid Name. It should not be Blank.");
			erbs.add(erb);
		}
		 
	   	if (erbs.size() == 0)
	   	{
   			ArrayList<Collaboration> collabList;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	int gridId = -1;
	  	 	gridId = GridManagement.gridPost(grid, ErrResps);
	    	
	    	if (ErrResps.size() > 0)
	    		return Response.ok().entity(ErrResps).build();   	
	    	else
	    	{
	    		grid.setId(gridId);
	    		return Response.ok().entity(grid).build();
	    	}
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
    	//return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridPut(Integer gridId, CellBuffer cellBufferRequest, SecurityContext securityContext) throws NotFoundException {
        // do some magic!

		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

			System.out.println("Inside GridApiServiceImpl.gridPut --- gridId : " + gridId);

		if (gridId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("gridId"); 
			erb.setProposedSolution("You must enter an Existing Grid ID. It should be a Positive Number.");
			erbs.add(erb);
		}

	   	if (erbs.size() == 0)
	   	{
	   		CellBuffer cbf;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	
	  	 	cbf = GridManagement.gridPut(gridId, cellBufferRequest, ErrResps);
	    	
	    	if (ErrResps.size() > 0)
	    		return Response.ok().entity(ErrResps).build();   	
	    	else
	    	{
	    		return Response.ok().entity(cbf).build();
	    	}
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    


//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridTableIdTransactionIdChangesGet(Integer tableId, Integer transactionId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridTableIdTransactionsBetweenTidsGet(Long tableId,  @NotNull Long startTid,  @NotNull Long endTid, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridTableIdTransactionsGet(Integer tableId,  Long startTid,  Long endTid,  Date startTime,  Date endTime, CellBuffer cellBufferRequest, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
