package com.yejh.jcode.sboot.restful.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDO {

    private Integer id;

    private String name;

    private Integer salary;
}
