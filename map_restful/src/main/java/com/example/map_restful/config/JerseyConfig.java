package com.example.map_restful.config;

import com.example.map_restful.controller.BaiduResource;
import com.example.map_restful.controller.PointResource;
import com.example.map_restful.controller.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(UserResource.class);
        register(BaiduResource.class);
        register(PointResource.class);
        register(MyFilter.class);
    }
}
