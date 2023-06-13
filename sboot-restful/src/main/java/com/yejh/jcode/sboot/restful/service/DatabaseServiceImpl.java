package com.yejh.jcode.sboot.restful.service;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.HandleHelper;
import cn.hutool.db.handler.RsHandler;
import com.yejh.jcode.base.json.JsonUtil;
import com.yejh.jcode.sboot.restful.dao.mapper.MySQLMapper;
import com.yejh.jcode.sboot.restful.pojo.AccountDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

    @Resource(name = "pgDataSource")
    private DataSource dataSource;

    @Resource(name = "mysqlSqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    /**
     * {@link SqlSessionFactory} 查 MySQL
     */
    @Override
    public String listDataBySF() throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            MySQLMapper mapper = sqlSession.getMapper(MySQLMapper.class);
            List<AccountDO> entityList = mapper.listAccount(AccountDO.builder().name("yejh").build());
            return JsonUtil.serialize2String(entityList);
        }
    }

    /**
     * {@link DataSource} 查 PostgreSQL
     */
    @Override
    public String listDataByDS() throws Exception {
        Db use = Db.use(dataSource);
        String sql = "SELECT * FROM public.empsalary ORDER BY id ASC";
        List<Entity> query = use.query(sql, (RsHandler<List<Entity>>) rs -> {
            ArrayList<Entity> entities = HandleHelper.handleRs(rs, new ArrayList<>(), false);
            entities.forEach(entity -> {
                entity.entrySet().forEach(entry -> {
                    Object value = entry.getValue();
                    if (value.getClass() == java.sql.Date.class) {
                        entry.setValue(new SimpleDateFormat("yyyy/MM-dd").format(value));
                    } else if (value.getClass() == java.sql.Timestamp.class) {
                        entry.setValue(new SimpleDateFormat("yyyy/MM-dd HH:mm:ss").format(value));
                    }
                });
            });
            return entities;
        }, new HashMap<>());
        return JsonUtil.serialize2String(query);
    }
}
