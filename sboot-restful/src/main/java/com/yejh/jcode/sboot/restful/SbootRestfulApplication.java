package com.yejh.jcode.sboot.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SbootRestfulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbootRestfulApplication.class, args);
    }

}
