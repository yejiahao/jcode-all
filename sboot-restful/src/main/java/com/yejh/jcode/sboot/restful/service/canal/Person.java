package com.yejh.jcode.sboot.restful.service.canal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    /*
    create table if not exists sync_vm.person
    (
        id          bigint primary key not null auto_increment,
        age         int                not null,
        name        varchar(20)        not null,
        sex         tinyint            not null,
        has_check   bool               not null default true,
        create_time timestamp          not null default current_timestamp,
        update_time timestamp                   default current_timestamp on update current_timestamp
    )
     */
    private Long id;
    private int age;
    private String name;
    private short sex;
    private boolean hasCheck;
    private Date createTime;
    private Date updateTime;
}

