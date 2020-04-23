package com.wjj.elasticsearch.elk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author EDZ
 * @date 2020/4/9
 * @description TODO
 */

@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @GetMapping("/info")
    public String testInfoElk() {
        log.info("我去华为中华人民共和国美女1");
        return "info";
    }

    @GetMapping("/erroe")
    public String testErrorElk() {
        log.info("我去华为中华人民共和国美女2");
        try {
            int i=10/0;
        }catch (Exception e) {
            log.info("我去华为中华人民共和国美女3");
            log.error("出错了，请联系管理员",e);
        }
        return "error";
    }
}
