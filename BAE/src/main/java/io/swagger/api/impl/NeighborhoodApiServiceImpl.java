package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.Collaboration;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.Member;
import io.swagger.model.Neighborhood;
import io.swagger.model.Relation;

import java.util.ArrayList;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;

import boardwalk.rest.NeighborhoodManagement;
import boardwalk.rest.UserManagement; 
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-10T09:36:40.808Z")
public class NeighborhoodApiServiceImpl extends NeighborhoodApiService {
    @Override
    public Response neighborhoodGet( String neighborhoodSpec, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
   
    
//  @GET
//  @Path("/{nhId}/collaboration")
	@Override
	public Response neighborhoodNhIdCollaborationGet(Integer nhId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
      // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}

		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("You must enter an Existing Neighborhood ID. It should be a Positive Number.");
			erbs.add(erb);
		}

	   	if (erbs.size() == 0)
	   	{
   			ArrayList<Collaboration> collabList;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	String retMsg = "";
	  	 	collabList = NeighborhoodManagement.neighborhoodNhIdCollaborationGet(nhId, ErrResps ,authBase64String);
	    	
	    	if (ErrResps.size() > 0)
	    		return Response.ok().entity(ErrResps).build();   	
	    	else
	        	return Response.ok().entity(collabList).build();
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
//      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
    
  
//  @DELETE
//  @Path("/{nhId}/member/{memberId}")
    @Override
    public Response neighborhoodNhIdDelete(Integer nhId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
   		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		System.out.println("nhId ->" + nhId);

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}
		
		if (nhId <= 0)
  		{	
  			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
  			erb.setProposedSolution("You must enter an Existing Neighborhood ID. It should be a Positive Number.");
  			erbs.add(erb);
  		}

  	   	if (erbs.size() == 0)
  	   	{
  	   			ArrayList<Neighborhood> nhList;
  		  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
  		  	 	String retMsg = "";
  		  	 	retMsg = NeighborhoodManagement.neighborhoodNhIdDelete(nhId, ErrResps ,authBase64String);
  		    	
  		    	if (ErrResps.size() > 0)
  		    		return Response.ok().entity(ErrResps).build();   	
  		    	else
  		        	return Response.ok().entity(retMsg).build();
  	   	}
  	   	else
  	   	{
  	       	return Response.ok().entity(erbs).build();
  	   	}    
//          return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();    
  	}
    

//	@GET
//	@Path("/{nhId}")
    @Override
    public Response neighborhoodNhIdGet(Integer nhId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
 		if (authBase64String == null)
 		{	
 			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
 			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
 			erbs.add(erb);
 		}
 		 
		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
 		{	
 			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
 			erb.setProposedSolution("You must enter an Existing Neighborhood ID. It should be a Positive Number.");
 			erbs.add(erb);
 		}

 	   	if (erbs.size() == 0)
 	   	{
   			ArrayList<Neighborhood> nhList;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	    	nhList = NeighborhoodManagement.neighborhoodNhIdGet(nhId, ErrResps ,authBase64String);
	    	
	    	if (nhList.size() > 0)
	        	return Response.ok().entity(nhList ).build();
	    	else
	    		return Response.ok().entity(ErrResps).build();   	
 	   	}
 	   	else
 	   	{
 	       	return Response.ok().entity(erbs).build();
 	   	}    
        //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();    }
    }


    //@GET
    //@Path("/{nhId}/member")
    @Override
    public Response neighborhoodNhIdMemberGet(Integer nhId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}

		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("You must enter an Existing Neighborhood ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		 
	   	if (erbs.size() == 0)
	   	{
			ArrayList<Member> memberList;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	memberList = NeighborhoodManagement.neighborhoodNhIdMemberGet(nhId, ErrResps ,authBase64String);
	    	
	    	if (memberList.size() > 0)
	        	return Response.ok().entity(memberList).build();
	    	else
	    		return Response.ok().entity(ErrResps).build();   	
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
        //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    
    //@POST
    //@Path("/{nhId}/member/{memberId}/collaboration")
    @Override
    public Response neighborhoodNhIdMemberMemberIdCollaborationPost(Integer nhId, Integer memberId, Collaboration collaboration, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}

		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("You must enter an Existing Neighborhood ID. It should be a Positive Number.");
			erbs.add(erb);
		}

		if (memberId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("memberId"); 
			erb.setProposedSolution("You must enter an Existing Member ID. It should be a Positive Number.");
			erbs.add(erb);
		}
		String collabName = null;
		String collabPurpose = null;
		collabName = collaboration.getName();
		collabPurpose = collaboration.getPurpose();
		
	  	if (collabName == null)
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("Collaboration.Name"); 
			erb.setProposedSolution("Collaboration Name is Missing in the Requet. Provide Collaboration Name");
			erbs.add(erb);
	  	}
	  	else if (collabName.trim().equals(""))
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Collaboration.Name"); 
			erb.setProposedSolution("Collaboration Name cannot be Blank.");
			erbs.add(erb);
	  	}

