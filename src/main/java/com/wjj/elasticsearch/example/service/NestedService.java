package com.wjj.elasticsearch.example.service;

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
     * 简单查询
     *
     * GET star_document/_search
     * {
     *   "query": {
     *     "nested": {
     *       "path": "productions",
     *       "query": {
     *         "term": {
     *           "productions.name": {
     *             "value": "咱们结婚吧"
     *           }
     *         }
     *       }
     *     }
     *   }
     * }
     *
     * @throws IOException
     */
    public void nestedService1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //productions nested对象名
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("productions.name", "咱们结婚吧");
        NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder("productions", termQueryBuilder, ScoreMode.None);
        sourceBuilder.query(nestedQueryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.GsonToBean(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     * 简单查询
     *
     * GET star_document/_search
     * {
     *   "query": {
     *     "nested": {
     *       "path": "productions",
     *       "query": {
     *           "bool": {
     *             "must": [
     *               {
     *                 "term": {
     *                   "productions.name": {
     *                     "value": "咱们结婚吧"
     *                   }
     *                 }
     *               },
     *               {
     *                 "term": {
     *                   "productions.type": {
     *                     "value": "电视剧"
     *                   }
     *                 }
     *               }
     *             ]
     *           }
     *       }
     *     }
     *   }
     * }
     *
     * @throws IOException
     */
    public void nestedService2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        TermQueryBuilder queryBuilder1 = QueryBuilders.termQuery("productions.name", "咱们结婚吧");
        TermQueryBuilder queryBuilder2 = QueryBuilders.termQuery("productions.type", "电视剧");
        boolQueryBuilder.must(queryBuilder1);
        boolQueryBuilder.must(queryBuilder2);

        NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder("productions", boolQueryBuilder, ScoreMode.None);
        sourceBuilder.query(nestedQueryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.GsonToBean(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }


    /**
     * nested对象 聚合查询
     * GET star_document/_search
     * {
     *   "size": 0,
     *   "aggs": {
     *     "productions": {
     *       "nested": {
     *         "path": "productions"
     *       },
     *       "aggs":{
     *       "productions_name" : {
     *         "terms": {
     *           "field": "productions.type",
     *           "size": 1
     *         }
     *       }
     *     }
     *
     *     }
     *
     *   }
     * }
     * @throws IOException
     */
    public void nestedService3() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("productions", "productions");
        nestedAggregationBuilder.subAggregation(AggregationBuilders.terms("productions_type").field("productions" +
                ".type").size(20));
        sourceBuilder.aggregation(nestedAggregationBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();

        ParsedNested parsedNested = aggregations.get("productions");
        long docCount = parsedNested.getDocCount();
        System.out.println(docCount);

        Terms terms = parsedNested.getAggregations().get("productions_type");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKey());
            System.out.println(bucket.getDocCount());
        }

    }
}
