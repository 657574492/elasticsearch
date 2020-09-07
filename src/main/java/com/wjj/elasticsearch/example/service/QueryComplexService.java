package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: wangjunjie 2019/8/28 13:38
 * @Description: 复杂查询
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/28 13:38
 */

@Service
public class QueryComplexService {

    @Autowired
    private RestHighLevelClient rhlClient;


    /**
     *
     * boost 指定match查询得分权重
     *
     * GET star/_search
     * {
     *   "query": {
     *     "bool": {
     *       "should": [
     *         {
     *           "match": {
     *             "content": {
     *               "query": "测试1",
     *                "boost":1
     *             }
     *
     *           }
     *         },
     *         {
     *           "match": {
     *             "introduce": {
     *               "query": "测试2",
     *               "boost":2
     *             }
     *           }
     *         }
     *       ]
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void queryComplex1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(QueryBuilders.matchQuery("content","测试1").boost(1));
        boolQueryBuilder.should(QueryBuilders.matchQuery("introduce","测试2").boost(2));
        sourceBuilder.query(boolQueryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        //System.out.println("toatal: "+hits.getTotalHits().value);
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }

    }

    /**
     * 多字段匹配查询
     * 获取最佳匹配字段，并排在前面
     *
     * tie_breaker 0 最佳字段匹配 1 所有语句同等重要
     *
     * GET star/_search
     * {
     *   "query": {
     *     "dis_max": {
     *       "tie_breaker": 0.1,
     *       "queries": [
     *         {
     *           "match": {
     *             "introduce": "最佳字段匹配"
     *           }
     *         },
     *         {
     *           "match": {
     *             "content": "这是个测试h哈哈a不知道a"
     *           }
     *         }
     *       ]
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void queryComplex2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        DisMaxQueryBuilder disMaxQueryBuilder = new DisMaxQueryBuilder();
        disMaxQueryBuilder.tieBreaker(0.1f);
        disMaxQueryBuilder.add(QueryBuilders.matchQuery("content","这是个测试h哈哈a不知道a"));
        disMaxQueryBuilder.add(QueryBuilders.matchQuery("introduce","最佳字段匹配"));
        sourceBuilder.query(disMaxQueryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        //System.out.println("toatal: "+hits.getTotalHits().value);
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     * 单字段多字符串查询
     * best_fields 最佳字段，某个字段相关度最高，就排在前边
     *
     * GET star/_search
     * {
     *   "query": {
     *     "multi_match": {
     *       "query": "路人a的测试",
     *       "type": "best_fields",
     *       "fields": ["introduce","content"]
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void queryComplex3() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        String[] fields = {"introduce", "content"};
        sourceBuilder.query(QueryBuilders.multiMatchQuery("路人a的测试",fields).
                type(MultiMatchQueryBuilder.Type.BEST_FIELDS));

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        //System.out.println("toatal: "+hits.getTotalHits().value);
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     * 单字段多字符串查询
     * cross_fields 以分词为单位计算栏位的总分，适用于词导向的匹配
     *
     * GET star/_search
     * {
     *   "query": {
     *     "multi_match": {
     *       "query": "测试a",
     *       "fields": ["introduce","content"],
     *       "type": "cross_fields"
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void queryComplex4() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        String[] fields = {"introduce", "content"};
        sourceBuilder.query(QueryBuilders.multiMatchQuery("测试a",fields).
                type(MultiMatchQueryBuilder.Type. CROSS_FIELDS));

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        //System.out.println("toatal: "+hits.getTotalHits().value);
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }
}
