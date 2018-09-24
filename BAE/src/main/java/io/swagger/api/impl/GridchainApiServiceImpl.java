package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import java.util.ArrayList;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import boardwalk.connection.BoardwalkConnection;
import boardwalk.rest.GridchainManagement;
import boardwalk.rest.bwAuthorization;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;

public class GridchainApiServiceImpl extends GridchainApiService {
    @Override
    public Response gridchainGridIdGet(Integer gridId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!

    	StringBuffer invReqMsg = new StringBuffer(500);
    	StringBuffer invResMsg = new StringBuffer(500);

    	BoardwalkConnection bwcon = null;
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		ArrayList<Integer> statusCode = new ArrayList<Integer>();
    	
    	ArrayList <ErrorRequestObject> erbs  = new ArrayList<ErrorRequestObject>();
    	ErrorRequestObject erb;

    	ArrayList <RequestErrorInfo> reqeis = new ArrayList<RequestErrorInfo>();
    	RequestErrorInfo reqei = new RequestErrorInfo();

    	ArrayList <ResponseErrorInfo> reseis = new ArrayList<ResponseErrorInfo>();
    	ResponseErrorInfo resei ;

    	GridChain gc;
    	
 		if (authBase64String == null)
		{	
 			erbs  = new ArrayList<ErrorRequestObject>();
 			
			erb = new ErrorRequestObject(); 
			erb.setError("Missing Authorization in Header"); 
			erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);

			invReqMsg.append("Authorization in Header not Found. ");
			
			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("Authorization in Header not Found");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
		}
		else
		{
			ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	    	//Connection connection = null;
			
			bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
			if (!ErrResps.isEmpty())
			{
				reqei = new RequestErrorInfo();
				reqei.setErrorMessage("Authentication Failed");
				reqei.setErrorDetails( ErrResps);
				reqeis.add(reqei);
				return Response.status(401).entity(reqeis).build();		//401: Authorization Failed
			}
		} 		

		if (gridId <= 0)	
		{	
			erbs  = new ArrayList<ErrorRequestObject>();
			
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("gridId"); 
			erb.setProposedSolution("You must enter an Existing Grid ID. It should be a Positive Number.");
			erbs.add(erb);

			invReqMsg.append("Negative GridId. ");
			
			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("Negative GridId");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
		}

		
		if (reqeis.size() == 0)
		{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	gc = GridchainManagement.gridchainGridIdGet(gridId, ErrResps, bwcon, memberNh, statusCode);

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
	    		return Response.status(200).entity(gc).build();
	    	}
		}
		else
		{
	    	ResponseInfo ri = new ResponseInfo();
	    	ri.setStatus("Invalid Request");
	    	ri.setMessage(invReqMsg.toString().trim());
	    	ri.setInvalidRequestDetails(reqeis);
	        return Response.status(400).entity(ri).build();
		}

    }
}