	  	if (collabPurpose == null)
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("Collaboration.Purpose"); 
			erb.setProposedSolution("Collaboration Purpose is Missing in the Request. Provide Collaboration Purpose");
			erbs.add(erb);
	  	}
	  	else if (collabPurpose.trim().equals(""))
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Collaboration.Purpose"); 
			erb.setProposedSolution("Collaboration Purpose cannot be Blank.");
			erbs.add(erb);
	  	}
	  	
	   	if (erbs.size() == 0)
	   	{
	   			int collabId =  -1;
		  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
		  	 	collabId = NeighborhoodManagement.neighborhoodNhIdMemberMemberIdCollaborationPost(nhId, memberId, collaboration, ErrResps ,authBase64String);
		    	if (ErrResps.size() > 0)
		    		return Response.ok().entity(ErrResps).build();   	
		    	else
		        	return Response.ok().entity(collabId).build();
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
    	//return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }


    //@DELETE
    // @Path("/{nhId}/member/{memberId}")
    @Override
    public Response neighborhoodNhIdMemberMemberIdDelete(Integer nhId, Integer memberId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}
		 
		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("The Neighborhood you are deleting a Member from must exists. It should be a Positive Number.");
			erbs.add(erb);
		}

		if (memberId <= 0)
		{
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("member.userId"); 
			erb.setProposedSolution("You must enter an Existing Member ID for deleting the Membership of Neighborhood. It should be a Positive Number.");
			erbs.add(erb);
		}

		if (erbs.size() == 0)
	   	{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	String msgRet;
	  	 	msgRet = NeighborhoodManagement.neighborhoodNhIdMemberMemberIdDelete(nhId, memberId, ErrResps ,authBase64String);
	    	
	    	if (ErrResps.size() > 0)
	    		return Response.ok().entity(ErrResps).build();   	
	    	else
   				return Response.ok().entity(msgRet).build();
	   	}
    	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
        //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    
    //@POST
    //@Path("/{nhId}/member")
    @Override
    public Response neighborhoodNhIdMemberPost(Integer nhId, Member member, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}
		
		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("The Neighborhood you are eadding a Member must exists. It should be a Positive Number.");
			erbs.add(erb);
		}
		
		if (member.getUserId() <= 0 )
		{
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("member.userId"); 
			erb.setProposedSolution("You must enter an Existing User ID for adding a Membership to Neighborhood. It should be a Positive Number.");
			erbs.add(erb);
		}
	   	
		if (erbs.size() == 0)
	   	{
			ArrayList<Member> memberList;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	memberList = NeighborhoodManagement.neighborhoodNhIdMemberPost(nhId, member, ErrResps ,authBase64String);
	    	
	    	if (memberList.size() > 0)
	        	return Response.ok().entity(memberList).build();
	    	else
	    		return Response.ok().entity(ErrResps).build();   	
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
        //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    
    //@DELETE
    //@Path("/{nhId}/relation")
    @Override
    public Response neighborhoodNhIdRelationDelete(Integer nhId,  @NotNull String relation, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!

		System.out.println("neighborhoodNhIdRelationDelete");
		System.out.println("nhId :" + nhId);
		System.out.println("relation :" + relation);
		//System.out.println("authBase64String :" + authBase64String);
		
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}
		 
		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("The Neighborhood Id you are Deleting Relations of must exist. It should be a Positive Number.");
			erbs.add(erb);
		}

		if (relation == null)
		{
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("relation"); 
			erb.setProposedSolution("Relation Name is Missing in the Request. Provide Relation Name");
			erbs.add(erb);
		}
		else if (relation.trim().equals(""))
		{
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("relation"); 
			erb.setProposedSolution("Relation Name Cannot be Blank. ");
			erbs.add(erb);
		}

		if (erbs.size() == 0)
	   	{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	String msgRet;
	  	 	msgRet = NeighborhoodManagement.neighborhoodNhIdRelationDelete(nhId, relation, ErrResps, authBase64String);

	    	if (ErrResps.size() > 0)
	    		return Response.ok().entity(ErrResps).build();   	
	    	else
   				return Response.ok().entity(msgRet).build();
	   	}
    	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    		
      //  return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    //@GET
    //@Path("/{nhId}/relation")
    @Override
    public Response neighborhoodNhIdRelationGet(Integer nhId, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}
		 
		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("The Neighborhood Id to get Relations must exist. It should be a Positive Number.");
			erbs.add(erb);
		}

		if (erbs.size() == 0)
	   	{
			ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
			ArrayList <Relation> rels;
			rels = NeighborhoodManagement.neighborhoodNhIdRelationGet(nhId, ErrResps, authBase64String );
			
			if (ErrResps.size() > 0)
				return Response.ok().entity(ErrResps).build();   	
			else
				return Response.ok().entity(rels).build();
	   	}
    	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
    	//return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    
    //@POST
    //@Path("/{nhId}/relation")
    @Override
    public Response neighborhoodNhIdRelationPost(Integer nhId, Relation relationship, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}
		 
		System.out.println("nhId ->" + nhId);
		if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("The Neighborhood you are setting Relation must exists. It should be a Positive Number.");
			erbs.add(erb);
		}

		if (relationship == null)
		{
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("relationship"); 
			erb.setProposedSolution("Relationship Details are Missing in the Request. Provide Relationship Details");
			erbs.add(erb);
		}
		else if (relationship.getName() == null)
		{
			erb = new ErrorRequestObject(); erb.setError("IsMissing"); erb.setPath("relationship.name"); 
			erb.setProposedSolution("Missing Relation Name. Provide Relation Name.");
			erbs.add(erb);
		}
		else if (relationship.getName().trim().equals(""))
		{
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("relationship.name"); 
			erb.setProposedSolution("Relation Name Cannot be Blank. ");
			erbs.add(erb);
		}

		List <Neighborhood> nhList = relationship.getRelatedNeighborhoodId();
		 
		Neighborhood nh;
		for(int index=0 ; index < nhList.size(); index +=1)
		{
			nh = nhList.get(index);
			if (nh.getId() <= 0)
			{
				erb = new ErrorRequestObject(); erb.setError("IsNegativeOrZero"); erb.setPath("relationship.nhList[" + index + "].Id = " + nh.getId() ); 
				erb.setProposedSolution("Neighborhood ID must be a Positive Number of an Existing Neighborhod. ");
				erbs.add(erb);
			}
		}
		 
		if (erbs.size() == 0)
	   	{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	String msgRet;
	  	 	msgRet = NeighborhoodManagement.neighborhoodNhIdRelationPost(nhId, relationship, ErrResps, authBase64String);
	    	
	    	if (ErrResps.size() > 0)
	    		return Response.ok().entity(ErrResps).build();   	
	    	else
   				return Response.ok().entity(msgRet).build();
	   	}
    	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
    }
    
    
    //@POST
    @Override
    public Response neighborhoodPost(Neighborhood neighborhood, SecurityContext securityContext, String authBase64String) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();

		if (authBase64String == null)
		{	
			erb = new ErrorRequestObject(); erb.setError("Missing Authorization in Header"); erb.setPath("Header:Authorization"); 
			erb.setProposedSolution("Authorization Header should contain user:pwd:nhPath as Base64 string");
			erbs.add(erb);
		}
		 
		String nhName = neighborhood.getName();
		Long objL = new Long(neighborhood.getParentId());
		int parentNhId = objL.intValue();
		boolean secure = neighborhood.isSecure();
		
		System.out.println("nhName ->" + nhName);

		if (nhName.trim().equals(""))
		{	
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Neighborhood.Name"); 
			erb.setProposedSolution("Neighborhood name cannot be Blank");
			erbs.add(erb);
		}
		 
		if (parentNhId != -1 &&  parentNhId <= 0)			// -1 used to create Neighborhood Level 0
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("Neighborhood.parentNhId"); 
			erb.setProposedSolution("Parent Neighbohood Id must be either -1 OR existing Neighborhood Id.");
			erbs.add(erb);
		}
		
	   	if (erbs.size() == 0)
	   	{
   			ArrayList<Neighborhood> nhList;
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	    	nhList = NeighborhoodManagement.neighborhoodPost(nhName, parentNhId, secure, ErrResps, authBase64String);
	    	
	    	if (nhList.size() > 0)
	        	return Response.ok().entity(nhList ).build();
	    	else
	    		return Response.ok().entity(ErrResps).build();   	
	   	}
	   	else
	   	{
	       	return Response.ok().entity(erbs).build();
	   	}    
       //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
