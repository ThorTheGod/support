package com.xidian.support.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author thornoir
 * @date 2022/1/17
 * @apiNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    private Integer scoreId;
    private Integer planId;
    private double infancyScore;
    private double outbreakScore;
    private double recoveryScore;
}
