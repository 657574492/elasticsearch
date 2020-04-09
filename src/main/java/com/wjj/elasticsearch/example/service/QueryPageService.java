package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangjunjie 2019/8/24 15:40
 * @Description: search after 深度分页查询
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/24 15:40
 */

@Service
public class QueryPageService {

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     * 普通分页查询
     * GET star_document/_search
     * {
     *   "size":2,     //查询数量
     *   "from": 4,    //其实位置
     *   "sort": [
     *     {
     *       "createDate": {
     *         "order": "desc"
     *       }
     *     },
     *     {
     *       "_id":{
     *         "order": "desc"
     *       }
     *     }
     *   ],
     *   "query": {
     *     "term": {
     *       "sex": {
     *         "value": 0
     *       }
     *     }
     *   }
     * }
     */


    /**
     * search after
     *
     * GET star_document/_search
     * {
     *   "size":2,
     *   "search_after":["1566628077159",5],
     *   "sort": [
     *     {
     *       "createDate": {
     *         "order": "desc"
     *       }
     *     },
     *     {
     *       "_id":{
     *         "order": "desc"
     *       }
     *     }
     *   ],
     *   "query": {
     *     "term": {
     *       "sex": {
     *         "value": 0
     *       }
     *     }
     *   }
     * }
     * @param values
     *
     *
     * @return
     * @throws IOException
     */
    public Map<String,Object> queryPage1(Object[] values) throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_document");
        searchRequest.types("_doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.sort("createDate",SortOrder.DESC);
        sourceBuilder.sort("_id",SortOrder.DESC);
        sourceBuilder.size(2);

        // 如果第一页，则 values 为空
        // 2 为 sourceBuilder.sort设置次数
        if (values!=null && values.length==2) {
            sourceBuilder.searchAfter(values);
        }
        sourceBuilder.query(QueryBuilders.termQuery("sex",0));

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        //System.out.println(".....total: "+hits.totalHits);
        Map<String, Object> searchResult = new HashMap<>();
        if (hits.getHits()==null ||hits.getHits().length==0) {
            return null;
        }

        List<StarDocument> data = new ArrayList<>();
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            StarDocument starDocument = GsonUtil.GsonToBean(hitString, StarDocument.class);
            System.out.println(starDocument.toString());
            data.add(starDocument);
        }

        //获取每页最后一个数据的排序值 sortValues
        int index = hits.getHits().length-1;
        Object[] sortValues = hits.getHits()[index].getSortValues();

        System.out.println(sortValues[0]+":"+sortValues[1]);
        searchResult.put("data",data);
        searchResult.put("sortValues",sortValues);
        return searchResult;

    }
}
