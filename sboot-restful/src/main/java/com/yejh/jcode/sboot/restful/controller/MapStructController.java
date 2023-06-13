package com.yejh.jcode.sboot.restful.controller;

import com.yejh.jcode.sboot.restful.pojo.ReqBO;
import com.yejh.jcode.sboot.restful.pojo.ReqEntityMapper;
import com.yejh.jcode.sboot.restful.pojo.ReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2022-12-09
 * @since 1.0.0
 */
@RestController
@RequestMapping("/mapstruct")
@Slf4j
public class MapStructController {

    @PostMapping("/vo2bo2vo")
    public ReqVO mapstruct(@RequestBody ReqVO reqVO) {
        log.info("reqVO: {}", reqVO);
        ReqBO reqBO = ReqEntityMapper.INSTANCE.reqVOToBO(reqVO);
        log.info("reqBO: {}", reqBO);
        ReqVO newVO = ReqEntityMapper.INSTANCE.reqBOToVO(reqBO);
        log.info("newVO: {}", newVO);
        return newVO;
    }
}
