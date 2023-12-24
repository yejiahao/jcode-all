package com.yejh.jcode.sboot.restful.scan;

import com.yejh.jcode.sboot.restful.scan.circular.IUser;
import com.yejh.jcode.sboot.restful.scan.circular.MyUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class ScanBeanMain {

    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext("com.yejh.jcode.sboot.restful.scan");
        IUser myUser = ac.getBean("myUser", MyUser.class);
        log.info("myUser: {}", myUser.addCnt());
    }
}
