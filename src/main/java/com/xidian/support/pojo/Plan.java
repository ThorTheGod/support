package com.xidian.support.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author thornoir
 * @date 2022/1/17
 * @apiNote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    private Integer planId;
    private Integer userId;
    private String category;
    private String planProvince;
    private String planCity;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planTime;
    //属性
    private Integer temp1;
    private Integer temp2;
    private Integer humidity1;
    private Integer humidity2;
    private Integer no2Content;
    private String weather;
    //要素
    private Integer newCases;
    private Integer confirmedCases;
    private Integer closeContact;
    private Integer asymptomatic;
    //方案
    private String medical;
    private String transport;
    private String livelihood;
    //实施效果影响
    private String epidemic;
    private Integer epidemicScore;  //N城市总人口
    private String economic;
    private Integer economicScore; //R治愈死亡数
    private String social;
    private Integer socialScore;


}
