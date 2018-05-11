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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-05T04:57:22.014Z")
public class NeighborhoodApiServiceImpl extends NeighborhoodApiService {
    @Override
    public Response neighborhoodGet( String neighborhoodSpec, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
   
    
//  @GET
//  @Path("/{nhId}/collaboration")
  @Override
  public Response neighborhoodNhIdCollaborationGet(Integer nhId, SecurityContext securityContext) throws NotFoundException {
      // do some magic!
		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		 
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
		  	 	collabList = NeighborhoodManagement.neighborhoodNhIdCollaborationGet(nhId, ErrResps);
		    	
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
    public Response neighborhoodNhIdDelete(Integer nhId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
   		ErrorRequestObject erb;
  		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
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
  		  	 	String retMsg = "";
  		  	 	retMsg = NeighborhoodManagement.neighborhoodNhIdDelete(nhId, ErrResps);
  		    	
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
    public Response neighborhoodNhIdGet(Integer nhId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
 		ErrorRequestObject erb;
 		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
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
 		    	nhList = NeighborhoodManagement.neighborhoodNhIdGet(nhId, ErrResps);
 		    	
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
    public Response neighborhoodNhIdMemberGet(Integer nhId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
 		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
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
		  	 	memberList = NeighborhoodManagement.neighborhoodNhIdMemberGet(nhId, ErrResps);
		    	
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
    public Response neighborhoodNhIdMemberMemberIdCollaborationPost(Integer nhId, Integer memberId, Collaboration collaboration, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        // do some magic!
 		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
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
		 
	  	if (collaboration.getName().trim().equals(""))
	  	{
			erb = new ErrorRequestObject(); erb.setError("IsBlank"); erb.setPath("Collaboration.Name"); 
			erb.setProposedSolution("Collaboration Name cannot be Blank.");
			erbs.add(erb);
	  	}

	   	if (erbs.size() == 0)
	   	{
	   			int collabId =  -1;
		  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
		  	 	collabId = NeighborhoodManagement.neighborhoodNhIdMemberMemberIdCollaborationPost(nhId, memberId, collaboration, ErrResps);
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
    public Response neighborhoodNhIdMemberMemberIdDelete(Integer nhId, Integer memberId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
 		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
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
		  	 	msgRet = NeighborhoodManagement.neighborhoodNhIdMemberMemberIdDelete(nhId, memberId, ErrResps);
		    	
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
    public Response neighborhoodNhIdMemberPost(Integer nhId, Member member, SecurityContext securityContext) throws NotFoundException {
        // do some magic!

 		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
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
		  	 	memberList = NeighborhoodManagement.neighborhoodNhIdMemberPost(nhId, member, ErrResps);
		    	
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

    @Override
    public Response neighborhoodNhIdRelationDelete(Integer nhId,  @NotNull Integer relation, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    //@GET
    //@Path("/{nhId}/relation")
    @Override
    public Response neighborhoodNhIdRelationGet(Integer nhId, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
 		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		 System.out.println("nhId ->" + nhId);
		 if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("The Neighborhood you are setting Relation must exists. It should be a Positive Number.");
			erbs.add(erb);
		}

		 if (erbs.size() == 0)
	   	{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	ArrayList <Relation> rels;
	  	 	rels = NeighborhoodManagement.neighborhoodNhIdRelationGet(nhId, ErrResps );
	    	
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
    public Response neighborhoodNhIdRelationPost(Integer nhId, Relation relationship, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
 		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		 System.out.println("nhId ->" + nhId);
		 if (nhId <= 0)
		{	
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("nhId"); 
			erb.setProposedSolution("The Neighborhood you are setting Relation must exists. It should be a Positive Number.");
			erbs.add(erb);
		}

		 if (relationship.getName().trim().equals(""))
		{
			erb = new ErrorRequestObject(); erb.setError("IsNegative"); erb.setPath("member.userId"); 
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
				erb = new ErrorRequestObject(); erb.setError("IsNegativeOrZero"); erb.setPath("relationship.nhList" + index + ".Id"); 
				erb.setProposedSolution("Neighborhood ID must be a Positive Number of an Existing Neighborhod. ");
				erbs.add(erb);
			 }
		 }
		 
		 if (erbs.size() == 0)
	   	{
	  	 	ArrayList <ErrorRequestObject> ErrResps = new ArrayList<ErrorRequestObject>();
	  	 	String msgRet;
	  	 	msgRet = NeighborhoodManagement.neighborhoodNhIdRelationPost(nhId, relationship, ErrResps);
	    	
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
    public Response neighborhoodPost(Neighborhood neighborhood, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
		ErrorRequestObject erb;
		 ArrayList <ErrorRequestObject> erbs = new ArrayList<ErrorRequestObject>();
		 
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
	    	nhList = NeighborhoodManagement.neighborhoodPost(nhName, parentNhId, secure, ErrResps);
	    	
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
