package com.wjj.elasticsearch.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class QueryScoreServiceTest {

    @Autowired
    private QueryScoreService queryScoreService;

    @Test
    public void queryScore1() throws IOException {
        queryScoreService.queryScore1();
    }
}