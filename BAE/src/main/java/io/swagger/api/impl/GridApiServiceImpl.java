package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import io.swagger.model.Grid;
import io.swagger.model.GridTransaction;
import io.swagger.model.ResponseInfo;
import io.swagger.model.Transaction;
import java.util.List;
import java.util.TimeZone;

import io.swagger.api.NotFoundException;

import java.io.InputStream;
import java.security.Principal;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.boardwalk.table.ColumnManager;
import com.boardwalk.table.RowManager;
import com.boardwalk.table.TableManager;
import com.boardwalk.user.UserManager;

import boardwalk.connection.BoardwalkConnection;
import boardwalk.rest.GridManagement;
import boardwalk.rest.NeighborhoodManagement;
import boardwalk.rest.bwAuthorization;

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
	  	 	
	  	 	cbf = GridManagement.gridGridIdGet(gridId, importTid, view, mode, baselineId, ErrResps, authBase64String, bwcon, memberNh, statusCode);

	  	 	//[LINK IMPORT]
	  	 	//500: Server-side Error. Exception thrown from GridManagement.gridGridIdGet [getTableBuffer] Block
	    	//500: Server-side Error. SystemException thrown from GridManagement.gridGridIdGet:TableManager.getTableInfo 
	    	//403: Forbidden. User don't have the privileges to execute this action.
	    	//200: Success. Returns cellBuffer

	  	 	//[REFRESH]
	  	 	//200 : Success. cellBuffer Returned
	  	 	//403: Forbidden. User don't have the privileges to execute this action.
	  	 	//500 : Server Error. SQLException thrown from GridManagement.gridGridIdGet::getGridRefresh -> getCriteriaTable | StartTransaction | BW_IMPORT_CHANGES 
	  	 	//500 : Server Error. SystemException thrown from GridManagement.gridGridIdGet::getGridRefresh -> TableManager.getTableInfo | ColumnManager.getXlColumnsForImport | RowManager.getTableRows
	  	 	
	    	if (ErrResps.size() > 0)
	    	{
				int scode = statusCode.get(0);
				return Response.status(scode).entity(ErrResps).build();   	
	    	}
	    	else
	    	{
	    		return Response.status(200).entity(cbf).build();		//200: Success. returns cellBuffer
	    	}
	   	}
	   	else
	   	{
	       	return Response.status(400).entity(erbs).build();	//400: Bad Request: Negative gridId, Null importTid, view, baseline, mode, mode != 1 or 0 
	   	}    
//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridPost(Grid grid, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
    	BoardwalkConnection bwcon = null;
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		ArrayList<Integer> statusCode = new ArrayList<Integer>();

		 
    	//System.out.println("GridApiServiceImpl::gridPost --> authBase64String : " + authBase64String);
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
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	int gridId = -1;
	  	 	gridId = GridManagement.gridPost(grid, ErrResps, authBase64String, bwcon, memberNh, statusCode);
	    	
	  	 	//404: Not found. Whiteboard not found.
	  	 	//404: Not found. Collaboration Id not found.
	  	 	//500: Server Error. Failed to get Neighborhood Relationships.
	  	 	//412: Precondition Failed.	Grid already exists in whiteboard. 

	    	if (ErrResps.size() > 0)
	    	{
				int scode = statusCode.get(0);
				return Response.status(scode).entity(ErrResps).build();   	
	    	}
	    	else
	    	{
	    		grid.setGridId(gridId);
	    		return Response.status(200).entity(grid).build();		//200: Success. Returns grid object with GridId
	    	}
	   	}
	   	else
	   	{
	       	return Response.status(400).entity(erbs).build();			//400:	Bad Request Negative or null collabId, wbId. Blank or null gridDesc, gridName.
	   	}    
    	//return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response gridPut( @NotNull Integer gridId, CellBuffer cellBufferRequest, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!

		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
    	BoardwalkConnection bwcon = null;
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		ArrayList<Integer> statusCode = new ArrayList<Integer>();

		System.out.println("Inside GridApiServiceImpl.gridPut --- gridId : " + gridId);
	    	//System.out.println("authBase64String : " + authBase64String);
			
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
	  	 	cbf = GridManagement.gridPut(gridId, cellBufferRequest, ErrResps, authBase64String, bwcon, memberNh, statusCode);
	    	
	  	 	//[LINK EXPORT]
	  	 	//404 : Bad Request. Missing elements info | cells | rowArray | columnArray | rows | columns | columnCellArrays | GridChangeBuffer
	  	 	//403 : Forbidden. User don't have the privileges Add Row | Administer Columns | View=none
	  	 	//404 : Bad Request. Blank Column Name
	  	 	//412 : Precondition Failed. Column Already Exists in Grid (Duplicate)
	  	 	//200 : Success. Returns cellBuffer
	  	 	//500 : Server Error. SystemException: Failed to get TableManager.getTableInfo.
	  	 	//500 : Server Error. SQLException on Server
	  	 	
	  	 	//[SUBMIT]
	  	 	//404 : Bad Request. Missing elements info | cells | rowArray | columnArray | rows | columns | columnCellArrays | GridChangeBuffer
		  	//404 : Bad Request. Membership is Not Valid (in validateMembership)
		  	//500 : Server Error. SystemException on Server in validateMembership
		  	//500 : Server Error. SQLException on Server on Submit
		  	//500 : Server Error. SystemException on Server on Submit	 	
	  	 	//409 : Conflict.  Critical Updates on Server.
	  	 	//403 : Forbidden.  User don't have the privileges to execute this action. Add/Delete Row | Administer Columns
	  	 	//423 : Locked. The resource that is being accessed is locked. The table is being updated by another user, Please try later
	  	 	//412 : Precondition Failed. Columns are not Unique. 
	  	 	//400 : Bad Request . Too many errors in payload
	  	 	
	    	if (ErrResps.size() > 0)
	    	{
				int scode = statusCode.get(0);
				return Response.status(scode).entity(ErrResps).build();   	
	    	}
	    	else
	    		return Response.status(200).entity(cbf).build();		//200 : Success. returns cellBuffer
	   	}
	   	else
	   	{
	       	return Response.status(400).entity(erbs).build();		//400: Bad Request. Negative GridId
	   	}    


