package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.BcpInstanceInfo;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import boardwalk.rest.RestTasks;


import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-04-23T07:19:11.481Z")
public class BcpInstanceApiServiceImpl extends BcpInstanceApiService {
    @Override
    public Response searchBCPInstance( @NotNull String serverName,  @NotNull String serverUrl,  @NotNull String loginName,  @NotNull String loginPwd, SecurityContext securityContext) throws NotFoundException {
    	System.out.println("inside BcpInstanceApiServiceImpl....");
    	BcpInstanceInfo bcpii = new BcpInstanceInfo();
    	bcpii = RestTasks.GetBcpInstance(serverName, serverUrl, loginName, loginPwd );
    	System.out.println("inside BcpInstanceApiServiceImpl... bcpii.toString() ->" + bcpii.toString());
    	return Response.ok().entity(bcpii).build();  
    }
}
