package com.wjj.elasticsearch.example.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: wangjunjie 2019/8/24 17:28
 * @Description: 聚合分析 查询metric
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/24 17:28
 */

@Service
public class AnalyzeMetricService {

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     *
     * 聚合查询metric 最小值（编号9）
     * @throws IOException
     */
    public void analyzeQuery1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.min("min_price").field("price");
        sourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        //查询的数据总数
        System.out.println("...total: "+hits.getTotalHits().value);

        Aggregation aggregation = response.getAggregations().get("min_price");
        ParsedMin parsedMin = (ParsedMin) aggregation;

        //获取查询结果
        System.out.println(parsedMin.getValue());
    }

    /**
     * 聚合查询metric 多个字段 （编号10）
     * @throws IOException
     */
    public void analyzeQuery2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder1 = AggregationBuilders.min("min_price").field("price");
        AggregationBuilder aggregationBuilder2 = AggregationBuilders.max("max_price").field("price");
        sourceBuilder.aggregation(aggregationBuilder1);
        sourceBuilder.aggregation(aggregationBuilder2);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        System.out.println("...total: "+hits.getTotalHits().value);

        Map<String, Aggregation> asMap = response.getAggregations().asMap();
        ParsedMin parsedMin = (ParsedMin) asMap.get("min_price");
        ParsedMax parsedMax = (ParsedMax) asMap.get("max_price");

        //获取查询结果
        System.out.println(parsedMin.getValue());
        System.out.println(parsedMax.getValue());


    }

    /**
     *
     * 聚合查询metric 一个字段所有数据 stats （编号11）
     * @throws IOException
     */
    public void analyzeQuery3() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        StatsAggregationBuilder aggregationBuilder = AggregationBuilders.stats("stats_price").field("price");
        sourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregation aggregation = searchResponse.getAggregations().get("stats_price");
        ParsedStats stats = (ParsedStats) aggregation ;
        System.out.println("avg:"+stats.getAvg()+" :"+
                stats.getMax()+" :"+
                stats.getMin()+" :"+
                stats.getSum()+" :"+stats.getCount());
    }

    /**
     *
     * 聚合查询metric 一个字段不同数据的数量 （编号12）
     * @throws IOException
     */
    public void analyzeQuery4() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        CardinalityAggregationBuilder aggregationBuilder = AggregationBuilders.cardinality("cardinality_price").field("price");
        sourceBuilder.aggregation(aggregationBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregation aggregation = response.getAggregations().get("cardinality_price");
        ParsedCardinality cardinalityAge = (ParsedCardinality) aggregation;
        System.out.println(cardinalityAge.getValue());
    }
}
