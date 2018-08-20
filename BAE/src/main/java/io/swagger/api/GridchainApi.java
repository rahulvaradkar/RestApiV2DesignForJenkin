package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.GridchainApiService;
import io.swagger.api.factories.GridchainApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.GridChain;
import io.swagger.model.ResponseInfo;

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

@Path("/gridchain")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the gridchain API")

public class GridchainApi  {
   private final GridchainApiService delegate;

   public GridchainApi(@Context ServletConfig servletContext) {
      GridchainApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("GridchainApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (GridchainApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = GridchainApiServiceFactory.getGridchainApi();
      }

      this.delegate = delegate;
   }

    @GET
    @Path("/{gridId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get the complete details of all Grid Components.", notes = "User can GET all Grid Component, it's status, History, Transactions done on entire grid. Information of the Grid present in the Whiteboard of the Collaboration using his/her memberships details for that Neighborhood. So {email} and user in Authrization must match. Also {nhPath} and nhPath in Authorization should match. The Grid information will have Column Names, sequence Number, Active/inactive & Access Control ( R/W ), Column Count, Row Count, Access Control for Add Row, Delete Row, Insert Column, Delete Column, Edit Data, Cuboid Properties. If user is the Owner then Accesss Control Cuboid information.", response = GridChain.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "Get Grid Chain Information.", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = GridChain.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ResponseInfo.class),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "Email not found. Authorization Failed. Neighborhood Path doesnot exists. Unauthorized Access.", response = ResponseInfo.class) })
    public Response gridchainGridIdGet(@ApiParam(value = "",required=true) @PathParam("gridId") Integer gridId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String )
    throws NotFoundException {
        return delegate.gridchainGridIdGet(gridId,securityContext,authBase64String);
    }
}
