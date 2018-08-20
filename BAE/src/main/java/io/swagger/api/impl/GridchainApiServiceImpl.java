package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import java.util.ArrayList;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;


import boardwalk.rest.GridchainManagement;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;

public class GridchainApiServiceImpl extends GridchainApiService {
    @Override
    public Response gridchainGridIdGet(Integer gridId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!

    	ArrayList <ErrorRequestObject> erbs  = new ArrayList<ErrorRequestObject>();
    	ErrorRequestObject erb;

    	ArrayList <RequestErrorInfo> reqeis = new ArrayList<RequestErrorInfo>();
    	RequestErrorInfo reqei;

    	ArrayList <ResponseErrorInfo> reseis = new ArrayList<ResponseErrorInfo>();
    	ResponseErrorInfo resei ;

    	GridChain gc;
    	
 		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); 
			erb.setError("Missing Authorization in Header"); 
			erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);

			reqei = new RequestErrorInfo();
			reqei.setErrorMessage("Authorization in Header not Found");
			reqei.setErrorDetails( erbs);
			reqeis.add(reqei);
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

		
		if (reqeis.size() == 0)
		{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	gc = GridchainManagement.gridchainGridIdGet(gridId, ErrResps, authBase64String);

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
	    	ri.setInvalidRequestDetails(reqeis);
	        return Response.status(400).entity(ri).build();
		}

    }
}
