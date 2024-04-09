package com.yejh.jcode.sboot.restful.controller;

import com.yejh.jcode.sboot.restful.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2022-12-09
 * @since 1.0.0
 */
@RestController
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/list-data-by-sf")
    public String listDataBySF() throws Exception {
        return databaseService.listDataBySF();
    }

    @GetMapping("/list-data-by-ds")
    public String listDataByDS() throws Exception {
        return databaseService.listDataByDS();
    }

    @GetMapping("/list-data-by-mp/{userid}")
    public String listDataByMP(@PathVariable("userid") long userID) throws Exception {
        return databaseService.listDataByMP(userID);
    }

}
