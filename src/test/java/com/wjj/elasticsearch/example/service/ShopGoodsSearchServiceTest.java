package com.wjj.elasticsearch.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopGoodsSearchServiceTest {

    @Autowired
    private ShopGoodsSearchService shopGoodsSearchService;

    @Test
    public void query1() throws IOException {
        shopGoodsSearchService.query1("小米");
    }
}