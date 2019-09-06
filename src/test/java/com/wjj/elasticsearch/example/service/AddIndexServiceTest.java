package com.wjj.elasticsearch.example.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddIndexServiceTest {

    @Autowired
    private AddIndexService addIndexService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addIndex() throws IOException {
        addIndexService.addIndex();
    }

    @Test
    public void addIndex2() throws IOException {
        addIndexService.addIndex2();
    }

    @Test
    public void addIndex3() throws IOException {
        addIndexService.addIndex3();
    }
}