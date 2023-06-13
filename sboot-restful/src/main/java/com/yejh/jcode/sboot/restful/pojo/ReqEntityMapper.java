package com.yejh.jcode.sboot.restful.pojo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReqEntityMapper {

    ReqEntityMapper INSTANCE = Mappers.getMapper(ReqEntityMapper.class);

    @Mapping(target = "number", expression = "java(vo.getNumber() + 11)")
    @Mapping(target = "name222", expression = "java(translateName2BO(vo.getName()))")
    @Mapping(target = "birthday333", source = "birthday")
    ReqBO reqVOToBO(ReqVO vo);

    @Mapping(target = "name", expression = "java(translateName2VO(bo.getName222()))")
    @Mapping(target = "birthday", source = "birthday333")
    ReqVO reqBOToVO(ReqBO bo);

    default String translateName2VO(String name) {
        return name + ", haha";
    }

    default String translateName2BO(String name) {
        return name;
    }
}
