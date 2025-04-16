package org.example.controller;

import org.example.entity.Expense;
import org.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/add")
    public Map<String, Object> addExpense(@RequestBody Expense expense) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        // 添加调试日志
        System.out.println("Received expense data: " + expense);

        try {
            if (expense.getUserId() == null) {
                meta.put("status", 400);
                meta.put("msg", "用户ID不能为空");
                result.put("meta", meta);
                return result;
            }

            boolean success = expenseService.addExpense(expense);
            meta.put("status", success ? 200 : 400);
            meta.put("msg", success ? "添加成功" : "添加失败");
        } catch (Exception e) {
            meta.put("status", 500);
            meta.put("msg", "数据库错误");
            e.printStackTrace();
        }

        result.put("meta", meta);
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> getExpenses(@RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        try {
            List<Expense> expenses = expenseService.getExpensesByUserId(userId);
            meta.put("status", 200);
            meta.put("msg", "success");
            result.put("data", expenses);
        } catch (Exception e) {
            meta.put("status", 500);
            meta.put("msg", "数据库错误");
            e.printStackTrace();
        }

        result.put("meta", meta);
        return result;
    }
}