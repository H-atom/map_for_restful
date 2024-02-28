package com.example.map_restful.controller;

import com.example.map_restful.service.impl.BaiduService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/baidu")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class BaiduResource {
    @Autowired
    BaiduService baidu;

    @GET
    @Path("/{uid}")
    public Response get(@PathParam("uid") String uid) throws Exception {
        return check(baidu.detail(uid));
    }

    @GET
    @Path("/suggest")
    public Response suggest(@QueryParam("query") String query,
                            @QueryParam("region") String region,
                            @QueryParam("lat") String lat,
                            @QueryParam("lng") String lng
    ) throws Exception {
        return check(baidu.suggest(query, region, lat, lng));
    }

    @GET
    @Path("/surrounding")
    public Response surrounding(@QueryParam("lat") String lat, @QueryParam("lng") String lng) throws Exception {
        return check(baidu.surrounding(lat, lng));
    }

    @GET
    @Path("/weather/{lat}/{lng}")
    public Response weather(@PathParam("lat") String lat,@PathParam("lng") String lng) throws Exception {
        return check(baidu.weather(lat,lng));
    }

    @GET
    @Path("/walk/{o}/{d}")
    public Response walk(@PathParam("o") String o, @PathParam("d") String d) throws Exception {
        return check(baidu.walk(o, d));
    }

    @GET
    @Path("/drive/{o}/{d}")
    public Response drive(@PathParam("o") String o, @PathParam("d") String d) throws Exception {
        return check(baidu.drive(o, d));
    }

    @GET
    @Path("/transit/{o}/{d}")
    public Response transit(@PathParam("o") String o, @PathParam("d") String d) throws Exception {
        return check(baidu.transit(o, d));
    }

    private Response check(String res) {
        if (res == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("百度地图使用失败").build();
        return Response.ok(res).build();
    }

}
