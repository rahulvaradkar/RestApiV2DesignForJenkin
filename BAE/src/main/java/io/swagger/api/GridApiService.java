package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.math.BigDecimal;
import io.swagger.model.CellBuffer;
import java.util.Date;
import io.swagger.model.ErrorAddRows;
import io.swagger.model.ErrorDeleteObject;
import io.swagger.model.ErrorDeleteRows;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.ErrorUpdateObject;
import io.swagger.model.Grid;
import io.swagger.model.GridTransaction;
import io.swagger.model.ResponseInfo;
import io.swagger.model.Transaction;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-30T16:47:27.652Z")
public abstract class GridApiService {
    public abstract Response gridDelete(Integer gridId,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response gridGridIdGet(Integer gridId, @NotNull Integer importTid, @NotNull String view, @NotNull Integer mode, @NotNull Integer baselineId, SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response gridPost(Grid grid,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response gridPut( @NotNull Integer gridId,CellBuffer cellBufferRequest,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response gridGridIdTxIdTransactionIdChangesGet(Integer gridId,Integer transactionId, @NotNull BigDecimal localTimeAfter111970, @NotNull String viewPref, SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response gridGridIdTransactionsBetweenTidsGet(Long gridId, @NotNull String reportType, @NotNull Long startTid, @NotNull Long endTid, @NotNull String viewPref,SecurityContext securityContext, String authBase64String) throws NotFoundException;
    public abstract Response gridGridIdTransactionsGet(Integer gridId, @NotNull BigDecimal localTimeAfter111970, @NotNull String viewPref, @NotNull String reportType, String activityPeriod, BigDecimal startDate, BigDecimal endDate, BigDecimal importTid,SecurityContext securityContext, String authBase64String) throws NotFoundException;
}
