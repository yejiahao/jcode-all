package com.yejh.jcode.sboot.restful.service;

public interface DatabaseService {

    String listDataBySF() throws Exception;

    String listDataByDS() throws Exception;

    String listDataByMP(long userID) throws Exception;
}
