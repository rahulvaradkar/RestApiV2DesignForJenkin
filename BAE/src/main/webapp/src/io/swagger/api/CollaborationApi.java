package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.CollaborationApiService;
import io.swagger.api.factories.CollaborationApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.ErrorRequestObject;
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

@Path("/collaboration")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the collaboration API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-10T09:36:40.808Z")
public class CollaborationApi  {
   private final CollaborationApiService delegate;

   public CollaborationApi(@Context ServletConfig servletContext) {
      CollaborationApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("CollaborationApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (CollaborationApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = CollaborationApiServiceFactory.getCollaborationApi();
      }

      this.delegate = delegate;
   }

    @DELETE
    @Path("/{collabId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete collaboration by ID", notes = "", response = Void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "collaboration", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "Collaboration not found", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response collaborationCollabIdDelete(@ApiParam(value = "",required=true) @PathParam("collabId") Integer collabId
,@Context SecurityContext securityContext,  @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.collaborationCollabIdDelete(collabId,securityContext,authBase64String);
    }
    @GET
    @Path("/{collabId}/whiteboard")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get the whiteboards for a collaboration", notes = "", response = Whiteboard.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "collaboration", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Whiteboard.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "Whiteboard not found", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response collaborationCollabIdWhiteboardGet(@ApiParam(value = "",required=true) @PathParam("collabId") Integer collabId
,@Context SecurityContext securityContext,  @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.collaborationCollabIdWhiteboardGet(collabId,securityContext,authBase64String);
    }
    @POST
    @Path("/{collabId}/whiteboard")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create new Whiteboard", notes = "", response = Integer.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "collaboration", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Integer.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "Whiteboard not found", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response collaborationCollabIdWhiteboardPost(@ApiParam(value = "",required=true) @PathParam("collabId") Integer collabId
,@ApiParam(value = "Whiteboard creation details" ,required=true) Whiteboard wb
,@Context SecurityContext securityContext,  @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.collaborationCollabIdWhiteboardPost(collabId,wb,securityContext,authBase64String);
    }
    @DELETE
    @Path("/{collabId}/whiteboard/{whiteboardId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete whiteboard by ID", notes = "", response = Void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "collaboration", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "Whiteboard not found", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response collaborationCollabIdWhiteboardWhiteboardIdDelete(@ApiParam(value = "",required=true) @PathParam("collabId") Integer collabId
,@ApiParam(value = "",required=true) @PathParam("whiteboardId") Integer whiteboardId
,@Context SecurityContext securityContext,  @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.collaborationCollabIdWhiteboardWhiteboardIdDelete(collabId,whiteboardId,securityContext,authBase64String);
    }
}
