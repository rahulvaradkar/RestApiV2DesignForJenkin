package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.Collaboration;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.GridChain;
import io.swagger.model.GridInfo;
import io.swagger.model.Membership;
import io.swagger.model.ResponseInfo;
import io.swagger.model.User;
import io.swagger.model.Whiteboard;

import java.util.ArrayList;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.client.authentication.ResponseAuthenticationException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.sun.mail.imap.protocol.Status;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;

import boardwalk.rest.UserManagement;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-09T04:12:45.675Z")
public class UserApiServiceImpl extends UserApiService {
	
	//  /user/{email}/membership
    @Override
    public Response userEmailMembershipsGet(String email, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
    	
       	System.out.println("email : " + email);

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
    		    		
        	if (erbs.size() == 0)
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

		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
			
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}

    	if (erbs.size() == 0)
    	{
       	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();

    		ArrayList<User> ul;
        	ul = UserManagement.userGet(active, ErrResps, authBase64String);
       	 	
        	System.out.println("ul.size :"+ ul.size());
        	System.out.println("ErrResps.size :"+ ErrResps.size());
        	
        	if (ul.size() > 0)
        		return Response.ok().entity(ul).build();
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
		
  //      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response userPost(User user, SecurityContext securityContext, String authBase64String) throws NotFoundException {
    	
/*    	System.out.println("user.getEmail()->" + user.getEmail());
    	System.out.println("user.getExternalId()->" + user.getExternalId() );
    	System.out.println("user.getFirstName()->" + user.getFirstName() );
    	System.out.println("user.getLastName()->" + user.getLastName() );
    	System.out.println("user.getPassword()->" + user.getPassword());
    	System.out.println("user.getId()->" + user.getId());
*/    	
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		ErrorRequestObject erb;

    	//System.out.println("authBase64String : " + authBase64String);
		
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
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
        	ul = UserManagement.userPost(user, ErrResps, authBase64String);
        	System.out.println("ul.size :"+ ul.size());
        	System.out.println("ErrResps.size :"+ ErrResps.size());
        	
        	if (ul.size() > 0)
        		return Response.ok().entity(ul).build();
    		else
    		{
    			return Response.ok().entity(ErrResps).build();
    		}
    	}
    	else
    	{
        	return Response.ok().entity(erbs).build();
    	}
    }


    @Override
    public Response userPut(User user, SecurityContext securityContext, String authBase64String) throws NotFoundException {
    	
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
			
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
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

	    //@io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = String.class),
	    //@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
	    //@io.swagger.annotations.ApiResponse(code = 404, message = "User profile not found", response = String.class),
	    //@io.swagger.annotations.ApiResponse(code = 422, message = "Failed to Update User Profile. Reason could be trying to create Duplicate entities", response = ErrorRequestObject.class, responseContainer = "List") })
		
    	if (erbs.size() == 0)
    	{
    		ArrayList<Object> sections = new ArrayList <Object>();
//    		sections.add(500);
  //  		sections.add(ErrRsps);
	//        return Response.status(sections.get(0) ).entity(sections.get(1)).build();
    		
    		
	   	 	//ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();	// ORIGNAL WORKING
			String o;
			//o = UserManagement.userPut(user, ErrResps, authBase64String);		// Original	WORKING
			
			
			o = UserManagement.userPut(user, sections, authBase64String);

  			if (sections.size() > 0)
    			return Response.status((Integer)sections.get(0)).entity(sections.get(1)).build();
    		else
    		{
		        return Response.status(200).entity(o).build();
    		}
			
			/*        	ORIGINAL CODE ---- WORKING00
			
        	System.out.println("After calling UserManagement.userPut o:"+ o);
        	System.out.println("ErrResps.size :"+ ErrResps.size());

  			if (ErrResps.size() > 0)
    			return Response.ok().entity(ErrResps).build();
    		else
    		{
		        return Response.status(200).entity(o).build();
    		}
*/    	
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
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		//System.out.println("authBase64String : " + authBase64String);
		
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
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
			o = UserManagement.userUserIdDelete(userId, ErrResps, authBase64String);

	    	if (ErrResps.size() == 0)
	        	return Response.ok().entity(o).build();
	    	else
	    		return Response.ok().entity(ErrResps).build();   	
    	}
    	else
    	{
        	return Response.ok().entity(erbs).build();
    	}
    }
    
    @Override
    public Response userUserIdGet(Integer userId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
//    	UserManagement um = new UserManagement();

    	ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

    	//System.out.println("authBase64String : " + authBase64String);

    	if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
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
	    	User user = UserManagement.userUserIdGet(userId, ErrResps, authBase64String);
	    	
	    	if (user != null)
	        	return Response.ok().entity(user).build();
	    	else
	    		return Response.ok().entity(ErrResps).build();   	
    	}
    	else
    	{
        	return Response.ok().entity(erbs).build();
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
