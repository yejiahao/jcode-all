package com.yejh.jcode.sboot.restful.mp;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class GenerateMbpCode {

    public static void main(String[] args) {
        final String url = "jdbc:clickhouse://localhost:8123/default";
        final String username = "";
        final String password = "";
        final String outputDir = "F:\\JetBrains\\IdeaProjects\\jcode-all\\sboot-restful\\src\\main\\java";
        final String xmlDir = "F:\\JetBrains\\IdeaProjects\\jcode-all\\sboot-restful\\src\\main\\resources\\com\\yejh\\jcode\\sboot\\restful\\dao\\mapper";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("yejiahao") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(outputDir); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                }))
                .packageConfig(builder -> {
                    builder.parent("com.yejh.jcode.sboot.restful") // 设置父包名
                            .moduleName("mp") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("hits_UserID_URL"); // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
