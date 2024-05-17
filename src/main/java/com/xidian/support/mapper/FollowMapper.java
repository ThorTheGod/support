package com.xidian.support.mapper;

import com.xidian.support.pojo.Follow;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thornoir
 * @date 2022/4/7
 * @apiNote
 */
@Repository
public interface FollowMapper {
    //userId1:主角；userId2:粉丝;

    void addFollow(Follow follow);

    void cancelFollow(Integer followId);

    List<Follow> findFollowByUserId1(Integer userId1);

    Follow findFollowByUserId12(Integer userId1,Integer userId2);

    List<Follow> findFollowByUserId2(Integer userId2);


}
