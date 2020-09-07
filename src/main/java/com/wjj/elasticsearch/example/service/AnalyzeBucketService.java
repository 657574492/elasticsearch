package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.GoodsDocument;
import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.Stats;
import org.elasticsearch.search.aggregations.pipeline.ParsedBucketMetricValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangjunjie 2019/8/26 15:20
 * @Description: 聚合分析 查询bucket
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/26 15:20
 */

@Service
public class AnalyzeBucketService {

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     *
     * 聚合查询bucket 一个字段不同数据 （编号2）
     * @throws IOException
     */
    public void analyzeQuery1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("terms_brandName").field("brandName").size(10);
        sourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();

        Terms terms = aggregations.get("terms_brandName");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println("key:"+bucket.getKey());
            //类型数量
            System.out.println("doc_count:"+bucket.getDocCount());
        }
    }

    /**
     *
     * 聚合查询bucket 一个字段不同数据 以字段数量进行排序（默认是以字段数量倒序排列） （编号3）
     * @throws IOException
     */
    public void analyzeQuery7() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders
                .terms("terms_brandName")
                .field("brandName")
                .order(BucketOrder.count(true));
        sourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();

        Terms terms = aggregations.get("terms_brandName");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println("key:"+bucket.getKey());
            //类型数量
            System.out.println("doc_count:"+bucket.getDocCount());
        }
    }

    /**
     * 聚合查询bucket 嵌套 top_hits 展示分组后的数据详情并排序 (分组排序) （编号4）
     * @throws IOException
     */
    public void analyzeQuery2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("terms_categoryId").field("categoryId").size(10);
        aggregationBuilder.subAggregation(AggregationBuilders
                .topHits("max_price").size(1).sort("price",SortOrder.DESC));
        sourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        Terms terms = aggregations.get("terms_categoryId");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println("key:"+bucket.getKey());
            //类型数量
            System.out.println("doc_count:"+bucket.getDocCount());

            ParsedTopHits topHits = bucket.getAggregations().get("max_price");
            SearchHits hits = topHits.getHits();
            for (SearchHit hit : hits) {
                String hitString = hit.getSourceAsString();
                GoodsDocument goodsDocument = GsonUtil.parse(hitString, GoodsDocument.class);
                System.out.println(GsonUtil.toJSONStringg(goodsDocument));
            }
        }

    }

    /**
     * 聚合查询bucket 嵌套 （编号5）
     * @throws IOException
     */
    public void analyzeQuery3() throws IOException {

        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("terms_brandName").field("brandName").size(10);
        sourceBuilder.aggregation(aggregationBuilder);
        //可以先根据条件查询，再统计
        sourceBuilder.query(QueryBuilders.rangeQuery("price").lte(100000));

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get("terms_brandName");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println("key:"+bucket.getKey());
            //类型数量
            System.out.println("doc_count:"+bucket.getDocCount());

        }
    }

    /**
     * 聚合查询 查询字段区间值 from 包括 to 不包括 （编号6）
     * @throws IOException
     */
    public void analyzeQuery4() throws IOException {

        SearchRequest searchRequest = new SearchRequest("shop_goods");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        RangeAggregationBuilder aggregationBuilder = AggregationBuilders.range("range_price")
                .field("price")
                .addUnboundedTo("range1", 200000)
                .addRange("range2", 200000, 400000)
                .addRange("range3", 400000, 600000)
                .addUnboundedFrom("ange4", 600000);
        sourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        ParsedRange terms = aggregations.get("range_price");
        List<? extends Range.Bucket> buckets = terms.getBuckets();
        for (Range.Bucket bucket : buckets) {
            System.out.println("key:"+bucket.getKey());
            //类型数量
            System.out.println("doc_count:"+bucket.getDocCount());
            System.out.println("form:"+bucket.getFrom());
            System.out.println("to:"+bucket.getTo());
            System.out.println("----------------------");
        }

    }

    /**
     *
     * 聚合查询 pipeline 聚合分析 (分组后：求分组后某一属性最大或最小的组) （编号7）
     *
     * @throws IOException
     */
    public void analyzeQuery5() throws IOException {

        SearchRequest searchRequest = new SearchRequest("shop_goods");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("terms_categoryId").field("categoryId");
        aggregationBuilder.subAggregation(AggregationBuilders.avg("avg_price").field("price"));
        //pipeline 聚合分析 查询平均年龄最小的 type类型
        PipelineAggregationBuilder pipelineAggregationBuilder = PipelineAggregatorBuilders.minBucket("min_category_price", "terms_categoryId>avg_price");
        sourceBuilder.aggregation(aggregationBuilder);
        sourceBuilder.aggregation(pipelineAggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get("terms_categoryId");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKey());
            System.out.println("count:"+bucket.getDocCount());
            ParsedAvg parsedAvg = bucket.getAggregations().get("avg_price");
            System.out.println(parsedAvg.getValue());
            System.out.println("----------------");
        }

        ParsedBucketMetricValue bucketMetricValue = aggregations.get("min_category_price");
        String key = bucketMetricValue.keys()[0];
        System.out.println(key);
        System.out.println(bucketMetricValue.value());
    }


    /**
     * 聚合查询结果排序 （编号8）
     * @throws IOException
     */
    public void analyzeQuery6() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");


        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("terms_categoryId").field("categoryId");
        AggregationBuilder orderAggregationBuilder = ((TermsAggregationBuilder) aggregationBuilder)
                .order(BucketOrder.aggregation("avg_price", false)).size(10);
        orderAggregationBuilder.subAggregation(AggregationBuilders.avg("avg_price").field("price"));
        sourceBuilder.aggregation(orderAggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get("terms_categoryId");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKey());
            System.out.println("count:"+bucket.getDocCount());
            ParsedAvg parsedAvg = bucket.getAggregations().get("avg_price");
            System.out.println(parsedAvg.getValue());
            System.out.println("----------------");
        }


    }
}
