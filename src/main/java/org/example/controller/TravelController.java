package org.example.controller;

import org.example.entity.Travel;
import org.example.mapper.TravelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/travel")
public class TravelController {

    @Autowired
    private TravelMapper travelMapper;

    @GetMapping("/list")
    public Map<String, Object> getTravelPlans(@RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        try {
            List<Travel> plans = travelMapper.getTravelPlansByUserId(userId);
            System.out.println("查询结果示例：" + (plans.isEmpty() ? "空列表" : plans.get(0).toString()));
            meta.put("status", 200);
            meta.put("msg", "success");
            result.put("data", plans);
            result.put("meta", meta);
        } catch (Exception e) {
            e.printStackTrace();
            meta.put("status", 500);
            meta.put("msg", "数据库错误");
            result.put("meta", meta);
        }

        return result;
    }


    @PostMapping("/add")
    public Map<String, Object> addTravelPlan(@RequestBody Travel travel) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        try {
            int rows = travelMapper.addTravelPlan(travel);
            if (rows > 0) {
                meta.put("status", 200);
                meta.put("msg", "添加成功");
            } else {
                meta.put("status", 400);
                meta.put("msg", "添加失败");
            }
            result.put("meta", meta);
        } catch (Exception e) {
            meta.put("status", 500);
            meta.put("msg", "数据库错误");
            result.put("meta", meta);
        }

        return result;
    }
}
