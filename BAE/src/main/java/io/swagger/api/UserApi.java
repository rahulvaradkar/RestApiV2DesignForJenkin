package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.UserApiService;
import io.swagger.api.factories.UserApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.Collaboration;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.GridChain;
import io.swagger.model.GridInfo;
import io.swagger.model.Membership;
import io.swagger.model.ResponseInfo;
import io.swagger.model.User;
import io.swagger.model.Whiteboard;

import java.util.Map;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/user")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the user API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-09T04:12:45.675Z")
public class UserApi  {
   private final UserApiService delegate;

   public UserApi(@Context ServletConfig servletContext) {
      UserApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("UserApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (UserApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = UserApiServiceFactory.getUserApi();
      }

      this.delegate = delegate;
   }



   @GET
   @Path("/{email}/memberships")
   
   @Produces({ "application/json" })
   @io.swagger.annotations.ApiOperation(value = "Get all user memberships in the system", notes = "Gets all user memberships in the system. User can Get only his/her memberships details. So {email} and user in Authrization must match.", response = Membership.class, responseContainer = "List", authorizations = {
       @io.swagger.annotations.Authorization(value = "bwAuth")
   }, tags={ "Get list of user memberships", })
   @io.swagger.annotations.ApiResponses(value = { 
       @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Membership.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input", response = ErrorRequestObject.class, responseContainer = "List") })
   public Response userEmailMembershipGet(@ApiParam(value = "",required=true) @PathParam("email") String email
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
   throws NotFoundException {
       return delegate.userEmailMembershipsGet(email,securityContext, authBase64String);
   }
   @GET
   @Path("/{email}/neighborhood/{nhPath}/collaboration/{collabId}/whiteboards")
   
   @Produces({ "application/json" })
   @io.swagger.annotations.ApiOperation(value = "Get the list of all Whiteboards present in the Collaboration in Neighborhood that user can access using his/her Neighborhood memberships.", notes = "User can GET the list of Whiteboards present in the Collaboration using his/her memberships details for that Neighborhood. So {email} and user in Authrization must match. Also {nhPath} and nhPath in Authorization should match.", response = Whiteboard.class, responseContainer = "List", authorizations = {
       @io.swagger.annotations.Authorization(value = "bwAuth")
   }, tags={ "Get list of Whiteboard present in Collaboration that user can access", })
   @io.swagger.annotations.ApiResponses(value = { 
       @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Whiteboard.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 422, message = "Email not found. Authorization Failed. Neighborhood Path doesnot exists. Collaboration Id not found. Unauthorized Access.", response = ErrorRequestObject.class, responseContainer = "List") })
   public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardGet(@ApiParam(value = "",required=true) @PathParam("email") String email
,@ApiParam(value = "",required=true) @PathParam("nhPath") String nhPath
,@ApiParam(value = "",required=true) @PathParam("collabId") Integer collabId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
   throws NotFoundException {
       return delegate.userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardsGet(email,nhPath,collabId,securityContext, authBase64String);
   }
   @GET
   @Path("/{email}/neighborhood/{nhPath}/collaboration/{collabId}/whiteboard/{whiteboardId}/grids")
   
   @Produces({ "application/json" })
   @io.swagger.annotations.ApiOperation(value = "Get the list of all Grids present in the Whiteboard of the Collaboration in Neighborhood that user can access using his/her Neighborhood memberships.", notes = "User can GET the list of Grids present in the Whiteboard of the Collaboration using his/her memberships details for that Neighborhood. So {email} and user in Authrization must match. Also {nhPath} and nhPath in Authorization should match.", response = GridInfo.class, responseContainer = "List", authorizations = {
       @io.swagger.annotations.Authorization(value = "bwAuth")
   }, tags={ "Get list of Grids present in the Whiteboard of Collaboration that user can access", })
   @io.swagger.annotations.ApiResponses(value = { 
       @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = GridInfo.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 422, message = "Email not found. Authorization Failed. Neighborhood Path doesnot exists. Unauthorized Access.", response = ErrorRequestObject.class, responseContainer = "List") })
   public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridGet(@ApiParam(value = "",required=true) @PathParam("email") String email
,@ApiParam(value = "",required=true) @PathParam("nhPath") String nhPath
,@ApiParam(value = "",required=true) @PathParam("collabId") Integer collabId
,@ApiParam(value = "",required=true) @PathParam("whiteboardId") Integer whiteboardId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
   throws NotFoundException {
       return delegate.userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridsGet(email,nhPath,collabId,whiteboardId,securityContext, authBase64String);
   }
   @GET
   @Path("/{email}/neighborhood/{nhPath}/collaboration/{collabId}/whiteboard/{whiteboardId}/grid/{gridId}")
   
   @Produces({ "application/json" })
   @io.swagger.annotations.ApiOperation(value = "Get the Grid Details of teh Grid present in the Whiteboard of the Collaboration in Neighborhood that user can access using his/her Neighborhood memberships.", notes = "User can GET the Grid Information of the Grid present in the Whiteboard of the Collaboration using his/her memberships details for that Neighborhood. So {email} and user in Authrization must match. Also {nhPath} and nhPath in Authorization should match. The Grid information will have Column Names, sequence Number, Active/inactive & Access Control ( R/W ), Column Count, Row Count, Access Control for Add Row, Delete Row, Insert Column, Delete Column, Edit Data, Cuboid Properties. If user is the Owner then Accesss Control Cuboid information.", response = GridInfo.class, responseContainer = "List", authorizations = {
       @io.swagger.annotations.Authorization(value = "bwAuth")
   }, tags={ "Get Grid Information.", })
   @io.swagger.annotations.ApiResponses(value = { 
       @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = GridInfo.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 422, message = "Email not found. Authorization Failed. Neighborhood Path doesnot exists. Unauthorized Access.", response = ErrorRequestObject.class, responseContainer = "List") })
   public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridGridIdGet(@ApiParam(value = "",required=true) @PathParam("email") String email
,@ApiParam(value = "",required=true) @PathParam("nhPath") String nhPath
,@ApiParam(value = "",required=true) @PathParam("collabId") Integer collabId
,@ApiParam(value = "",required=true) @PathParam("whiteboardId") Integer whiteboardId
,@ApiParam(value = "",required=true) @PathParam("gridId") Integer gridId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
   throws NotFoundException {
       return delegate.userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridGridIdGet(email,nhPath,collabId,whiteboardId,gridId,securityContext, authBase64String);
   }

    @GET
    @Path("/{email}/neighborhood/{nhPath}/collaboration/{collabId}/whiteboard/{whiteboardId}/gridchain/{gridId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get the complete details of all Grid Components.", notes = "User can GET all Grid Component, it's status, History, Transactions done on entire grid. Information of the Grid present in the Whiteboard of the Collaboration using his/her memberships details for that Neighborhood. So {email} and user in Authrization must match. Also {nhPath} and nhPath in Authorization should match. The Grid information will have Column Names, sequence Number, Active/inactive & Access Control ( R/W ), Column Count, Row Count, Access Control for Add Row, Delete Row, Insert Column, Delete Column, Edit Data, Cuboid Properties. If user is the Owner then Accesss Control Cuboid information.", response = GridChain.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "Get Grid Chain Information.", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = GridChain.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ResponseInfo.class),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "Email not found. Authorization Failed. Neighborhood Path doesnot exists. Unauthorized Access.", response = ResponseInfo.class) })
    public Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridchainGridIdGet(@ApiParam(value = "",required=true) @PathParam("email") String email
,@ApiParam(value = "",required=true) @PathParam("nhPath") String nhPath
,@ApiParam(value = "",required=true) @PathParam("collabId") Integer collabId
,@ApiParam(value = "",required=true) @PathParam("whiteboardId") Integer whiteboardId
,@ApiParam(value = "",required=true) @PathParam("gridId") Integer gridId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridchainGridIdGet(email,nhPath,collabId,whiteboardId,gridId,securityContext, authBase64String);
    }

