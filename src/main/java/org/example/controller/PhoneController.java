package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_KEY = "ed0cd46ea8864a8351ba1be384640042"; // API Key
    private static final String API_URL = "http://apis.juhe.cn/mobile/get";

    @GetMapping("/query")
    public Map<String, Object> queryPhone(@RequestParam String phone) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        try {
            String url = String.format("%s?key=%s&phone=%s&dtype=json", API_URL, API_KEY, phone);
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