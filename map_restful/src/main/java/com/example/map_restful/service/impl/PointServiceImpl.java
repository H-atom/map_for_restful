package com.example.map_restful.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.map_restful.entity.Point;
import com.example.map_restful.mapper.PointMapper;
import com.example.map_restful.service.PointService;
import org.springframework.stereotype.Service;

@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements PointService {
}
