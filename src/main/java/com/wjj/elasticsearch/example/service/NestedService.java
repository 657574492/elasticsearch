package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.GoodsDocument;
import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Author: wangjunjie 2019/8/27 11:34
 * @Description: nested对象
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/27 11:34
 */

@Service
public class NestedService {

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     * 简单查询 (编号13)
     * @throws IOException
     */
    public void nestedService1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //productions nested对象名
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("sku.skuData.attribute", "内存");
        NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder("sku.skuData", termQueryBuilder, ScoreMode.None);
        sourceBuilder.query(nestedQueryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            GoodsDocument goodsDocument = GsonUtil.parse(hitString, GoodsDocument.class);
            System.out.println(goodsDocument.toString());
        }
    }

    /**
     * 查询 (编号14)
     * @throws IOException
     */
    public void nestedService2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //productions nested对象名
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.termQuery("sku.skuData.attribute", "内存"));
        boolQueryBuilder.must(QueryBuilders.termsQuery("sku.skuData.value","16"));
        NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder("sku.skuData", boolQueryBuilder, ScoreMode.None);
        sourceBuilder.query(nestedQueryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            GoodsDocument goodsDocument = GsonUtil.parse(hitString, GoodsDocument.class);
            System.out.println(goodsDocument.toString());
        }
    }


    /**
     * nested对象 聚合查询 (编号15)
     * @throws IOException
     */
    public void nestedService3() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("attribute", "sku.skuData");
        nestedAggregationBuilder.subAggregation(AggregationBuilders.terms("attribute_type").field("sku.skuData.attribute" ).size(20));
        sourceBuilder.aggregation(nestedAggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();

        ParsedNested parsedNested = aggregations.get("attribute");
        long docCount = parsedNested.getDocCount();
        System.out.println(docCount);

        Terms terms = parsedNested.getAggregations().get("attribute_type");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKey());
            System.out.println(bucket.getDocCount());
        }

    }
}
