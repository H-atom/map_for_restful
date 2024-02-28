package com.example.map_restful.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.map_restful.entity.Point;
import com.example.map_restful.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Service
public class BaiduService {

    private final String place = "https://api.map.baidu.com/place/v2/";
    private final String JSON = "&output=json";
    @Autowired
    PointService pointService;

    public String surrounding(String lat, String lng) throws Exception {
        String[] in = {"美食", "银行", "购物", "旅游景点", "休闲娱乐"};
        String[] out = {"restaurant", "bank", "shopping", "screen", "entertainment"};
        JSONObject list = new JSONObject();
        for (int i = 0; i < in.length; i++) {
            String res = request(place + "search?query=" + in[i] + "&radius=1500&radius_limit=true&location="
                    + lat + "," + lng + JSON);
            JSONArray b = brief(res);
            if (b == null)
                return null;
            list.put(out[i], b);
        }
        return list.toString();
    }

    public String suggest(String query, String region, String lat, String lng) throws Exception {
        String res = request(place + "suggestion?city_limit=false" +
                "&query=" + query + "&region=" + region + "&location=" + lat + "," + lng + JSON
        );
        JSONArray b = brief(res);
        if (b==null)
            return null;
        return b.toString();
    }

    public String detail(String uid) throws Exception {
        String res = request(place + "detail?scope=2&uid=" + uid + JSON);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getIntValue("status") != 0) {
            return null;
        }
        JSONObject result = json.getJSONObject("result");
        JSONObject brief = new JSONObject();
        String[] tag =
                {"uid", "name", "address", "province", "city", "area", "adcode", "telephone", "street_id", "detail"};
        for (String s : tag) {
            brief.put(s, result.getString(s));
        }
        JSONObject location = result.getJSONObject("location");
        brief.put("lat", location.getString("lat"));
        brief.put("lng", location.getString("lng"));
        if ("1".equals(result.getString("detail"))) {
            JSONObject info = result.getJSONObject("detail_info");
            brief.put("tag", info.getString("tag"));
            brief.put("rating", info.getString("overall_rating"));
//            JSONObject navi = info.getJSONObject("navi_location");
//            brief.put("navi_lat", navi.getString("lat"));
//            brief.put("navi_lng", navi.getString("lng"));

        }
        return brief.toString();
    }

    public String weather(String lat,String lng) throws Exception {
        String id = adcode(lat,lng);
        String res = request("https://api.map.baidu.com/weather/v1/?data_type=fc&district_id=" + id);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getIntValue("status") != 0) {
            return null;
        }
        JSONObject result = json.getJSONObject("result");
        return result.toString();
    }

    private String adcode(String lat,String lng) throws Exception {
        return JSONObject.parseObject(
                request("https://api.map.baidu.com/reverse_geocoding/v3/?&location=" +"&location="+lat+","+lng + JSON)
        ).getJSONObject("result").getJSONObject("addressComponent").getString("adcode");
    }

    public String drive(String o, String d) throws Exception {
        JSONObject r = new JSONObject();
        for (int i = 0; i < 4; i++) {
            JSONObject res = navigation(o, d, "driving?tactics=" + i + "&steps_info=1&");
            if (res.getString("wrong") != null){
                JSONObject rr = new JSONObject();
                rr.put("message","没有线路");
                return rr.toString();
            }
            r.put(i + "", res);
        }
        return r.toString();
    }

    public String walk(String o, String d) throws Exception {
        JSONObject res = navigation(o, d, "walking?");
        if (res.getString("wrong") != null){
            JSONObject r = new JSONObject();
            r.put("message","没有线路");
            return r.toString();
        }
        return res.toString();
    }

    public String transit(String o, String d) throws Exception {
        JSONObject res = navigation(o, d, "transit?");
        if (res.getString("wrong") != null){
            JSONObject r = new JSONObject();
            r.put("message","没有线路");
            return r.toString();
        }
        return res.toString();
    }

    private JSONObject navigation(String origin, String destination, String mode) throws Exception {
        String method = "https://api.map.baidu.com/directionlite/v1/" + mode;
        method += "origin=" + origin;
        method += "&destination=" + destination;
        String res = request(method);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getIntValue("status") != 0) {
            JSONObject b = new JSONObject();
            b.put("wrong", "没有线路");
            return b;
        }
        return json.getJSONObject("result").getJSONArray("routes").getJSONObject(0);
    }

    private String location(String uid) throws Exception {
        String res = request(place + "detail?scope=2&uid=" + uid + JSON);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getIntValue("status") != 0) {
            return json.toJSONString();
        }
        JSONObject location = json.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        return lat + "," + lng;
    }

    private String request(String method) throws Exception {
        String AK = "&ak=YGfTWtnNHrsjz97inW0fkBk0xzQGMLOj";
        URL url = new URL(method + AK);
        URLConnection httpConnection = url.openConnection();
        httpConnection.connect();
        InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        isr.close();
        return builder.toString();
    }

    private JSONArray brief(String res) {
        JSONObject json = JSONObject.parseObject(res);
        if (json.getIntValue("status") != 0) {
            return null;
        }
        JSONArray result = json.getJSONArray("results");
        if (result == null) {
            result = json.getJSONArray("result");
        }
        JSONArray brief = new JSONArray();
        for (int i = 0; i < result.size(); i++) {
            JSONObject point = result.getJSONObject(i);
            JSONObject b = new JSONObject();
            JSONObject location = point.getJSONObject("location");
            b.put("address", point.getString("address"));
            b.put("name", point.getString("name"));
            b.put("uid", point.getString("uid"));
            b.put("lat", location.getString("lat"));
            b.put("lng", location.getString("lng"));
            brief.add(b);
        }
        return brief;
    }
}
