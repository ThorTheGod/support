package com.xidian.support.mapper;

import com.xidian.support.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thornoir
 * @date 2022/1/18
 * @apiNote
 */
@Repository
public interface UserMapper {

    void addUser(User user);

    void deleteUser(int userId);

    void updateUser(User user);

    User findUserByUserId(int userId);

    User findUserByUsername(String username);

    List<User> getAllUser();

}
