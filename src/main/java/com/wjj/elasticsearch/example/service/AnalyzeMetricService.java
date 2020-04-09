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
     * 聚合查询metric 获取最小值
     *
     * GET star_document/_search
     * {
     *   "size": 0,
     *   "aggs": {
     *     "min_age": {
     *       "min": {
     *         "field": "age"
     *       }
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void analyzeQuery1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.min("min_age").field("age");
        sourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        //查询的数据总数
        //System.out.println("...total: "+hits.totalHits);

        Map<String, Aggregation> asMap = response.getAggregations().asMap();
        ParsedMin parsedMin = (ParsedMin) asMap.get("min_age");

        //获取查询结果
        System.out.println(parsedMin.getValue());
    }

    /**
     *
     * 聚合查询metric 多个字段
     * GET star_document/_search
     * {
     *   "size": 0,
     *   "aggs": {
     *     "min_age": {
     *       "min": {
     *         "field": "age"
     *       }
     *     },
     *     "max_age": {
     *       "max": {
     *         "field": "createDate"
     *       }
     *     }
     *   }
     *
     * }
     * @throws IOException
     */
    public void analyzeQuery2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder1 = AggregationBuilders.min("min_age").field("age");
        AggregationBuilder aggregationBuilder2 = AggregationBuilders.max("max_date").field("createDate");
        sourceBuilder.aggregation(aggregationBuilder1);
        sourceBuilder.aggregation(aggregationBuilder2);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        //查询的数据总数 todo
        //System.out.println("...total: "+hits.totalHits);

        Map<String, Aggregation> asMap = response.getAggregations().asMap();
        ParsedMin parsedMin = (ParsedMin) asMap.get("min_age");
        ParsedMax parsedMax = (ParsedMax) asMap.get("max_date");

        //获取查询结果
        System.out.println(parsedMin.getValue());
        System.out.println(parsedMax.getValue());

        //如果值是long类型 将被转化为科学计数
        //System.out.println(new BigDecimal(parsedMax.getValue()).toString());
    }

    /**
     *
     * 聚合查询metric 一个字段所有数据 stats
     * GET star_document/_search
     * {
     *   "size": 0,
     *   "aggs": {
     *     "stats_age": {
     *       "stats": {
     *         "field": "age"
     *       }
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void analyzeQuery3() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        StatsAggregationBuilder aggregationBuilder = AggregationBuilders.stats("stats_age").field("age");
        sourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Map<String, Aggregation> aggMap = searchResponse.getAggregations().asMap();
        ParsedStats stats = (ParsedStats) aggMap.get("stats_age");
        System.out.println("avg:"+stats.getAvg()+" :"+
                stats.getMax()+" :"+
                stats.getMin()+" :"+
                stats.getSum()+" :"+stats.getCount());
    }

    /**
     *
     * 聚合查询metric 一个字段不同数据的数量
     * GET star_document/_search
     * {
     *   "size": 0,
     *   "aggs": {
     *     "cardinality_age": {
     *       "cardinality": {
     *         "field": "age"
     *       }
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void analyzeQuery4() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        CardinalityAggregationBuilder aggregationBuilder = AggregationBuilders.cardinality("cardinality_age").field("age");
        sourceBuilder.aggregation(aggregationBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Map<String, Aggregation> asMap = response.getAggregations().asMap();
        ParsedCardinality cardinalityAge = (ParsedCardinality) asMap.get("cardinality_age");
        System.out.println(cardinalityAge.getValue());
    }
}
