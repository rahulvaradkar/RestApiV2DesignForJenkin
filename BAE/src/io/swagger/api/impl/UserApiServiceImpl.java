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

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-04-30T03:35:16.055Z")
public class UserApiServiceImpl extends UserApiService {
    @Override
    public Response userGet( Boolean active, SecurityContext securityContext) throws NotFoundException {
    	System.out.println("active : " + active);

		 //No check for API Parameters. As true or false is read. so <ErrorRequestObject erb> IS NOT USED
		//ErrorRequestObject erb;
		// ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

   	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
   	 
		ArrayList<User> ul;
    	ul = UserManagement.userGet(active, ErrResps);

    	System.out.println("ul.size :"+ ul.size());
    	System.out.println("ErrResps.size :"+ ErrResps.size());
    	
    	if (ul.size() > 0)
    		return Response.ok().entity(ul).build();
		else
		{
			return Response.ok().entity(ErrResps).build();
		}
  //      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response userPost(User user, SecurityContext securityContext) throws NotFoundException {
    	
    	System.out.println("user.getEmail()->" + user.getEmail());
    	System.out.println("user.getExternalId()->" + user.getExternalId() );
    	System.out.println("user.getFirstName()->" + user.getFirstName() );
    	System.out.println("user.getLastName()->" + user.getLastName() );
    	System.out.println("user.getPassword()->" + user.getPassword());
    	System.out.println("user.getId()->" + user.getId());
    	
		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
    	if (user.getEmail().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email");
    		erb.setProposedSolution("Enter Email");
    		erbs.add(erb);
    	}
    	if (user.getExternalId().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.ExternalId"); 
    		erb.setProposedSolution("Enter ExternalId");
    		erbs.add(erb);
    	}
    	if (user.getFirstName().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.FirstName");
    		erb.setProposedSolution("Enter First Name");
    		erbs.add(erb);
    	}
    	if (user.getLastName().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.LastName"); 
    		erb.setProposedSolution("Enter Last Name");
    		erbs.add(erb);
    	}
    	if (user.getPassword().equals(""))
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.Password"); 
    		erb.setProposedSolution("Enter Password");
    		erbs.add(erb);
    	}

    	if (erbs.size() == 0)
    	{
       	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
       	 
    		ArrayList<User> ul;
        	ul = UserManagement.userPost(user, ErrResps);
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
    public Response userPut(User user, SecurityContext securityContext) throws NotFoundException {
    	
		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

	    	if (user.getEmail().equals(""))
	    	{	
	    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.email"); 
	    		erb.setProposedSolution("Enter Email");
	    		erbs.add(erb);
	    	}
	    	if (user.getExternalId().equals(""))
	    	{	
	    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.ExternalId");
	    		erb.setProposedSolution("Enter ExternalId");

	    		erbs.add(erb);
	    	}
	    	if (user.getFirstName().equals(""))
	    	{	
	    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.FirstName");
	    		erb.setProposedSolution("Enter First Name");
	    		erbs.add(erb);
	    	}
	    	if (user.getLastName().equals(""))
	    	{	
	    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.LastName"); 
	    		erb.setProposedSolution("Enter Last Name");
	    		erbs.add(erb);
	    	}
	    	if (user.getPassword().equals(""))
	    	{	
	    		erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("User.Password");
	    		erb.setProposedSolution("Enter Password");
	    		erbs.add(erb);
	    	}

	    	if (erbs.size() == 0)
	    	{
		   	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
				 
				String o;
				o = UserManagement.userPut(user, ErrResps);

	        	System.out.println("After calling UserManagement.userPut o:"+ o);
	        	System.out.println("ErrResps.size :"+ ErrResps.size());

	        	if (ErrResps.size() > 0)
	    			return Response.ok().entity(ErrResps).build();
	    		else
	    		{
			        return Response.ok().entity(o).build();
	    		}
	    	}
	    	else
	    	{
	        	return Response.ok().entity(erbs).build();
	    	}
		        
        // do some magic!
//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response userUserIdDelete(Integer userId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!

		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

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
			o = UserManagement.userUserIdDelete(userId, ErrResps);

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
    public Response userUserIdGet(Integer userId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
//    	UserManagement um = new UserManagement();

		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		 
		 if (userId <= 0)
    	{	
    		erb = new ErrorRequestObject(); erb.setError("IsNegativeOrZero"); erb.setPath("userId"); 
    		erb.setProposedSolution("UserId must be Positive Integer");
    		erbs.add(erb);
    	}
		 
    	if (erbs.size() == 0)
    	{
	 	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	    	User user = UserManagement.userUserIdGet(userId, ErrResps);
	    	
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
