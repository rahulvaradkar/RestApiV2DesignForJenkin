package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.BcpInstanceApiService;
import io.swagger.api.factories.BcpInstanceApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.BcpInstanceInfo;

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

@Path("/bcpInstance")


@io.swagger.annotations.Api(description = "the bcpInstance API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-04-23T07:19:11.481Z")
public class BcpInstanceApi  {
   private final BcpInstanceApiService delegate;

   public BcpInstanceApi(@Context ServletConfig servletContext) {
      BcpInstanceApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("BcpInstanceApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (BcpInstanceApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = BcpInstanceApiServiceFactory.getBcpInstanceApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "returns BCP Instance information", notes = "By passing BCP Url, you can get BCP Instance information in the system ", response = BcpInstanceInfo.class, tags={ "developers","api consumers", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "list of users and Root level Neighborhooods", response = BcpInstanceInfo.class) })
    public Response searchBCPInstance(@ApiParam(value = "Alias used for the BCP Instance Server connecting to",required=true) @QueryParam("serverName") String serverName
,@ApiParam(value = "pass the URL of BCP Instance Server",required=true) @QueryParam("serverUrl") String serverUrl
,@ApiParam(value = "pass the login to connect to BPC Instance Server",required=true) @QueryParam("login_name") String loginName
,@ApiParam(value = "pass the login password to connect to BPC Instance Server",required=true) @QueryParam("login_pwd") String loginPwd
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.searchBCPInstance(serverName,serverUrl,loginName,loginPwd,securityContext);
    }
}
