package com.wjj.elasticsearch.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangjunjie 2019/8/15 16:11
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/15 16:11
 */

@ConfigurationProperties("wjj")
public class WjjProperties {

    private List<EsProperties> es = new ArrayList();

    private String name;

    public List<EsProperties> getEs() {
        return es;
    }

    public void setEs(List<EsProperties> es) {
        this.es = es;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
