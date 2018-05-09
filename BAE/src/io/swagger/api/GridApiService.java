package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.CellBuffer;
import java.util.Date;
import io.swagger.model.ErrorAddRows;
import io.swagger.model.ErrorDeleteObject;
import io.swagger.model.ErrorDeleteRows;
import io.swagger.model.ErrorReadObject;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.ErrorUpdateObject;
import io.swagger.model.Grid;
import io.swagger.model.Transaction;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-07T10:23:52.356Z")
public abstract class GridApiService {
    public abstract Response gridDelete(Integer tableId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response gridGet(String tableId,CellBuffer cellBufferRequest,SecurityContext securityContext) throws NotFoundException;
    public abstract Response gridPost(Grid grid,SecurityContext securityContext) throws NotFoundException;
    public abstract Response gridPut(Integer gridId,CellBuffer cellBufferRequest,SecurityContext securityContext) throws NotFoundException;
    public abstract Response gridTableIdTransactionIdChangesGet(Integer tableId,Integer transactionId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response gridTableIdTransactionsBetweenTidsGet(Long tableId, @NotNull Long startTid, @NotNull Long endTid,SecurityContext securityContext) throws NotFoundException;
    public abstract Response gridTableIdTransactionsGet(Integer tableId, Long startTid, Long endTid, Date startTime, Date endTime,CellBuffer cellBufferRequest,SecurityContext securityContext) throws NotFoundException;
}
