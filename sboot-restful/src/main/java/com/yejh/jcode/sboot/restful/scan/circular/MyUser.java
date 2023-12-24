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
public class MyUser implements IUser {

    private int age = 23;

    private String name = "zhangsan";

    @Autowired
    private IBook book;

    @Override
    public IUser addCnt() {
        log.info("add cnt: {}", ++age);
        return this;
    }
}
