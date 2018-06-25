package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.GridApiService;
import io.swagger.api.factories.GridApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CellBuffer;
import java.util.Date;
import io.swagger.model.ErrorAddRows;
import io.swagger.model.ErrorDeleteObject;
import io.swagger.model.ErrorDeleteRows;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.ErrorUpdateObject;
import io.swagger.model.Grid;
import io.swagger.model.Transaction;

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


import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/grid")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the grid API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-30T16:47:27.652Z")
public class GridApi  {
   private final GridApiService delegate;

   public GridApi(@Context ServletConfig servletContext) {
      GridApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("GridApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (GridApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = GridApiServiceFactory.getGridApi();
      }

      this.delegate = delegate;
   }

    @DELETE
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete cuboid by ID", notes = "", response = Void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "grid", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 104, message = "No Permissions to delete this object", response = ErrorDeleteObject.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Cuboid not found", response = Void.class) })
    public Response gridDelete(@ApiParam(value = "",required=true) @PathParam("gridId") Integer gridId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.gridDelete(gridId,securityContext,authBase64String);
    }
    @GET
    @Path("/{gridId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Download grid data for a given cell specification", notes = "", response = CellBuffer.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "grid", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 103, message = "No Permissions to create this object", response = ErrorRequestObject.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = CellBuffer.class) })
    public Response gridGridIdGet(@ApiParam(value = "",required=true) @PathParam("gridId") Integer gridId
,@ApiParam(value = "",required=true) @QueryParam("importTid") Integer importTid
,@ApiParam(value = "",required=true) @QueryParam("view") String view
,@ApiParam(value = "",required=true) @QueryParam("mode") Integer mode
,@ApiParam(value = "",required=true) @QueryParam("baselineId") Integer baselineId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String )
    throws NotFoundException {
        return delegate.gridGridIdGet(gridId,importTid,view,mode,baselineId,securityContext, authBase64String);
    }
    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create a new cuboid. returns a new grid id", notes = "", response = Grid.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "grid", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation, returns grid with an ID", response = Grid.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input", response = ErrorRequestObject.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 422, message = "101 No Permissions to create this object", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response gridPost(@ApiParam(value = "Cuboid creation details" ,required=true) Grid grid
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.gridPost(grid,securityContext, authBase64String);
    }
    @PUT
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update a grid", notes = "", response = CellBuffer.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "grid", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 104, message = "No Permissions to create this object", response = ErrorUpdateObject.class),
        
        @io.swagger.annotations.ApiResponse(code = 111, message = "No Permissions to create this object", response = ErrorAddRows.class),
        
        @io.swagger.annotations.ApiResponse(code = 112, message = "No Permissions to create this object", response = ErrorDeleteRows.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = CellBuffer.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input (Bad Request)", response = ErrorRequestObject.class, responseContainer = "List") })
    public Response gridPut(@ApiParam(value = "",required=true) @QueryParam("gridId") Integer gridId
,@ApiParam(value = "Cell buffer details" ,required=true) CellBuffer cellBufferRequest
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.gridPut(gridId,cellBufferRequest,securityContext, authBase64String);
    }
    @GET
    @Path("/{tableId}/{transactionId}/changes")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get changes for given transaction Id", notes = "", response = CellBuffer.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "grid", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = CellBuffer.class) })
    public Response gridTableIdTransactionIdChangesGet(@ApiParam(value = "",required=true) @PathParam("tableId") Integer tableId
,@ApiParam(value = "",required=true) @PathParam("transactionId") Integer transactionId
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.gridTableIdTransactionIdChangesGet(tableId,transactionId,securityContext, authBase64String);
    }
    @GET
    @Path("/{tableId}/transactionsBetweenTids")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get cuboid transactions between two transactions", notes = "", response = Transaction.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "grid", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Transaction.class, responseContainer = "List") })
    public Response gridTableIdTransactionsBetweenTidsGet(@ApiParam(value = "",required=true) @PathParam("tableId") Long tableId
,@ApiParam(value = "",required=true) @QueryParam("startTid") Long startTid
,@ApiParam(value = "",required=true) @QueryParam("endTid") Long endTid
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.gridTableIdTransactionsBetweenTidsGet(tableId,startTid,endTid,securityContext, authBase64String);
    }
    @GET
    @Path("/{tableId}/transactions")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get cuboid transactions for time interval for a given specification", notes = "", response = Transaction.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "bwAuth")
    }, tags={ "grid", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Transaction.class, responseContainer = "List") })
    public Response gridTableIdTransactionsGet(@ApiParam(value = "",required=true) @PathParam("tableId") Integer tableId
,@ApiParam(value = "") @QueryParam("startTid") Long startTid
,@ApiParam(value = "") @QueryParam("endTid") Long endTid
,@ApiParam(value = "") @QueryParam("startTime") Date startTime
,@ApiParam(value = "") @QueryParam("endTime") Date endTime
,@ApiParam(value = "limits transactions to a cell, row, column or a specification" ) CellBuffer cellBufferRequest
,@Context SecurityContext securityContext, @HeaderParam("Authorization") String authBase64String)
    throws NotFoundException {
        return delegate.gridTableIdTransactionsGet(tableId,startTid,endTid,startTime,endTime,cellBufferRequest,securityContext, authBase64String);
    }
}
