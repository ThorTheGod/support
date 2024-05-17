package com.xidian.support.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author thornoir
 * @date 2022/3/31
 * @apiNote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Integer commentId;
    private Integer planId;
    private Integer userId;
    private String content;
    private Integer thumb;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

}
