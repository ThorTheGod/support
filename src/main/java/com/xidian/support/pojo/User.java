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
public class User {

    private Integer userId;
    private String username;
    private String email;
    private String password;
    private String headImg;
    private Integer priority;


}
