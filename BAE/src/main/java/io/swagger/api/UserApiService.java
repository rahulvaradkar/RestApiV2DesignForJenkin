package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.Collaboration;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.GridChain;
import io.swagger.model.GridInfo;
import io.swagger.model.Membership;
import io.swagger.model.ResponseInfo;
import io.swagger.model.User;
import io.swagger.model.Whiteboard;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-09T04:12:45.675Z")
public abstract class UserApiService {
    public abstract Response userEmailMembershipsGet(String email,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardsGet(String email,String nhPath,Integer collabId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridsGet(String email,String nhPath,Integer collabId,Integer whiteboardId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridGridIdGet(String email,String nhPath,Integer collabId,Integer whiteboardId,Integer gridId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userEmailNeighborhoodNhPathCollaborationCollabIdWhiteboardWhiteboardIdGridchainGridIdGet(String email,String nhPath,Integer collabId,Integer whiteboardId,Integer gridId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userEmailNeighborhoodNhPathCollaborationsGet(String email,String nhPath,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userGet( Boolean active,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userPost(User user,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userPut(User user,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userUserIdDelete(Integer userId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response userUserIdGet(Integer userId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
}
