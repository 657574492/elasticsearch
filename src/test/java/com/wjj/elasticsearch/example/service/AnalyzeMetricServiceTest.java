package com.wjj.elasticsearch.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@SpringBootTest
@RunWith(SpringRunner.class)
public class AnalyzeMetricServiceTest {

    @Autowired
    private AnalyzeMetricService analyzeMetricService;

    @Test
    public void analyzeQuery1() throws IOException {
        analyzeMetricService.analyzeQuery1();
    }

    @Test
    public void analyzeQuery2() throws IOException {
        analyzeMetricService.analyzeQuery2();
    }

    @Test
    public void analyzeQuery3() throws IOException {
        analyzeMetricService.analyzeQuery3();
    }

    @Test
    public void analyzeQuery4() throws IOException {
        analyzeMetricService.analyzeQuery4();
    }
}