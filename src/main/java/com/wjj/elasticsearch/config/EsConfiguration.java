package com.wjj.elasticsearch.config;

import com.wjj.elasticsearch.config.properties.EsProperties;
import com.wjj.elasticsearch.config.properties.WjjProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangjunjie 2019/8/15 16:07
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/15 16:07
 */

@Configuration
public class EsConfiguration {

    private static int connectTimeOut = 1000; // 连接超时时间
    private static int socketTimeOut = 30000; // 连接超时时间
    private static int connectionRequestTimeOut = 500; // 获取连接的超时时间

    private static int maxConnectNum = 100; // 最大连接数
    private static int maxConnectPerRoute = 100; // 最大路由连接数

    @Autowired
    private WjjProperties wjjProperties;

    @Bean
    public RestHighLevelClient rhlClient() {
        List<EsProperties> es = wjjProperties.getEs();
        List<HttpHost> hosts = new ArrayList<>();
        for (EsProperties e : es) {
            hosts.add(new HttpHost(e.getHost(),e.getPort()));
        }

        RestClientBuilder restClientBuilder = RestClient.builder(hosts.toArray(new HttpHost[0]));
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("elastic","123456"));

        // 异步httpclient连接延时配置
       restClientBuilder.setRequestConfigCallback(new RequestConfigCallback() {
            @Override
            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(connectTimeOut);
                requestConfigBuilder.setSocketTimeout(socketTimeOut);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
                return requestConfigBuilder;
            }
        });

       // 异步httpclient连接数配置
        restClientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(maxConnectNum);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                return httpClientBuilder;
            }

        } );
        return new RestHighLevelClient(restClientBuilder);
    }
}
