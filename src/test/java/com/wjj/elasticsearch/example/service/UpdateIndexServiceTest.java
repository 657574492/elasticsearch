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
public class UpdateIndexServiceTest {

    @Autowired
    private UpdateIndexService updateIndexService;

    @Test
    public void updateIndex1() throws IOException {
        updateIndexService.updateIndex1();
    }
}