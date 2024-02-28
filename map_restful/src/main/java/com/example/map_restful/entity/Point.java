package com.example.map_restful.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("point")
public class Point implements Serializable {

    @TableId(type = IdType.AUTO)
    @FormParam("id")
    private Integer id;
    @FormParam("name")
    private String name;
    @FormParam("lat")
    private String lat;
    @FormParam("lng")
    private String lng;
    @FormParam("open")
    @DefaultValue("0")
    private Boolean open;
    @FormParam("owner")
    private String owner;
    @FormParam("detail")
    private String detail;
    @FormParam("address")
    private String address;
    @FormParam("timestamp")
    private Timestamp timestamp;
    @TableLogic
    private Boolean deleted = false;
}
