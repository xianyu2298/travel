package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Expense;

import java.util.List;

@Mapper
public interface ExpenseMapper {
    int addExpense(Expense expense);
    List<Expense> getExpensesByUserId(Integer userId);
}