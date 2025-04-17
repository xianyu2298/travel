package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/train")
public class TrainController {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(TrainController.class);

    public TrainController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/query")
    public Map<String, Object> queryTrain(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam(required = false) String date) {

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.lolimi.cn/API/hc/api.php")
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival);

            if (date != null && isValidDate(date)) {
                builder.queryParam("time", date);
            }

            // 获取响应
            String response = restTemplate.getForObject(builder.toUriString(), String.class);

            if (response != null && !response.isEmpty()) {
                meta.put("status", 200);
                meta.put("msg", "success");

                // 直接返回获取的原始响应
                Map<String, Object> data = new HashMap<>();
                data.put("response", response);  // 直接将原始的响应数据放入
                result.put("data", data);
            } else {
                meta.put("status", 500);
                meta.put("msg", "查询失败");
            }
        } catch (Exception e) {
            meta.put("status", 500);
            meta.put("msg", "服务器错误");
        }

        result.put("meta", meta);
        return result;  // 返回原始响应
    }

    // 校验日期格式
    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);  // 使用LocalDate解析日期
            return true;
        } catch (DateTimeParseException e) {
            return false;  // 如果无法解析则返回false
        }
    }
}
