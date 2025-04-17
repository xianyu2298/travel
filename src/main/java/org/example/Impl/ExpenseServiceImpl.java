package org.example.Impl;

import org.example.entity.Expense;
import org.example.mapper.ExpenseMapper;
import org.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseMapper expenseMapper;

    @Override
    public boolean addExpense(Expense expense) {
        return expenseMapper.addExpense(expense) > 0;
    }

    @Override
    public List<Expense> getExpensesByUserId(Integer userId) {
        return expenseMapper.getExpensesByUserId(userId);
    }
}