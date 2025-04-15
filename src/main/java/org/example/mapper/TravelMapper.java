package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Travel;
import java.util.List;

@Mapper
public interface TravelMapper {
    List<Travel> getTravelPlansByUserId(Integer userId);
    int addTravelPlan(Travel travel);
}
