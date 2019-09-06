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
public class QueryCompletionServiceTest {

    @Autowired
    private QueryCompletionService queryCompletionService;

    @Test
    public void queryCompletion1() throws IOException {
        queryCompletionService.queryCompletion1();
    }
}