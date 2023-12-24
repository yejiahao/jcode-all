package com.yejh.jcode.sboot.restful.scan.circular;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@EnableTransactionManagement
@Transactional
public class MyBook implements IBook {

    private long bookId = 1L;

    private String bookName = "JVM";

    @Autowired
    private ICandy candy;

    @PostConstruct
    public void initBook() {
        bookId = 100L;
        bookName = "Spring";
    }

    @Override
    public void borrowRecord() {
        log.info("borrow record.");
    }

    @Override
    public String toString() {
        return "MyBook{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
