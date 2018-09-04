package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import java.util.ArrayList;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;
import java.sql.Connection;

import org.glassfish.jersey.client.authentication.ResponseAuthenticationException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.sun.mail.imap.protocol.Status;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;

import boardwalk.connection.BoardwalkConnection;
import boardwalk.rest.UserManagement;
import boardwalk.rest.bwAuthorization;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-09T04:12:45.675Z")
public class UserApiServiceImpl extends UserApiService {
	
	//  /user/{email}/memberships
    @Override
    public Response userEmailMembershipsGet(String email, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
    		
       	System.out.println("email : " + email);

    	StringBuffer invReqMsg = new StringBuffer(500);
    	StringBuffer invResMsg = new StringBuffer(500);

    	ArrayList <ErrorRequestObject> erbs  = new ArrayList<ErrorRequestObject>();
    	ErrorRequestObject erb;

    	ArrayList <RequestErrorInfo> reqeis = new ArrayList<RequestErrorInfo>();
    	RequestErrorInfo reqei = new RequestErrorInfo();

    	ArrayList <ResponseErrorInfo> reseis = new ArrayList<ResponseErrorInfo>();
    	ResponseErrorInfo resei ;
    	
    		//ErrorRequestObject erb;
    		//ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

    		//System.out.println("authBase64String : " + authBase64String);
    			
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

     	    	ResponseInfo ri = new ResponseInfo();
    	    	ri.setStatus("Authorization Failed.");
    	    	ri.setMessage(invReqMsg.toString().trim());
    	    	ri.setInvalidRequestDetails(reqeis);
    	        return Response.status(401).entity(ri).build();    			
    		}

    		if (email == null)
    		{
    			
    			erbs  = new ArrayList<ErrorRequestObject>();
    			
    			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.email"); 
    			erb.setProposedSolution("Enter Email");
    			erbs.add(erb);

    			invReqMsg.append("Missing Email. ");
    			
    			reqei = new RequestErrorInfo();
    			reqei.setErrorMessage("Missing Email.");
    			reqei.setErrorDetails( erbs);
    			reqeis.add(reqei);
    		}
    		else if (email.trim().equals(""))
        	{	
    			erbs  = new ArrayList<ErrorRequestObject>();
    			
    			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email"); 
    			erb.setProposedSolution("Enter Email");
    			erbs.add(erb);

    			invReqMsg.append("Blank Email. ");
    			
    			reqei = new RequestErrorInfo();
    			reqei.setErrorMessage("Blank Email.");
    			reqei.setErrorDetails( erbs);
    			reqeis.add(reqei);
        	}
    		    		
