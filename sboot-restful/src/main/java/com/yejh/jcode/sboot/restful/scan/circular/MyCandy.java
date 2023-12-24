package com.yejh.jcode.sboot.restful.scan.circular;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
@ToString
public class MyCandy implements ICandy {

    private String cname = "lisi";

    @Autowired
    private IUser user;

}
