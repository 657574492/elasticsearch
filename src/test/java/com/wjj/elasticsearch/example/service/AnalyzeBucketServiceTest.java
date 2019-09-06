package com.wjj.elasticsearch.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class AnalyzeBucketServiceTest {

    @Autowired
    private AnalyzeBucketService analyzeBucketService;

    @Test
    public void analyzeQuery1() throws IOException {
        analyzeBucketService.analyzeQuery1();
    }

    @Test
    public void analyzeQuery2() throws IOException {
        analyzeBucketService.analyzeQuery2();
    }

    @Test
    public void analyzeQuery3() throws IOException {
        analyzeBucketService.analyzeQuery3();
    }

    @Test
    public void analyzeQuery4() throws IOException {
        analyzeBucketService.analyzeQuery4();
    }

    @Test
    public void analyzeQuery5() throws IOException {
        analyzeBucketService.analyzeQuery5();
    }

    @Test
    public void analyzeQuery6() throws IOException {
        analyzeBucketService.analyzeQuery6();
    }


}