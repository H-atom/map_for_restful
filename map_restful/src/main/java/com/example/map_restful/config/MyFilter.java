package com.example.map_restful.config;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.*;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

public class MyFilter implements ContainerResponseFilter {

    public ContainerResponse filter(ContainerRequest creq, ContainerResponse cres) {
        cres.getHeaders().add("Access-Control-Allow-Origin", "*");
        cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, HEAD");

        cres.getHeaders().add("Access-Control-Max-Age", "1209600");
        //可以通过 throw new WebApplicationException(Status.UNAUTHORIZED); 来中断请求
        return cres;
    }

    public void filter(ContainerRequestContext request, ContainerResponseContext cres) throws IOException {
        cres.getHeaders().add("Access-Control-Allow-Origin", "*");
        cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, HEAD");
        cres.getHeaders().add("Access-Control-Max-Age", "1209600");
    }

}