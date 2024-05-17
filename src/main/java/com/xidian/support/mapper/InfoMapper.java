package com.xidian.support.mapper;

import com.xidian.support.pojo.Info;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thornoir
 * @date 2022/1/18
 * @apiNote
 */
@Repository
public interface InfoMapper {

    void addInfo(Info info);

    void deleteInfo(int infoId);

    void updateInfo(Info info);

    Info findInfoByInfoId(int infoId);

    List<Info> getAllInfo();

    List<Info> getInfoByUserId(int userId);

    List<Info> getInfoByProvince(String province);

    List<Info> getInfoByCity(String city);

}