//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridGridIdTransactionIdChangesGet(Integer gridId, Integer transactionId, SecurityContext securityContext , String authBase64String) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridGridIdTransactionsBetweenTidsGet(Long gridId,  @NotNull String reportType, @NotNull Long startTid,  @NotNull Long endTid, @NotNull String viewPref, SecurityContext securityContext  , String authBase64String) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response gridGridIdTransactionsGet(Integer gridId,  @NotNull BigDecimal localTimeAfter111970,  @NotNull String viewPref,  @NotNull String reportType,  String activityPeriod,  BigDecimal startDate,  BigDecimal endDate,  BigDecimal importTid, SecurityContext securityContext  , String authBase64String) throws NotFoundException 
    {

    	BoardwalkConnection bwcon = null;
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		ArrayList<Integer> statusCode = new ArrayList<Integer>();

    	ArrayList <RequestErrorInfo> reqeis = new ArrayList<RequestErrorInfo>();
    	RequestErrorInfo reqei;

    	ArrayList <ResponseErrorInfo> reseis = new ArrayList<ResponseErrorInfo>();
    	ResponseErrorInfo resei ;

		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		System.out.println("Inside GridApiServiceImpl.gridPut --- gridId : " + gridId);
		System.out.println("Inside GridApiServiceImpl.gridPut --- localTimeAfter111970 : " + localTimeAfter111970);
		System.out.println("Inside GridApiServiceImpl.gridPut --- viewPref : " + viewPref);
		System.out.println("Inside GridApiServiceImpl.gridPut --- reportType : " + reportType);
		System.out.println("Inside GridApiServiceImpl.gridPut --- activityPeriod : " + activityPeriod);
		System.out.println("Inside GridApiServiceImpl.gridPut --- startDate : " + startDate);
		System.out.println("Inside GridApiServiceImpl.gridPut --- endDate : " + endDate);
		System.out.println("Inside GridApiServiceImpl.gridPut --- importTid : " + importTid);
    	System.out.println("Inside GridApiServiceImpl.gridPut --- authBase64String : " + authBase64String);
			
    	long difference_in_MiliSec;
    	long local_offset = localTimeAfter111970.longValue() ;  //ON User machine

		Calendar cal_GMT = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		long server_Millis = cal_GMT.getTimeInMillis();			//ON GMT
		difference_in_MiliSec = local_offset - server_Millis;	//This is Offset of Local machine. i.e. India is +5:30 GMT in milliseconds.

		
		Calendar cal_Local = Calendar.getInstance();
		long server_Millis_local = cal_Local.getTimeInMillis();			//ON local
		long difference_in_MiliSec_local = local_offset - server_Millis_local;	//This is Offset of Local machine. i.e. India is +5:30 GMT in milliseconds.

		
    	System.out.println("Inside GridApiServiceImpl.gridPut --- local_offset : " + local_offset);
    	System.out.println("Inside GridApiServiceImpl.gridPut --- server_Millis : " + server_Millis);
    	System.out.println("Inside GridApiServiceImpl.gridPut --- difference_in_MiliSec : " + difference_in_MiliSec);
    	System.out.println("Inside GridApiServiceImpl.gridPut --- difference_in_MiliSec_local : " + difference_in_MiliSec_local);

		System.out.println("Local Server (gmt) in miliSeconds is " + server_Millis );
		System.out.println("The difference in Server and Clietnis " + (local_offset - server_Millis ));
    	
    	if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			
			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("Authorization in Header not Found");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
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

		if (gridId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("gridId"); 
			erb.setProposedSolution("You must enter an Existing Grid ID. It should be a Positive Number.");
			erbs.add(erb);

			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("Negative GridId");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
		}

		long actStartDate = -1;
		long actEndDate = -1;

		if (reportType.toUpperCase().equals("DURATION"))
		{
			java.util.Date d = new java.util.Date();
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(d);

			actEndDate = d.getTime();
			actStartDate = 0;

			
			if (activityPeriod.toUpperCase().equals("WEEK"))
			{
				cal.add(Calendar.DATE, -7);
				actStartDate = cal.getTime().getTime();
			}
			else if (activityPeriod.toUpperCase().equals("MONTH"))
			{
				cal.add(Calendar.MONTH, -1);
				actStartDate = cal.getTime().getTime();
			}
			else if (activityPeriod.toUpperCase().equals("QUARTER"))
			{
				cal.add(Calendar.MONTH, -3);
				actStartDate = cal.getTime().getTime();
			}
			else if (activityPeriod.toUpperCase().equals("YEAR"))
			{
				cal.add(Calendar.YEAR, -1);
				actStartDate = cal.getTime().getTime();
			}
			else if (activityPeriod.toUpperCase().equals("CUSTOM") && startDate != null && endDate != null)
			{
				actEndDate = endDate.longValue() - difference_in_MiliSec;
				actStartDate = startDate.longValue() - difference_in_MiliSec;
				
				if (actStartDate < actEndDate)
				{
					erb = new ErrorRequestObject();
					erb.setError("Start Date < End Date");
					erb.setPath("startDate, endDate");
					erb.setProposedSolution("Start Date must be prior to End Date.");
					erbs.add(erb);
				}
			}
			else 
			{
				erb = new ErrorRequestObject();
				erb.setError("Invalid Activity Period");
				erb.setPath("activityPeriod");
				erb.setProposedSolution("The Valid ActivityPeriod must be either Week OR Month OR Quarter OR Year OR Custom. For Custom activityPeriod you must provide valid startDate and endDate.");
				erbs.add(erb);
			}
		}
		else if (reportType.toUpperCase().equals("AFTERIMPORT"))
		{
			if (importTid == null)
			{
				erb = new ErrorRequestObject();
				erb.setError("importTid is missing in GET Request");
				erb.setPath("importTid");
				erb.setProposedSolution("importTid is mandetory to get transaction list after Last Import. Enter importTid as -1 or Positive Tranaction Number");
				erbs.add(erb);
			}
		}
		System.out.println("++++++++++++++++ actStartDate = "  + actStartDate + " actEndDate = " + actEndDate );
		
		
/*		if (startTid <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("startTid"); 
			erb.setProposedSolution("You must enter an Existing Start Transaction ID. It should be a Positive Number.");
			erbs.add(erb);
			
			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("Negative Start TxId");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
		}

		if (endTid <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("endTid"); 
			erb.setProposedSolution("You must enter an Existing End Transaction ID. It should be a Positive Number.");
			erbs.add(erb);

			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("Negative End TxId");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
		}

		if (startTid > endTid )
		{
			erb = new ErrorRequestObject(); erb.setError("startTid >= endTid"); erb.setPath("startTid"); 
			erb.setProposedSolution("Start Transaction Id must be prior to End Transaction Id.");
			erbs.add(erb);
			
			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("StartTId > EndTid");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
		}

		if (startDate > endDate)
		{
			erb = new ErrorRequestObject(); erb.setError("startDate > endDate"); erb.setPath("startDate"); 
			erb.setProposedSolution("Start Date must be prior to End Date. ");
			erbs.add(erb);
			
			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("Start Date > End Date");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
		}
*/

		if (reqeis.size() == 0)
		{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	   		ArrayList <GridTransaction> txs;

	  	 	txs = GridManagement.gridGridIdTransactionsGet(gridId, reportType, importTid.longValue() , actStartDate, actEndDate, difference_in_MiliSec, viewPref, ErrResps, authBase64String, bwcon, memberNh, statusCode);

	    	if (ErrResps.size() > 0)
	    	{
		    	resei = new ResponseErrorInfo();		    	
		    	resei.setErrorMessage("Errors on Server");
		    	resei.setErrorDetails(ErrResps);
		  
		    	ResponseInfo ri = new ResponseInfo();
		    	ri.setStatus("Failure");
		    	ri.setFailureDetails(reseis);
	    		return Response.status(422).entity(ri).build();   	
	    	}
	    	else
	    	{
	    		return Response.status(200).entity(txs).build();
	    	}
		}
		else
		{
	    	ResponseInfo ri = new ResponseInfo();
	    	ri.setStatus("Invalid Request");
	    	ri.setInvalidRequestDetails(reqeis);
	        return Response.status(400).entity(ri).build();
		}
    }
}
