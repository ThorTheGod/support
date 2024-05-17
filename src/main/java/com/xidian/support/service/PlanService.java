package com.xidian.support.service;

import com.xidian.support.mapper.FollowMapper;
import com.xidian.support.mapper.PlanMapper;
import com.xidian.support.mapper.ScoreMapper;
import com.xidian.support.mapper.UserMapper;
import com.xidian.support.pojo.Follow;
import com.xidian.support.pojo.Plan;
import com.xidian.support.pojo.Score;
import com.xidian.support.pojo.User;
import com.xidian.support.util.ExcelUtil;
import com.xidian.support.util.PythonUtil;
import com.xidian.support.util.ScoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thornoir
 * @date 2022/1/23
 * @apiNote
 */
@Service
public class PlanService {
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FollowMapper followMapper;

    /**
     * 新增方案并计算时期分数后存储
     * @param plan 新方案
     */
    public void addPlan(Plan plan){
        planMapper.addPlan(plan);
        //权重计算获得scores[]并产生score对象
        double[] scores = ScoreUtil.calScore(plan);
        Score score = new Score(null,plan.getPlanId(),scores[0]*100,scores[1]*100,scores[2]*100);

        scoreService.addScore(score);
    }

    public void deletePlan(int planId){
        planMapper.deletePlan(planId);
    }

    public void updatePlan(Plan plan){
        planMapper.updatePlan(plan);
    }

    public List<Map<String,Object>> findPlanByPlanId(int planId){
        Map<String,Object> res = new HashMap<>();
        Plan plan = planMapper.findPlanByPlanId(planId);
        List<Plan> plans = new ArrayList<>();
        plans.add(plan);
        return getPlanScores(plans);
    }


    /**
     * 获得方案列表，组装并返回方案得分列表
     * @param plans
     * @return
     */
    public List<Map<String,Object>> getPlanScores(List<Plan> plans){
        List<Map<String,Object>> planScores = new ArrayList<>();
        for(Plan plan:plans){
            Map<String,Object> obj = new HashMap<>();
            obj.put("Score",scoreService.findScoreByPlanId(plan.getPlanId()));
            obj.put("Plan",plan);
            obj.put("User",userMapper.findUserByUserId(plan.getUserId()));
            planScores.add(obj);
        }
        return planScores;
    }


    public List<Map<String,Object>> findPlanByUserId(int userId){
        List<Plan> plans = planMapper.findPlanByUserId(userId);
        return getPlanScores(plans);
    }


    public List<Map<String,Object>> findPlanByTime(String preTime,String sufTime){

        List<Plan> plans = planMapper.findPlanByTime(preTime,sufTime);
        return getPlanScores(plans);
    }

    public List<Map<String,Object>> getAllPlan(){
        List<Plan> plans = planMapper.getAllPlan();
        return getPlanScores(plans);
    }
    public List<Map<String,Object>> findPlanByCategory(String category){
        List<Plan> plans = planMapper.findPlanByCategory(category);
        return getPlanScores(plans);
    }

    public List<Map<String,Object>> getAllPlanAndFollow(User user){
        List<Plan> plans = planMapper.getAllPlan();
        List<Map<String,Object>> planScores = new ArrayList<>();
        for(Plan plan:plans){
            Map<String,Object> obj = new HashMap<>();
            obj.put("Score",scoreService.findScoreByPlanId(plan.getPlanId()));
            obj.put("Plan",plan);
            obj.put("User",userMapper.findUserByUserId(plan.getUserId()));
            //获得方案作者的所有粉丝
            List<Follow> follows = followMapper.findFollowByUserId1(plan.getUserId());
            obj.put("ifFollow","No");
            for(Follow follow:follows){
                if(follow.getUserId2()==user.getUserId()){
                    obj.put("ifFollow","Yes");
                    break;
                }
            }
            planScores.add(obj);
        }
        return planScores;
    }

    /**
     * 根据输入的目标案例，返回相似度前三的planId+sim相似度
     * @param tarPlan 目标案例
     * @param period 时期
     * @return
     */
    public List<Map<String,Object>> match(Plan tarPlan,String period,double threshold){
        //获得所有方案用于和目标案例计算出相似度
        List<Plan> plans = planMapper.getAllPlan();
        double[] planIds = new double[plans.size()+1];
//        int[] planIds = new int[24];
//        for (int i = 0; i < 24; i++) {
//            planIds[i] = i+1;
//        }
        //填充方案编号
        int i=0;
        for(Plan plan:plans){
            planIds[i++] = plan.getPlanId();
        }
        planIds[i]=threshold;
        //根据现有案例和目标案例，计算生成属性相似度excel文件sim.xls
        ExcelUtil.export(plans,tarPlan);

        //根据sim.xls计算出每个方案的最终相似度值，并排序返回前三个案例
        List<Map<String,Object>> res = PythonUtil.calTotalSim(planIds);
        Double calScore = 0.0;
        //查出planId的方案，用于计算得分
        for (Map<String,Object> re:res) {
            Plan plan = planMapper.findPlanByPlanId((int)re.get("planId"));
//            res.get(j).put("Score",ScoreUtil.calScore(plan,period));
            Score score = new Score();
            calScore = ScoreUtil.calScore(plan,period);
            switch (period){
                case "潜伏期":score.setInfancyScore(calScore);break;
                case "爆发期":score.setOutbreakScore(calScore);break;
                case "恢复期":score.setRecoveryScore(calScore);break;
            }
            re.put("Score",score);
            re.put("Plan",plan);
        }

        return res;
    }

}
