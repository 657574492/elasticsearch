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
public class QueryGeoServiceTest {

    @Autowired
    private QueryGeoService geoService;

    @Test
    public void geoQuery1() throws IOException {
        geoService.geoQuery1();
    }
}