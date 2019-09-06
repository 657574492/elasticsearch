package com.wjj.elasticsearch.example;

import com.wjj.elasticsearch.example.config.properties.WjjProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WjjProperties.class)
public class EsExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsExampleApplication.class, args);
    }

}
