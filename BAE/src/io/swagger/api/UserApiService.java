package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.ErrorRequestObject;
import io.swagger.model.User;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-04-30T03:35:16.055Z")
public abstract class UserApiService {
    public abstract Response userGet( Boolean active,SecurityContext securityContext) throws NotFoundException;
    public abstract Response userPost(User user,SecurityContext securityContext) throws NotFoundException;
    public abstract Response userPut(User user,SecurityContext securityContext) throws NotFoundException;
    public abstract Response userUserIdDelete(Integer userId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response userUserIdGet(Integer userId,SecurityContext securityContext) throws NotFoundException;
}
