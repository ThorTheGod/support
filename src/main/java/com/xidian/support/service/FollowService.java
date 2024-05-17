package com.xidian.support.service;

import com.xidian.support.mapper.FollowMapper;
import com.xidian.support.mapper.PlanMapper;
import com.xidian.support.mapper.UserMapper;
import com.xidian.support.pojo.Follow;
import com.xidian.support.pojo.Plan;
import com.xidian.support.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thornoir
 * @date 2022/4/8
 * @apiNote
 */
@Service
public class FollowService {

    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PlanMapper planMapper;

    public List<Map<String,Object>> getFollowUsers(int userId2){
        List<Map<String,Object>> followUsers = new ArrayList<>();
        List<Follow> follows = followMapper.findFollowByUserId2(userId2);
        for(Follow follow:follows){
            Map<String,Object> followUser = new HashMap<>();
            User user1 = userMapper.findUserByUserId(follow.getUserId1());
            List<Plan> plans = planMapper.findPlanByUserId(user1.getUserId());
            followUser.put("user1",user1);
            followUser.put("follow",follow);
            followUser.put("plan",plans.size()==0?null:plans.get(0));
            followUsers.add(followUser);
        }
        return followUsers;
    }



}
