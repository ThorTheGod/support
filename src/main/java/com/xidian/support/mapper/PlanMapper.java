package com.xidian.support.mapper;

import com.xidian.support.pojo.Plan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thornoir
 * @date 2022/1/18
 * @apiNote
 */
@Repository
public interface PlanMapper {

    void addPlan(Plan plan);

    void deletePlan(int planId);

    void updatePlan(Plan plan);

    Plan findPlanByPlanId(int planId);

    List<Plan> findPlanByUserId(int userId);

    List<Plan> findPlanByTime(String preTime,String sufTime);

    List<Plan> getAllPlan();


    List<Plan> findPlanByCategory(String category);
}
