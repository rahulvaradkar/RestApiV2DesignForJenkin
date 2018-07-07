package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.CellBuffer;
import java.util.Date;
import io.swagger.model.ErrorAddRows;
import io.swagger.model.ErrorDeleteObject;
import io.swagger.model.ErrorDeleteRows;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.ErrorUpdateObject;
import io.swagger.model.Grid;
import io.swagger.model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;
import java.security.Principal;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.boardwalk.user.UserManager;

import boardwalk.rest.GridManagement;
import boardwalk.rest.NeighborhoodManagement;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-30T16:47:27.652Z")
public class GridApiServiceImpl extends GridApiService {

	 
	@Override
    public Response gridDelete(Integer gridId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    // Old call ->    public Response gridGridIdGet(Integer gridId, CellBuffer cellBufferRequest, SecurityContext securityContext, String authBase64String) throws NotFoundException {
	@Override
    public Response gridGridIdGet(Integer gridId,  @NotNull Integer importTid,  @NotNull String view,  @NotNull Integer mode,  @NotNull Integer baselineId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
    	
    	//System.out.println("authBase64String : " + authBase64String);
    	ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}

		if (gridId <= 0)	
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("gridId"); 
			erb.setProposedSolution("You must enter an Existing Grid ID. It should be a Positive Number.");
			erbs.add(erb);
		}

		if (importTid == null)
		{
			erb = new ErrorRequestObject();
			erb.setError("importTid is missing in GET Request");
			erb.setPath("importTid");
			erb.setProposedSolution("importTid is mandetory. Enter importTid as -1 or Positive Tranaction Number ");
			erbs.add(erb);
		}
		
		if (view == null)
		{
			erb = new ErrorRequestObject();
			erb.setError("View is missing in GET Request");
			erb.setPath("view");
			erb.setProposedSolution("View is mandetory. Valid View values are [ MY_ROWS |LATEST | DESIGN | LATEST_BY_USER | LATEST_VIEW_OF_ALL_USERS | LATEST_VIEW_OF_ALL_CHILDREN | LATEST_VIEW_OF_ALL_USERS_IN_ANY_NH | LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NH | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD ]");
			erbs.add(erb);
		}
		else if (view.trim().equals("MY_ROWS") || 
    			view.trim().equals("LATEST") || 
    			view.trim().equals("DESIGN") || 
    			view.trim().equals("LATEST_BY_USER") || 
    			view.trim().equals("LATEST_VIEW_OF_ALL_USERS") || 
    			view.trim().equals("LATEST_VIEW_OF_ALL_CHILDREN") || 
    			view.trim().equals("LATEST_VIEW_OF_ALL_USERS_IN_ANY_NH") || 
    			view.trim().equals("LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NH") || 
    			view.trim().equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH") || 
    			view.trim().equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD") || 
    			view.trim().equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD") || view.trim().indexOf("?") == 0 )
		{    			
    		System.out.println("view : " + view);
		}		
		else 
		{
			erb = new ErrorRequestObject();
			erb.setError("Invalid View in GET Request");
			erb.setPath("view");
			erb.setProposedSolution("View is mandetory. Valid View values are [ MY_ROWS |LATEST | DESIGN | LATEST_BY_USER | LATEST_VIEW_OF_ALL_USERS | LATEST_VIEW_OF_ALL_CHILDREN | LATEST_VIEW_OF_ALL_USERS_IN_ANY_NH | LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NH | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD ]");
			erbs.add(erb);
		}

		if (mode == null)
		{
			erb = new ErrorRequestObject();
			erb.setError("Mode is missing in GET Request");
			erb.setPath("mode");
			erb.setProposedSolution("Mode is mandetory. Valid Mode is 1 or 0 to get the Grid Status");
			erbs.add(erb);
		}
		else if ((mode != 0) && (mode != 1))
		{
			erb = new ErrorRequestObject();
			erb.setError("Invalid Mode in GET Request");
			erb.setPath("mode");
			erb.setProposedSolution("Valid Mode is 1 or 0 to get the Grid Status");
			erbs.add(erb);
		}
		
		if (baselineId == null)
		{
			erb = new ErrorRequestObject();
			erb.setError("BaselineId is missing in GET Request");
			erb.setPath("baselineId");
			erb.setProposedSolution("BaselineId is mandetory. Provide default BaselineId as -1");
			erbs.add(erb);
		}
		
		if (erbs.size() == 0)
	   	{
	   		CellBuffer cbf;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	
	  	 	cbf = GridManagement.gridGridIdGet(gridId, importTid, view, mode, baselineId, ErrResps, authBase64String);

	  	 	//	        return Response.ok().entity( new ApiResponseMessage( 201, ErrResps.toString())).build();
    		//return Response.ok().entity(ErrResps).build();   	

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
    public Response gridPost(Grid grid, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

	    	//System.out.println("GridApiServiceImpl::gridPost --> authBase64String : " + authBase64String);

	    	if (authBase64String == null)
			{	
				erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
				erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
				erbs.add(erb);
			}
		 
		String gridName = null;
		String gridDesc = null;
		Integer collabId = null;
		Integer wbId = null;
		
		collabId = grid.getCollabId();
		if (collabId == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("Grid.collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		else if (collabId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("Grid.collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 
		wbId = grid.getWbId();
		if (wbId == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("Grid.wbId"); 
			erb.setProposedSolution("You must enter an Existing Whiteboard ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		else if (wbId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("Grid.wbId"); 
			erb.setProposedSolution("You must enter an Existing Whiteboard ID. It should be a Positive Number.");
			erbs.add(erb);
		}

		gridDesc = grid.getDescription();
		if (gridDesc == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("Grid.description"); 
			erb.setProposedSolution("You must enter Grid Description. It should not be BLANK.");
			erbs.add(erb);
		}
		else if (gridDesc.trim().equals(""))
		{	
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Grid.description"); 
			erb.setProposedSolution("You must enter Grid Description. It should not be BLANK.");
			erbs.add(erb);
		}
		
		gridName = grid.getName();
		if (gridName == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Grid.Name"); 
			erb.setProposedSolution("You must enter an Grid Name. It should not be Blank.");
			erbs.add(erb);
		}
		else if (gridName.trim().equals(""))
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
	  	 	gridId = GridManagement.gridPost(grid, ErrResps, authBase64String);
	    	
	    	if (ErrResps.size() > 0)
	    		return Response.ok().entity(ErrResps).build();   	
	    	else
	    	{
	    		grid.setGridId(gridId);
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
    public Response gridPut( @NotNull Integer gridId, CellBuffer cellBufferRequest, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!

		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

			System.out.println("Inside GridApiServiceImpl.gridPut --- gridId : " + gridId);
	    	//System.out.println("authBase64String : " + authBase64String);
			
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}
			
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
	  	 	
	  	 	cbf = GridManagement.gridPut(gridId, cellBufferRequest, ErrResps, authBase64String);
	    	
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
    public Response gridTableIdTransactionIdChangesGet(Integer tableId, Integer transactionId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridTableIdTransactionsBetweenTidsGet(Long tableId,  @NotNull Long startTid,  @NotNull Long endTid, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridTableIdTransactionsGet(Integer tableId,  Long startTid,  Long endTid,  Date startTime,  Date endTime, CellBuffer cellBufferRequest, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
