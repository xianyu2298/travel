package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.id}")
    private String weatherApiId;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @GetMapping("/now")
    public Map<String, Object> getWeather(@RequestParam String province,
                                          @RequestParam String city) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        try {
            // 构建请求URL
            String url = UriComponentsBuilder.fromHttpUrl(weatherApiUrl)
                    .queryParam("id", weatherApiId)
                    .queryParam("key", weatherApiKey)
                    .queryParam("sheng", URLEncoder.encode(province, StandardCharsets.UTF_8.toString()))
                    .queryParam("place", URLEncoder.encode(city, StandardCharsets.UTF_8.toString()))
                    .build()
                    .toUriString();

            // 发送请求
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // 检查HTTP状态码
            if (response.getStatusCode() != HttpStatus.OK) {
                // 如果状态码不是200，返回错误信息
                meta.put("status", 500);
                meta.put("msg", "请求失败，HTTP状态码: " + response.getStatusCodeValue());
                result.put("meta", meta);
                return result;
            }

            // 检查返回的内容类型
            String contentType = response.getHeaders().getContentType().toString();
            if (contentType.contains("text/html")) {
                // 提取HTML中嵌套的JSON字符串
                String htmlResponse = response.getBody();
                String jsonString = extractJsonFromHtml(htmlResponse);

                // 解析JSON字符串
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> body = mapper.readValue(jsonString, Map.class);

                // 处理响应
                if ("200".equals(String.valueOf(body.get("code")))) {
                    meta.put("status", 200);
                    meta.put("msg", "success");
                    result.put("data", body);
                } else {
                    meta.put("status", 500);
                    meta.put("msg", "天气API错误: " + body.get("msg"));
                }
            } else {
                // 直接处理返回的JSON响应
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> body = mapper.readValue(response.getBody(), Map.class);
                if ("200".equals(String.valueOf(body.get("code")))) {
                    meta.put("status", 200);
                    meta.put("msg", "success");
                    result.put("data", body);
                } else {
                    meta.put("status", 500);
                    meta.put("msg", "天气API错误: " + body.get("msg"));
                }
            }
        } catch (Exception e) {
            meta.put("status", 500);
            meta.put("msg", "天气服务异常: " + e.getMessage());
            e.printStackTrace();
        }

        result.put("meta", meta);
        return result;
    }

    // 提取HTML中的JSON字符串
    private String extractJsonFromHtml(String htmlResponse) {
        // 正则表达式提取JSON部分
        String regex = "\\{.*\\}";  // 匹配以{开始，}结束的JSON字符串
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlResponse);
        if (matcher.find()) {
            return matcher.group();
        }
        return "{}";  // 默认返回一个空的JSON对象
    }

}