package com.example.map_restful.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.map_restful.entity.User;
import com.example.map_restful.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/user")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @Autowired
    UserService userService;

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") int id) {
        User user = userService.getById(id);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("账号不存在").build();
        }
        return Response.ok(user).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user, @Context UriInfo uriInfo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getPhone());
        User check = userService.getOne(queryWrapper);
        if (check != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("账号已存在").build();
        }
        userService.save(user);
        URI uri = uriInfo.getAbsolutePathBuilder().path("" + user.getId()).build();
        return Response.created(uri).entity(user).build();
    }

    @DELETE
    public Response deleteUser(@CookieParam("id") int id, @CookieParam("password") String password) {
        User user = userService.getById(id);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("账号不存在").build();
        }
        if (!user.getPassword().equals(password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("请先登录").build();
        }

        userService.removeById(id);
        return Response.noContent().build();
    }

    @PATCH
    public Response change(@BeanParam User user, @CookieParam("password") String password) {
        User user1 = userService.getById(user.getId());
        if (user1 == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("账号不存在").build();
        }
        if (!user.getPassword().equals(password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("请先登录").build();
        }
        userService.updateById(user);
        user = userService.getById(user.getId());
        return Response.ok(user).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getPhone());
        User one = userService.getOne(queryWrapper);
        if (one == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("账号不存在").build();
        }
        if (one.getPassword().equals(user.getPassword())) {
            return Response.ok(one).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("密码错误").build();
    }
}
