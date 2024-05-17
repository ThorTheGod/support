package com.xidian.support.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author thornoir
 * @date 2022/4/7
 * @apiNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    private Integer followId;
    private Integer userId1;
    private Integer userId2;



}
