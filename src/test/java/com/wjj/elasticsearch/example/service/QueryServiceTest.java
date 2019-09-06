package com.wjj.elasticsearch.example.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryServiceTest {

    @Autowired
    private QueryService queryService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void query1() throws IOException {
        queryService.query1();
    }

    @Test
    public void query2() throws IOException {
        queryService.query2();
    }

    @Test
    public void query3() throws IOException {
        queryService.query3();
    }

    @Test
    public void query4() throws IOException {
        queryService.query4();
    }

    @Test
    public void query5() throws IOException {
        queryService.query5();
    }

    @Test
    public void query6() throws IOException {
        queryService.query6();
    }

    @Test
    public void query10() throws IOException {
        queryService.query10();
    }

    @Test
    public void query11() throws IOException {
        queryService.query11();
    }

    @Test
    public void query12() throws IOException {
        queryService.wildcardQuery();
    }

    @Test
    public void query13() throws IOException {
        queryService.prefixQuery();
    }
}