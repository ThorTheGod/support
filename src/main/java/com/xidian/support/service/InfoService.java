package com.xidian.support.service;

import com.xidian.support.mapper.InfoMapper;
import com.xidian.support.mapper.UserMapper;
import com.xidian.support.pojo.Info;
import com.xidian.support.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thornoir
 * @date 2022/1/23
 * @apiNote
 */
@Service
public class InfoService {

    @Autowired
    private InfoMapper infoMapper;
    @Autowired
    private UserMapper userMapper;

    public void addInfo(Info info){
        infoMapper.addInfo(info);
    }

    public void deleteInfo(int infoId){
        infoMapper.deleteInfo(infoId);
    }

    public void updateInfo(Info info){
        infoMapper.updateInfo(info);
    }

    public Info findInfoByInfoId(int infoId){
        return infoMapper.findInfoByInfoId(infoId);
    }

    public List<Map<String,Object>> getAllInfo(){
        List<Map<String,Object>> infoUsers = new ArrayList<>();
        List<Info> infos = infoMapper.getAllInfo();
        for(Info info:infos){
            User user = userMapper.findUserByUserId(info.getUserId());
            Map<String,Object> infoUser = new HashMap<>();
            infoUser.put("user",user);
            infoUser.put("info",info);
            infoUsers.add(infoUser);
        }
        return infoUsers;
    }

    public List<Info> getInfoByUserId(int userId){
        return infoMapper.getInfoByUserId(userId);
    }

    public List<Info> getInfoByProvince(String province){
        return infoMapper.getInfoByProvince(province);
    }

    public List<Info> getInfoByCity(String city){
        return infoMapper.getInfoByCity(city);
    }
}
