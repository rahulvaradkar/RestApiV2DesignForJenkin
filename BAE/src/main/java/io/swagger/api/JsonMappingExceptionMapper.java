package io.swagger.api;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;

@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException >{

    @Override
    public Response toResponse(JsonMappingException exception)
    {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("This is an invalid request. At least one field format is not readable by the system.")
                .type( MediaType.APPLICATION_JSON)
                .build();
    }
    
}