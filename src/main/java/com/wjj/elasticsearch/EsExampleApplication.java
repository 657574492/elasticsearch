package com.wjj.elasticsearch;

import com.wjj.elasticsearch.example.config.properties.WjjProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WjjProperties.class)
@MapperScan("com.wjj.elasticsearch.shop.dao")
public class EsExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsExampleApplication.class, args);
    }

}
