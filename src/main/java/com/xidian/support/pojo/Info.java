package com.xidian.support.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author thornoir
 * @date 2022/1/17
 * @apiNote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Info {

    private Integer infoId;
    private Integer userId;
    private String province;
    private String city;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    private String title;
    private String brief;
    private String content;

}
