package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: wangjunjie 2019/8/28 21:55
 * @Description: 查询算分数优化
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/28 21:55
 */

@Service
public class QueryScoreService {

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     *
     * 默认 score算分数 和 age 值 的乘积排序
     * GET star/_search
     * {
     *   "query": {
     *     "function_score": {
     *       "query": {
     *         "match": {
     *           "introduce": "最佳字段匹配"
     *         }
     *       },
     *       "field_value_factor": {
     *         "field": "age"
     *       }
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void queryScore1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //设置算分公式
        FieldValueFactorFunctionBuilder fvffb = ScoreFunctionBuilders
                .fieldValueFactorFunction("age")
                .modifier(FieldValueFactorFunction.Modifier.NONE);
        FunctionScoreQueryBuilder scoreQueryBuilder = new FunctionScoreQueryBuilder(QueryBuilders
                .matchQuery("introduce", "最佳字段匹配"), fvffb);
        sourceBuilder.query(scoreQueryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(" total:"+hits.totalHits);

        for (SearchHit hit : hits) {
            System.out.println(hit.getScore());
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.GsonToBean(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     *
     * 新的分数 = 老的分数 * log（1+ factor*createDate）
     * GET star/_search
     * {
     *   "query": {
     *     "function_score": {
     *       "query": {
     *         "match": {
     *           "introduce": "女神"
     *         }
     *       },
     *       "field_value_factor": {
     *         "field": "createDate",
     *         "modifier": "log1p"
     *         , "factor": 2
     *       }
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void queryScore2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //设置算分公式
        FieldValueFactorFunctionBuilder fvffb = ScoreFunctionBuilders
                .fieldValueFactorFunction("age")
                .modifier(FieldValueFactorFunction.Modifier.LOG1P)
                .factor(2f);
        FunctionScoreQueryBuilder scoreQueryBuilder = new FunctionScoreQueryBuilder(QueryBuilders
                .matchQuery("introduce", "女神"), fvffb);
        sourceBuilder.query(scoreQueryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(" total:"+hits.totalHits);

        for (SearchHit hit : hits) {
            System.out.println(hit.getScore());
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.GsonToBean(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     *    // 总分 = 最佳字段 + 0.3 * 其余字段
     *    GET product/_search
     *    {
     *    "query": {
     *    "multi_match": {
     *    "query": "小米华为",
     *    "fields": ["brandName","categoryName","title^10" ],
     *    "tie_breaker": 0.3
     *    }
     *    }
     *    }
     */
    public void test() throws IOException {
        SearchRequest searchRequest = new SearchRequest("product");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        String[] fields = {"brandName^10","categoryName","title"};
        sourceBuilder.query(QueryBuilders.multiMatchQuery("小米华为",fields));

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        System.out.println("toatal: "+hits.totalHits);
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            Map<String, Object> map = GsonUtil.GsonToMaps(hitString);
            System.out.println(map);
        }

    }

}
