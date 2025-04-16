package org.example.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Expense {
    private Integer id;
    private Integer travelPlanId;
    private Integer userId;
    private BigDecimal transportation;
    private BigDecimal accommodation;
    private BigDecimal dining;
    private BigDecimal tickets;
    private BigDecimal shopping;
    private BigDecimal others;
    private BigDecimal total;
    private Date createdAt;
    private String planName;
}