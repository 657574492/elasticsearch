package com.wjj.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author EDZ
 * @date 2020/4/28
 * @description TODO
 */

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public String test(Exception ex, HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        log.error(url, ex);
        return "error";
    }
}