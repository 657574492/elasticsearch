package com.wjj.elasticsearch.shop.controller;


import com.wjj.elasticsearch.shop.service.EsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/es")
public class EsIndexController {

    @Autowired
    private EsIndexService esIndexService;

    @GetMapping("/creat/shop")
    public String createShopIndex() throws IOException {
        esIndexService.createShopIndex();
        return "success";
    }
}
