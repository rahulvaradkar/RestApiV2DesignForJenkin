package io.swagger.api.factories;

import io.swagger.api.NeighborhoodApiService;
import io.swagger.api.impl.NeighborhoodApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-05T04:57:22.014Z")
public class NeighborhoodApiServiceFactory {
    private final static NeighborhoodApiService service = new NeighborhoodApiServiceImpl();

    public static NeighborhoodApiService getNeighborhoodApi() {
        return service;
    }
}