    		if (reqeis.size() == 0)
    		{
           	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();

        		ArrayList<Membership> ml;
            	ml = UserManagement.userGetMemberships(email, ErrResps, authBase64String);
           	 	
            	System.out.println("ml.size :"+ ml.size());
            	System.out.println("ErrResps.size :"+ ErrResps.size());
            	
            	if (ml.size() > 0)
            		return Response.ok().entity(ml).build();
        		else
        		{
    		    	resei = new ResponseErrorInfo();		    	
    		    	resei.setErrorMessage("Errors on Server");
    		    	resei.setErrorDetails(ErrResps);
    		    	reseis.add(resei);
    		    	
    		    	ResponseInfo ri = new ResponseInfo();
    		    	ri.setStatus("Failure");
    		    	ri.setFailureDetails(reseis);
    	    		return Response.status(422).entity(ri).build();  
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
    		    	
        //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    
    
    
    //GET  ....../user/{email}/neighborhood/{nhPath}/collaboration/{collabId}/whiteboards
    @Override
    public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardsGet(String email, String nhPath, Integer collabId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
    	
    	System.out.println("email : " + email);
      	System.out.println("nhPath : " + nhPath);
      	System.out.println("collabId : " + collabId);

		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
			
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}

		if (email == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
		}
		else if (email.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
    	}

		if (nhPath == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.nhPath");
    		erb.setProposedSolution("Enter nhPath");
    		erbs.add(erb);
		}
		else if (nhPath.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.nhPath");
    		erb.setProposedSolution("Enter nhPath");
    		erbs.add(erb);
    	}

		if (collabId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("collabId"); 
			erb.setProposedSolution("You must enter an Existing Collaboration ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 		

    	if (erbs.size() == 0)
    	{
       	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();

    		ArrayList<Whiteboard> wbs;
        	wbs = UserManagement.userGetNeighborhoodCollaborationWhiteboards(email, nhPath, collabId, ErrResps, authBase64String);
       	 	
        	System.out.println("wbs.size :"+ wbs.size());
        	System.out.println("ErrResps.size :"+ ErrResps.size());
        	
        	if (wbs.size() > 0)
        		return Response.ok().entity(wbs).build();
    		else
    		{
    			return Response.ok().entity(ErrResps).build();
    			//return Response.status(500).entity(ErrResps).build();
    		}
    	}
    	else
    	{
        	return Response.ok().entity(erbs).build();
    	}    	
    	    	
        //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    
    
    //GET  /user/{email}/neighborhood/{nhPath}/collaboration/{collabId}/whiteboard/{whiteboardId}/grids
    @Override
	public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridsGet(String email, String nhPath, Integer collabId, Integer whiteboardId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
		// TODO Auto-generated method stub

    	System.out.println("email : " + email);
      	System.out.println("nhPath : " + nhPath);
      	System.out.println("collabId : " + collabId);
      	System.out.println("whiteboardId : " + whiteboardId);

		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
			
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}

		if (email == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
		}
		else if (email.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
    	}

		if (nhPath == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.nhPath");
    		erb.setProposedSolution("Enter nhPath");
    		erbs.add(erb);
		}
		else if (nhPath.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.nhPath");
    		erb.setProposedSolution("Enter nhPath");
    		erbs.add(erb);
    	}

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
       	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();

    		ArrayList<GridNames> grids;
        	grids = UserManagement.userGetNeighborhoodCollaborationWhiteboardGrids(email, nhPath, collabId, whiteboardId,  ErrResps, authBase64String);
       	 	
        	System.out.println("wbs.size :"+ grids.size());
        	System.out.println("ErrResps.size :"+ ErrResps.size());
        	
        	if (grids.size() > 0)
        		return Response.ok().entity(grids).build();
    		else
    		{
    			return Response.ok().entity(ErrResps).build();
    			//return Response.status(500).entity(ErrResps).build();
    		}
    	}
    	else
    	{
        	return Response.ok().entity(erbs).build();
    	}    	
    	    	
    	
    	//return null;
	}

    //GET	......../user/{email}/neighborhood/{nhPath}/collaboration/{collabId}/whiteboard/{whiteboardId}/grid/{gridId}
     @Override
    public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridGridIdGet(String email, String nhPath, Integer collabId, Integer whiteboardId, Integer gridId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
       	System.out.println("email : " + email);
       	System.out.println("nhPath : " + nhPath);
       	System.out.println("collabId : " + collabId);
       	System.out.println("whiteboardId : " + whiteboardId);
       	System.out.println("gridId : " + gridId);

 		ErrorRequestObject erb;
 		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

 		//System.out.println("authBase64String : " + authBase64String);
 			
 		if (authBase64String == null)
 		{	
 			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
 			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
 			erbs.add(erb);
 		}

 		if (email == null)
 		{
     		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.email");
     		erb.setProposedSolution("Enter Email");
     		erbs.add(erb);
 		}
 		else if (email.trim().equals(""))
     	{	
     		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email");
     		erb.setProposedSolution("Enter Email");
     		erbs.add(erb);
     	}

 		if (nhPath == null)
 		{
     		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.nhPath");
     		erb.setProposedSolution("Enter nhPath");
     		erbs.add(erb);
 		}
 		else if (nhPath.trim().equals(""))
     	{	
     		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.nhPath");
     		erb.setProposedSolution("Enter nhPath");
     		erbs.add(erb);
     	}

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
 		
 		if (gridId <= 0)
 		{	
 			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("gridId"); 
 			erb.setProposedSolution("You must enter an Existing Grid ID. It should be a Positive Number.");
 			erbs.add(erb);
 		}
 		
     	if (erbs.size() == 0)
     	{
    	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();

     		GridInfo gi;
         	gi = UserManagement.userGetNeighborhoodCollaborationWhiteboardGrid(email, nhPath, collabId, whiteboardId, gridId, ErrResps, authBase64String);
        	 	
         	System.out.println("gi.name :"+ gi.getName() );
         	System.out.println("ErrResps.size :"+ ErrResps.size());
         	
         	if (gi != null)
         		return Response.ok().entity(gi).build();
     		else
     		{
     			return Response.ok().entity(ErrResps).build();
     			//return Response.status(500).entity(ErrResps).build();
     		}
     	}
     	else
     	{
         	return Response.ok().entity(erbs).build();
     	}    	        
     	// do some magic!
     	// return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

     
     //GET.....  /user/{email}/neighborhood/{nhPath}/collaboration/{collabId}/whiteboard/{whiteboardId}/gridchain/{gridId}
     @Override
     public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridchainGridIdGet(String email, String nhPath, Integer collabId, Integer whiteboardId, Integer gridId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
         // do some magic!
         return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
     }
     
    //	/user/{email}/neighborhood/{nhPath}/collaborations
    @Override
    public Response userEmailNeighborhoodNhPathCollaborationsGet(String email, String nhPath, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
    	
      	System.out.println("email : " + email);
      	System.out.println("nhPath : " + nhPath);

		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
			
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}

		if (email == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
		}
		else if (email.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
    	}

		if (nhPath == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.nhPath");
    		erb.setProposedSolution("Enter nhPath");
    		erbs.add(erb);
		}
		else if (nhPath.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.nhPath");
    		erb.setProposedSolution("Enter nhPath");
    		erbs.add(erb);
    	}
		
    	if (erbs.size() == 0)
    	{
       	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();

    		ArrayList<Collaboration> cl;
        	cl = UserManagement.userGetNeighborhoodCollaborations(email, nhPath, ErrResps, authBase64String);
       	 	
        	System.out.println("cl.size :"+ cl.size());
        	System.out.println("ErrResps.size :"+ ErrResps.size());
        	
        	if (cl.size() > 0)
        		return Response.ok().entity(cl).build();
    		else
    		{
    			return Response.ok().entity(ErrResps).build();
    			//return Response.status(500).entity(ErrResps).build();
    		}
    	}
    	else
    	{
        	return Response.ok().entity(erbs).build();
    	}    	
    	
        //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response userGet( Boolean active, SecurityContext securityContext, String authBase64String) throws NotFoundException {
    	System.out.println("active : " + active);

    	BoardwalkConnection bwcon = null;
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
			
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		ArrayList<Integer> memberNh = new ArrayList<Integer>();
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();
    		}
    	}
		
    	if (erbs.size() == 0)
    	{
       	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();

    		ArrayList<User> ul;
        	ul = UserManagement.userGet(active, ErrResps, bwcon);
       	 	
        	System.out.println("ul.size :"+ ul.size());
        	System.out.println("ErrResps.size :"+ ErrResps.size());
        	
        	if (ul.size() > 0)
        		return Response.status(200).entity(ul).build();				// success list of active/inactive users
    		else
    		{
    			return Response.status(500).entity(ErrResps).build();		// something has gone wrong on the website's server, but the server could not be more specific on what the exact problem is.
    		}
    	}
    	else
    	{
        	return Response.status(400).entity(erbs).build();
    	}
		
  //      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response userPost(User user, SecurityContext securityContext, String authBase64String) throws NotFoundException {
    	
    	BoardwalkConnection bwcon = null;
    	
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		ErrorRequestObject erb;

    	//System.out.println("authBase64String : " + authBase64String);
		
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		ArrayList<Integer> memberNh = new ArrayList<Integer>();
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();
    		}
    	}

		String Email = user.getEmail();
		String ExternalId = user.getExternalId();
		String FirstName = user.getFirstName();
		String LastName = user.getLastName();
		String Password = user.getPassword();
		
		if (Email == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
		}
		else if (Email.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
    	}
		
		if (ExternalId == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.ExternalId");
    		erb.setProposedSolution("Enter ExternalId");
    		erbs.add(erb);
		}
		else if (ExternalId.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.ExternalId"); 
    		erb.setProposedSolution("Enter ExternalId");
    		erbs.add(erb);
    	}
		
		if (FirstName == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.FirstName");
    		erb.setProposedSolution("Enter FirstName");
    		erbs.add(erb);
		}
		else if (FirstName.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.FirstName");
    		erb.setProposedSolution("Enter First Name");
    		erbs.add(erb);
    	}
    	
		if (LastName == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.LastName");
    		erb.setProposedSolution("Enter LastName");
    		erbs.add(erb);
		}
		else if (LastName.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.LastName"); 
    		erb.setProposedSolution("Enter Last Name");
    		erbs.add(erb);
    	}
    	
		if (Password == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.Password");
    		erb.setProposedSolution("Enter Password");
    		erbs.add(erb);
		}
    	else if (Password.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.Password"); 
    		erb.setProposedSolution("Enter Password");
    		erbs.add(erb);
    	}

    	if (erbs.size() == 0)
    	{
       	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
    		ArrayList<User> ul;
        	ul = UserManagement.userPost(user, ErrResps, bwcon);
        	System.out.println("ul.size :"+ ul.size());
        	System.out.println("ErrResps.size :"+ ErrResps.size());
        	
        	if (ul.size() > 0)
        		return Response.status(200).entity(ul).build();		//Successfully created user
    		else
    		{
    			return Response.status(409).entity(ErrResps).build();	//409 Conflict. The request could not be completed due to a conflict with the current state of the target resource. This code is used in situations where the user might be able to resolve the conflict and resubmit the request.
    		}
    	}
    	else
    	{
        	return Response.status(400).entity(erbs).build();
    	}
    }


    @Override
    public Response userPut(User user, SecurityContext securityContext, String authBase64String) throws NotFoundException {
    	
    	BoardwalkConnection bwcon = null;
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
			
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		ArrayList<Integer> memberNh = new ArrayList<Integer>();
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();
    		}
    	}

		String Email = user.getEmail();
		String ExternalId = user.getExternalId();
		String FirstName = user.getFirstName();
		String LastName = user.getLastName();
		String Password = user.getPassword();

		if (Email == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
		}
		else if (Email.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
    	}
		
		if (ExternalId == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.ExternalId");
    		erb.setProposedSolution("Enter ExternalId");
    		erbs.add(erb);
		}
		else if (ExternalId.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.ExternalId"); 
    		erb.setProposedSolution("Enter ExternalId");
    		erbs.add(erb);
    	}
		
		if (FirstName == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.FirstName");
    		erb.setProposedSolution("Enter FirstName");
    		erbs.add(erb);
		}
		else if (FirstName.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.FirstName");
    		erb.setProposedSolution("Enter First Name");
    		erbs.add(erb);
    	}
    	
		if (LastName == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.LastName");
    		erb.setProposedSolution("Enter LastName");
    		erbs.add(erb);
		}
		else if (LastName.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.LastName"); 
    		erb.setProposedSolution("Enter Last Name");
    		erbs.add(erb);
    	}
    	
		if (Password == null)
		{
    		erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("User.Password");
    		erb.setProposedSolution("Enter Password");
    		erbs.add(erb);
		}
    	else if (Password.trim().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.Password"); 
    		erb.setProposedSolution("Enter Password");
    		erbs.add(erb);
    	}

    	if (erbs.size() == 0)
    	{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
			String o;
			o = UserManagement.userPut(user, ErrResps, bwcon);
  			if (ErrResps.size() > 0)
  				return Response.status(409).entity(ErrResps).build();   	//The request could not be completed due to a conflict with the current state of the target resource. This code is used in situations where the user might be able to resolve the conflict and resubmit the request.
    		else
    		{
		        return Response.status(200).entity(o).build();
    		}
		}
    	else
    	{
        	return Response.status(400).entity(erbs).build();						//	400, message = "Invalid input (Bad Request)",
    	}
        // do some magic!
