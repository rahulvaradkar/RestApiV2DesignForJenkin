package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.Collaboration;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.Member;
import io.swagger.model.Neighborhood;
import io.swagger.model.Relation;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-09T04:12:45.675Z")
public abstract class NeighborhoodApiService {
    public abstract Response neighborhoodGet( String neighborhoodSpec,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdCollaborationGet(Integer nhId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdDelete(Integer nhId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdGet(Integer nhId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdMemberGet(Integer nhId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdMemberMemberIdCollaborationPost(Integer nhId,Integer memberId,Collaboration collaboration,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdMemberMemberIdDelete(Integer nhId,Integer memberId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdMemberPost(Integer nhId,Member member,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdRelationDelete(Integer nhId, @NotNull String relation, SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdRelationGet(Integer nhId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodNhIdRelationPost(Integer nhId,Relation relationship,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response neighborhoodPost(Neighborhood neighborhood,SecurityContext securityContext, String authBase64String) throws NotFoundException;
}
