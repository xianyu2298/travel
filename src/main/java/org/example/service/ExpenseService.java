package org.example.service;

import org.example.entity.Expense;
import java.util.List;

public interface ExpenseService {
    boolean addExpense(Expense expense);
    List<Expense> getExpensesByUserId(Integer userId);
}