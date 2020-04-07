package com.wjj.elasticsearch.ik;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangjunjie
 * @date 2020/4/7
 * @description ik分词 热更新
 */

@Controller
@RequestMapping("/ik")
public class IkWordController {

    @GetMapping("/update")
    public void updateIkWords(HttpServletRequest request, HttpServletResponse response) {

    }
}
