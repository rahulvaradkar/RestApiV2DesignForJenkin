package io.swagger.api.factories;

import io.swagger.api.NeighborhoodApiService;
import io.swagger.api.impl.NeighborhoodApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-10T09:36:40.808Z")
public class NeighborhoodApiServiceFactory {
    private final static NeighborhoodApiService service = new NeighborhoodApiServiceImpl();

    public static NeighborhoodApiService getNeighborhoodApi() {
        return service;
    }
}
