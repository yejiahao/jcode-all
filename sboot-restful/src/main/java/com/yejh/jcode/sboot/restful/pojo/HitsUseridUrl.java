package com.yejh.jcode.sboot.restful.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("hits_UserID_URL")
public class HitsUseridUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "UserID")
    private Long userID;

    @TableField(value = "URL")
    private String url;

    @TableField(value = "EventTime")
    private Date eventTime;
}
