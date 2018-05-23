package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.BcpInstanceInfo;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-04-23T07:19:11.481Z")
public abstract class BcpInstanceApiService {
    public abstract Response searchBCPInstance( @NotNull String serverName, @NotNull String serverUrl, @NotNull String loginName, @NotNull String loginPwd,SecurityContext securityContext) throws NotFoundException;
}
