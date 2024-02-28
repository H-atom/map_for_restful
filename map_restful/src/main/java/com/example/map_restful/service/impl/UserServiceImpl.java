package com.example.map_restful.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.map_restful.entity.User;
import com.example.map_restful.mapper.UserMapper;
import com.example.map_restful.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
