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
public class QueryComplexServiceTest {

    @Autowired
    private QueryComplexService queryComplexService;

    @Test
    public void queryComplex1() throws IOException {
        queryComplexService.queryComplex1();
    }

    @Test
    public void queryComplex2() throws IOException {
        queryComplexService.queryComplex2();
    }

    @Test
    public void queryComplex3() throws IOException {
        queryComplexService.queryComplex3();
    }

    @Test
    public void queryComplex4() throws IOException {
        queryComplexService.queryComplex4();
    }
}