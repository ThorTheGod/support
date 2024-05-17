package com.xidian.support;

import com.xidian.support.mapper.FollowMapper;
import com.xidian.support.mapper.PlanMapper;
import com.xidian.support.mapper.ScoreMapper;
import com.xidian.support.mapper.UserMapper;
import com.xidian.support.pojo.Follow;
import com.xidian.support.pojo.Plan;
import com.xidian.support.pojo.Score;
import com.xidian.support.pojo.User;
import com.xidian.support.service.PlanService;
import com.xidian.support.service.ScoreService;
import com.xidian.support.util.ExcelUtil;
import com.xidian.support.util.PythonUtil;
import com.xidian.support.util.WordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@SpringBootTest
class SupportApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private PlanService planService;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private FollowMapper followMapper;
    @Test
    void contextLoads(){
//        Date date = new Date(2022, Calendar.JANUARY,21);
//        Plan plan = new Plan(null,3,"西藏","拉萨",date,20,20,10,"晴天",5,10,3,5,"医疗","交通","民生","可以",80,"还行",
//                70,"还行",70);
        Plan plan = new Plan(null,null,"新冠肺炎",null,null,null,4,17,64,94,19,"多云",9,26,1015,33,null,null,null,
                null,
                7450785,null,1,null,null);
//        System.out.println(planService.match(plan,"潜伏期",0.3));
//        WordUtil.genWord(planMapper.findPlanByPlanId(2));
//        System.out.println(planService.findPlanByPlanId(1));
    }


}
