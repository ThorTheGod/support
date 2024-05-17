package com.xidian.support.util;

import com.xidian.support.pojo.Plan;
import com.xidian.support.pojo.Score;

import java.util.Arrays;

/**
 * @author thornoir
 * @date 2022/1/24
 * @apiNote
 */
public class ScoreUtil {

    /**
     * 根据固定权重计算出某方案的三时期得分
     * @param plan
     * @return
     */
    public static double[] calScore(Plan plan){
        double[] scores = new double[3];
        scores[0] = 0.4367*plan.getEpidemicScore()+0.2516*plan.getEconomicScore()+0.3117*plan.getSocialScore();
        scores[1] = 0.435*plan.getEpidemicScore()+0.25*plan.getEconomicScore()+0.315*plan.getSocialScore();
        scores[2] = 0.3467*plan.getEpidemicScore()+0.3266*plan.getEconomicScore()+0.3267*plan.getSocialScore();

        scores[0] = ((double)(int)(scores[0]*1000))/1000;
        scores[1] = ((double)(int)(scores[1]*1000))/1000;
        scores[2] = ((double)(int)(scores[2]*1000))/1000;
        return scores;
    }

    public static double calScore(Plan plan,String period){
        double score = 0.0;
        switch (period){
            case "潜伏期":score =
                    0.4367*plan.getEpidemicScore()+0.2516*plan.getEconomicScore()+0.3117*plan.getSocialScore();break;
            case "爆发期":score =
                    0.435*plan.getEpidemicScore()+0.25*plan.getEconomicScore()+0.315*plan.getSocialScore();break;
            case "恢复期":score =
                    0.3467*plan.getEpidemicScore()+0.3266*plan.getEconomicScore()+0.3267*plan.getSocialScore();break;
        }
        score = ((double)(int)(score*1000))/1000;
        return score;
    }


}