//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response userUserIdDelete(Integer userId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
    	BoardwalkConnection bwcon = null;
    	
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
		
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		ArrayList<Integer> memberNh = new ArrayList<Integer>();
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();
    		}
    	}
		
		if (userId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegativeOrZero"); erb.setPath("userId"); 
			erb.setProposedSolution("UserId must be Positive Integer");
			erbs.add(erb);
		}

    	if (erbs.size() == 0)
    	{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	    	String o;
			o = UserManagement.userUserIdDelete(userId, ErrResps, bwcon);

	    	if (ErrResps.size() == 0)
	        	return Response.status(200).entity(o).build();				//successfully de-activated user
	    	else
	    		return Response.status(404).entity(ErrResps).build();   	//user not found
    	}
    	else
    	{
        	return Response.status(400).entity(erbs).build();				//bad request
    	}
    }
    
    @Override
    public Response userUserIdGet(Integer userId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!

    	BoardwalkConnection bwcon = null;
    	ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

    	//System.out.println("authBase64String : " + authBase64String);

    	if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
			return Response.status(401).entity(erbs).build();
		}
    	else
    	{
    		ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
        	//Connection connection = null;
    		
    		ArrayList<Integer> memberNh = new ArrayList<Integer>();
    		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
    		if (!ErrResps.isEmpty())
    		{
    			return Response.status(401).entity(ErrResps).build();
    		}
    	}
		 
		
    	if (userId <= 0)
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsNegativeOrZero"); erb.setPath("userId"); 
    		erb.setProposedSolution("UserId must be Positive Integer");
    		erbs.add(erb);
    	}
		 
    	if (erbs.size() == 0)
    	{
	 	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	    	User user = UserManagement.userUserIdGet(userId, ErrResps, bwcon);
	    	
	    	if (user != null)
	        	return Response.status(200).entity(user).build();
	    	else
	    		return Response.status(404).entity(ErrResps).build();   	
    	}
    	else
    	{
        	return Response.status(400).entity(erbs).build();
    	}
    }







/*	@Override
	public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridGridIdGet(String email,
			String nhPath, Integer collabId, Integer whiteboardId, Integer gridId, SecurityContext securityContext,
			String authBase64String) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}*/
}
