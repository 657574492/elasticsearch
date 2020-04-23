package com.wjj.elasticsearch.shop.controller;


import com.wjj.elasticsearch.shop.service.EsIndexService;
import com.wjj.elasticsearch.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/es")
public class EsIndexController {

    @Autowired
    private EsIndexService esIndexService;
    @Autowired
    private ShopService shopService;

    @GetMapping("/creat/shop")
    public String createShopIndex() throws IOException {
        esIndexService.createShopIndex();
        return "success";
    }

    @GetMapping("/search")
    public Map<String,Object> search() throws IOException {
        return shopService.searchES(new BigDecimal("121.48789949"),
                new BigDecimal("31.23916171"),"凯悦",
                null,2,null);
    }

}
