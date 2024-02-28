package com.example.map_restful;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.map_restful.entity.Point;
import com.example.map_restful.service.PointService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MapRestfulApplicationTests {

    @Autowired
    PointService pointService;

    @Test
    void contextLoads() {
    }

}
