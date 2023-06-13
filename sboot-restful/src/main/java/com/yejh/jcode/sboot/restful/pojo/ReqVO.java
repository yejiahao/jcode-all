package com.yejh.jcode.sboot.restful.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReqVO {

    private Integer number;

    private String name;

    private Date birthday;

}
