package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.util.GsonUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: wangjunjie 2020/3/8 16:30
 * @Description: 地理坐标查询
 * @Version: 1.0.0
 * @Modified By: xxx 2020/3/8 16:30
 */

@Service
public class QueryGeoService {

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     *
     *
     * 查询 距坐标10km的数据
     * GET shop/_search
     * {
     *   "query": {
     *      "bool" : {
     *         "must" : [{
     *             "term":{
     *               "category_id":2
     *             }},
     *             {"match":{
     *               "name": "凯悦"
     *             }}
     *         ],
     *         "filter" : {
     *             "geo_distance" : {
     *                 "distance" : "10km",    // 10km 范围了
     *                 "location" : "31.2391,121.4878"   //location mapping 坐标字段
     *             }
     *         }
     *     }
     *   }
     *
     * }
     */
    public void geoQuery1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name","凯悦"));
        boolQueryBuilder.must(QueryBuilders.termQuery("category_id",2));

        boolQueryBuilder.filter(QueryBuilders.
                geoDistanceQuery("location").
                distance(10,DistanceUnit.KILOMETERS).point(31.2391,121.4878));

        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            Map<String, Object> map = GsonUtil.parseMap(hitString);
            System.out.println(map);
        }

    }

}
