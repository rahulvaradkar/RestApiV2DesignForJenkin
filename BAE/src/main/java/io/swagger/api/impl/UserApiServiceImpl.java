package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.ErrorRequestObject;
import io.swagger.model.User;

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
}
