package com.wjj.elasticsearch.example.controller;

import com.wjj.elasticsearch.example.domain.index.GoodsDocument;
import com.wjj.elasticsearch.example.service.HighLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author EDZ
 * @date 2020/7/30
 * @description TODO
 */

@RestController
@RequestMapping("/example")
public class ExampleController {

    @Autowired
    private HighLightService highLightService;

  /*  @GetMapping("/highLight")
    private GoodsDocument highLightTest() {
        return highLightService.qurey1();
    }*/
}
