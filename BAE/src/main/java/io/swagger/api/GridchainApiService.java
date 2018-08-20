package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.GridChain;
import io.swagger.model.ResponseInfo;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;

public abstract class GridchainApiService {
    public abstract Response gridchainGridIdGet(Integer gridId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
}
