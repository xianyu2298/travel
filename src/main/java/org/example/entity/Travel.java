package org.example.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "travel_plan")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String planName;
    private Date travelDate;
    private String itinerary;

    // 新增字段
    private String startProvince;
    private String startCity;
    private String endProvince;
    private String endCity;
}
