package com.wjj.elasticsearch.example.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.net.InetSocketAddress;

/**
 * @author wangjunjie
 * @date 2020/4/7
 * @description TODO
 */

//@Configuration
public class CanalClient implements DisposableBean {

    private CanalConnector myCanalConnector;

    @Bean
    public CanalConnector myCanalConnector() {
        this.myCanalConnector = CanalConnectors.newClusterConnector(Lists.newArrayList(
                new InetSocketAddress("192.168.0.100", 11111))
                , "example", "canal", "123456");
        myCanalConnector.connect();
        myCanalConnector.subscribe();
        myCanalConnector.rollback();
        return myCanalConnector;

    }

    @Override
    public void destroy() throws Exception {
        if(myCanalConnector != null){
            myCanalConnector.disconnect();
        }
    }
}
