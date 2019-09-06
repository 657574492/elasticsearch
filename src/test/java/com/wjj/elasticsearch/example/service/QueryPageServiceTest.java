package com.wjj.elasticsearch.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class QueryPageServiceTest {

    @Autowired
    private QueryPageService queryPageService;

    @Test
    public void queryPage1() throws IOException {
        queryPage(null,1);
    }


    private void queryPage(Object[] values,int page) throws IOException {
        System.out.println("......page: "+page);
        Map<String, Object> map = queryPageService.queryPage1(values);
        if (map == null) {
            return;
        }
        queryPage((Object[])map.get("sortValues"),page+1);
    }
}