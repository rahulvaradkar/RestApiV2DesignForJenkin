package io.swagger.api.factories;

import io.swagger.api.BcpInstanceApiService;
import io.swagger.api.impl.BcpInstanceApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-04-23T07:19:11.481Z")
public class BcpInstanceApiServiceFactory {
    private final static BcpInstanceApiService service = new BcpInstanceApiServiceImpl();

    public static BcpInstanceApiService getBcpInstanceApi() {
        return service;
    }
}
