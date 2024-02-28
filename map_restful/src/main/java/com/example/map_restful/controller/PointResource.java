package com.example.map_restful.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.map_restful.entity.Point;
import com.example.map_restful.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@Path("/point")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class PointResource {
    @Resource
    PointService pointService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPoint(Point point) {
        pointService.save(point);
        return Response.ok().entity(point).build();
    }

    @DELETE
    @Path("/{owner}/{id}")
    public Response deletePoint(@PathParam("id") int id,@PathParam("owner") String owner) {
        Point point = pointService.getById(id);
        if (point == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("自定义点不存在").build();
        }
        if (Objects.equals(owner, point.getOwner())) {
            pointService.removeById(id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("无权访问其他用户内容").build();
    }

    @PATCH
    @Path("/{owner}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response change(Point point,@PathParam("owner")String owner) {
        Point check = pointService.getById(point.getId());
        if (check == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("自定义点不存在").build();
        }
        if (point.getOpen() || Objects.equals(owner, point.getOwner())) {
            pointService.updateById(point);
            point = pointService.getById(point.getId());
            return Response.ok(point).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("无权访问其他用户内容").build();
    }


    @GET
    @Path("/{owner}")
    public Response search(@DefaultValue("")@QueryParam("query")String query, @PathParam("owner")String owner){
        QueryWrapper<Point> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("open", false);
        queryWrapper.and(w->w.like("detail", query).or().like("name",query).or().like("address",query));
        queryWrapper.eq("owner",owner);
        List<Point> list = pointService.list(queryWrapper);
        QueryWrapper<Point> q = new QueryWrapper<>();
        queryWrapper.and(w->w.like("detail", query).or().like("name",query).or().like("address",query));
        q.eq("open", true);
        List<Point> l = pointService.list(q);
        list.addAll(l);
        return Response.ok(list).build();
    }
}
