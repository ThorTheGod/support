package com.xidian.support.mapper;

import com.xidian.support.pojo.Score;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thornoir
 * @date 2022/1/18
 * @apiNote
 */
@Repository
public interface ScoreMapper {

    void addScore(Score score);

    void updateScore(Score score);

    Score findScoreByPlanId(int planId);

    List<Score> getAllScore();
}
