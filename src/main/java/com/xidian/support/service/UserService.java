package com.xidian.support.service;

import com.xidian.support.mapper.UserMapper;
import com.xidian.support.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author thornoir
 * @date 2022/1/23
 * @apiNote
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void addUser(User user){
        userMapper.addUser(user);
    }

    public void deleteUser(int userId){
        userMapper.deleteUser(userId);
    }

    public void updateUser(User user){
        userMapper.updateUser(user);
    }

    public User findUserByUserId(int userId){
        return userMapper.findUserByUserId(userId);
    }

    public User findUserByUsername(String username){
        return userMapper.findUserByUsername(username);
    }

    public List<User> getAllUser(){
        return userMapper.getAllUser();
    }

}
