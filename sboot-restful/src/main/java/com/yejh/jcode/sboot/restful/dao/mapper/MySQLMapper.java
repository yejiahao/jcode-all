package com.yejh.jcode.sboot.restful.dao.mapper;

import com.yejh.jcode.sboot.restful.pojo.AccountDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MySQLMapper {

    List<AccountDO> listAccount(@Param("accountParam") AccountDO accountDO);
}
