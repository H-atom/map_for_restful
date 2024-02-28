package com.example.map_restful.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import java.io.Serializable;

@Data
@TableName("user")
public class User implements Serializable {

    @TableLogic
    @TableId(type = IdType.AUTO)
    @FormParam("id")
    private Integer id;
    @FormParam("phone")
    private String phone;
    @FormParam("username")
    private String username;
    @FormParam("password")
    private String password;
    @TableLogic
    @DefaultValue("0")
    private Boolean deleted;
}
