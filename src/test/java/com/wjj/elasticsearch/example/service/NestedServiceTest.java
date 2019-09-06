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
public class NestedServiceTest {

    @Autowired
    private NestedService nestedService;

    @Test
    public void nestedService1() throws IOException {
        nestedService.nestedService1();
    }

    @Test
    public void nestedService2() throws IOException {
        nestedService.nestedService2();
    }

    @Test
    public void nestedService3() throws IOException {
        nestedService.nestedService3();
    }
}