   @GET
   @Path("/{email}/neighborhood/{nhPath}/collaborations")
   
   @Produces({ "application/json" })
   @io.swagger.annotations.ApiOperation(value = "Get all Collaborations in Neighborhood that user can access using his/her Neighborhood memberships.", notes = "User can GET Collaboration using his/her memberships details for that Neighborhood. So {email} and user in Authrization must match. Also {nhPath} and nhPath in Authorization should match.", response = Collaboration.class, responseContainer = "List", authorizations = {
       @io.swagger.annotations.Authorization(value = "bwAuth")
   }, tags={ "Get list of collaborations that user can access", })
   @io.swagger.annotations.ApiResponses(value = { 
       @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Collaboration.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
       
       @io.swagger.annotations.ApiResponse(code = 422, message = "Email not found. Authorization Failed. Neighborhood Path doesnot exists. Unauthorized Access.", response = ErrorRequestObject.class, responseContainer = "List") })
   public Response userEmailNeighborhoodNhPathCollaborationGet(@ApiParam(value = "",required=true) @PathParam("email") String email
,@ApiParam(value = "",required=true) @PathParam("nhPath") String nhPath
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
   throws NotFoundException {
       return delegate.userEmailNeighborhoodNhPathCollaborationsGet(email,nhPath,securityContext, authBase64String);
   }
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get all users in the system", notes = "Gets all users in the system, can be filtered by active/inactive users", response = User.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "user", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = User.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response userGet(@ApiParam(value = "") @QueryParam("active") Boolean active
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.userGet(active,securityContext,authBase64String);
    }
    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create new user", notes = "", response = User.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "user", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "successful operation", response = User.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "Failed to Create New User", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response userPost(@ApiParam(value = "User creation details" ,required=true) User user
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.userPost(user,securityContext,authBase64String);
    }
    @PUT
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update user profile", notes = "", response = String.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "user", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "User profile not found", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "Failed to Update User Profile. Reason could be trying to create Duplicate entities", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response userPut(@ApiParam(value = "User creation details" ,required=true) User user
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.userPut(user,securityContext,authBase64String);
    }
    @DELETE
    @Path("/{userId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "De-activate user by ID", notes = "", response = String.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "user", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "User profile not found", response = String.class) })
    public Response userUserIdDelete(@ApiParam(value = "",required=true) @PathParam("userId") Integer userId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.userUserIdDelete(userId,securityContext,authBase64String);
    }
    @GET
    @Path("/{userId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get a specific user profile from the systems", notes = "", response = User.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "user", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = User.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "User not found", response = String.class) })
    public Response userUserIdGet(@ApiParam(value = "",required=true) @PathParam("userId") Integer userId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.userUserIdGet(userId,securityContext,authBase64String);
    }
}
