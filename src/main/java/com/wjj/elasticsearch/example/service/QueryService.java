package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * @Author: wangjunjie 2019/8/24 14:31
 * @Description: 查询相关
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/24 14:31
 */

@Service
public class QueryService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 普通查询 term
     *
     * GET star_document/_search
     * {
     *   "query": {
     *     "term": {
     *       "username": {
     *         "value": "李明"
     *       }
     *     }
     *   }
     * }
     */
    public void query1() throws IOException {

        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //term 查询条件 不会被分词
        searchSourceBuilder.query(QueryBuilders.termQuery("username","李明"));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     *
     * 普通查询 terms  条件只要一个配置就满足
     *POST star/_search
     * {
     *   "query": {
     *     "terms": {
     *       "username": [
     *         "李梅",
     *         "小明"
     *       ]
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void query11() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        String[] values = {"李梅", "小明", "王二"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("username",values));

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println("total: "+hits.getTotalHits().value);
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     *
     * 如何mapping 设置为
     * "content":{
     *           "type":"text",
     *           "analyzer":"ik_smart",
     *           "fields":{
     *             "keyword":{
     *               "type":"keyword",
     *               "ignore_above":100
     *             }
     *           }
     *         }
     * type类型为text 也可以用term 类型查询
     *
     * POST test/_search
     * {
     *
     *   "query": {
     *     "term": {
     *       "content.keyword": "小姐姐的味道"
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void query10() throws IOException {

        SearchRequest searchRequest = new SearchRequest("test2");
        searchRequest.types("_doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //term 查询条件 不会被分词
        searchSourceBuilder.query(QueryBuilders.termQuery("content.keyword","小姐姐的味道"));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            System.out.println(hitString);
        }
    }

    /**
     *
     *
     * match分词查询，查询条件默认采用mapping设置的该字段的分词器 ，也可以手动设置分词器
     *
     * GET star_document/_search
     * {
     *   "query": {
     *     "match": {
     *       "introduce": {
     *         "query": "小姐姐的味道"
     *          // "analyzer": "ik_smart"
     *       }
     *     }
     *   }
     * }
     * @throws IOException
     *
     */
    public void query2() throws IOException {

        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("introduce","小姐姐的味道").analyzer("standard"));
        //手动设置分词器
        //searchSourceBuilder.query(QueryBuilders.matchQuery("introduce","女神高圆圆测试").analyzer("standard"));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     *
     *
     * minimumShouldMatch 查询参数匹配情况  2:查询参数分词后，要有2个词与字段匹配
     * 20%:查询参数分词后，要有20%个词与字段匹配
     *
     * GET star_document/_search
     * {
     *   "query": {
     *     "match": {
     *       "introduce": {
     *         "query": "小姐姐的味道",
     *         "minimum_should_match": "50%"
     *       }
     *     }
     *   }
     * }
     * @throws IOException
     *
     */
    public void query3() throws IOException {

        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("introduce","小姐姐的味道")
                .minimumShouldMatch("50%"));
        //手动设置分词器
        //searchSourceBuilder.query(QueryBuilders.matchQuery("introduce","女神高圆圆测试").analyzer("standard"));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }
    /**
     *
     * 返回指定字段
     * GET star_document/_search
     * {
     *   "_source": ["uid","type"],
     *   "query": {
     *     "term": {
     *       "username": "王二"
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void query4() throws IOException {

        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.termQuery("username","王二"));
        searchSourceBuilder.fetchSource(new String[]{"uid","type"},null);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }


    /**
     *
     * filter 不计算相关度，效率更高
     * GET star_document/_search
     * {
     *   "query": {
     *     "bool": {
     *       "filter": {
     *         "match": {
     *           "introduce": "不计算相关度"
     *         }
     *       }
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void query5() throws IOException {

        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(QueryBuilders.matchQuery("introduce","不计算相关度"));
        searchSourceBuilder.query(boolQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
        }
    }

    /**
     *
     * filter 不计算相关度，效率更高
     * GET star_document/_search
     * {
     *   "query": {
     *     "bool": {
     *       "filter": [
     *         {
     *           "term": {
     *             "sex": {
     *               "value": 0
     *             }
     *           }
     *         },
     *         {
     *           "match": {
     *             "introduce": "不计算相关度"
     *           }
     *         }
     *       ]
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void query6() throws IOException {

        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(QueryBuilders.termQuery("sex",0));
        boolQueryBuilder.filter(QueryBuilders.matchQuery("introduce","不计算相关度"));
        searchSourceBuilder.query(boolQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());

        }
    }

    /**
     *
     * wildcard 通配符查询
     * POST star/_search
     * {
     *   "query": {
     *     "wildcard": {
     *       "username": {
     *         "value": "高圆*"
     *       }
     *     }
     *   }
     * }
     */
    public void wildcardQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.wildcardQuery("username","高圆*"));
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());

        }
    }

    /**
     *
     * prefix 指定前缀查询 效率高于 wildcard
     * POST star/_search
     * {
     *   "query": {
     *     "prefix": {
     *       "username": {
     *         "value": "高"
     *       }
     *     }
     *   }
     * }
     */
    public void prefixQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.prefixQuery("username","高圆"));
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.parse(hitString, StarDocument.class);
            System.out.println(starDocument.toString());

        }
    }
}
