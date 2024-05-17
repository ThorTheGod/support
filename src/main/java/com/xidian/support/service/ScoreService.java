package com.xidian.support.service;

import com.xidian.support.mapper.ScoreMapper;
import com.xidian.support.pojo.Plan;
import com.xidian.support.pojo.Score;
import com.xidian.support.util.ScoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author thornoir
 * @date 2022/1/23
 * @apiNote
 */
@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private PlanService planService;


    public void addScore(Score score){
        scoreMapper.addScore(score);
    }

    public Score findScoreByPlanId(int planId){
        Score score = scoreMapper.findScoreByPlanId(planId);
        score.setInfancyScore(score.getInfancyScore()/100);
        score.setOutbreakScore(score.getOutbreakScore()/100);
        score.setRecoveryScore(score.getRecoveryScore()/100);
        return score;
    }

    /**
     * 修改了权重，则更新所有方案的三个时期的得分值
     */
    public void updateAllScore(){
        List<Map<String,Object>> planScores = planService.getAllPlan();
        Score score;
        double[] scores;
        for (Map<String,Object> planScore:planScores
             ) {
            //计算权重并赋值
            score = (Score)planScore.get("Score");
            scores = ScoreUtil.calScore((Plan)planScore.get("Plan"));
            score.setInfancyScore(scores[0]/100);
            score.setOutbreakScore(scores[1]/100);
            score.setRecoveryScore(scores[2]/100);
            scoreMapper.updateScore(score);
        }

    }

}
