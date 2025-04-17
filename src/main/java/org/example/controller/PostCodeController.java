package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/postcode")
public class PostCodeController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_KEY = "7672094c63c939a056f030a619dcc723";
    private static final String API_URL = "http://v.juhe.cn/postcode/query";

    @GetMapping("/query")
    public Map<String, Object> queryPostCode(@RequestParam String postcode) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        try {
            String url = String.format("%s?key=%s&postcode=%s&dtype=json", API_URL, API_KEY, postcode);
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.get("error_code").equals(0)) {
                meta.put("status", 200);
                meta.put("msg", "查询成功");
                result.put("data", response.get("result"));
            } else {
                meta.put("status", 500);
                meta.put("msg", "查询失败: " + response.get("reason"));
            }
        } catch (Exception e) {
            meta.put("status", 500);
            meta.put("msg", "服务器错误");
            e.printStackTrace();
        }

        result.put("meta", meta);
        return result;
    }
}