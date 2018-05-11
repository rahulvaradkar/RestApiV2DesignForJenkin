package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.ErrorRequestObject;
import io.swagger.model.Whiteboard;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-05T04:57:22.014Z")
public abstract class CollaborationApiService {
    public abstract Response collaborationCollabIdDelete(Integer collabId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response collaborationCollabIdWhiteboardGet(Integer collabId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response collaborationCollabIdWhiteboardPost(Integer collabId,Whiteboard wb,SecurityContext securityContext) throws NotFoundException;
    public abstract Response collaborationCollabIdWhiteboardWhiteboardIdDelete(Integer collabId,Integer whiteboardId,SecurityContext securityContext) throws NotFoundException;
}
