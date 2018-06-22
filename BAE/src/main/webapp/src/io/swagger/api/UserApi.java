package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.UserApiService;
import io.swagger.api.factories.UserApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.ErrorRequestObject;
import io.swagger.model.User;

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
