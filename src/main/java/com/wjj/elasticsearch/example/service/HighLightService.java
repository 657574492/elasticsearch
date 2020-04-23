package com.wjj.elasticsearch.example.service;


import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * 高亮查询
 */

@Service
public class HighLightService {

    @Autowired
    private RestHighLevelClient rhlclient;

    /**
     *
     *
     * 高亮查询
     *
     */
    public void qurey1() throws IOException {
        SearchRequest request = new SearchRequest("shop");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(QueryBuilders.matchQuery("name","酒店"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("category_name","酒店"));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field nameField = new HighlightBuilder.Field("name");
        HighlightBuilder.Field categoryNameField = new HighlightBuilder.Field("category_name");

        highlightBuilder.field(nameField);
        highlightBuilder.field(categoryNameField);

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.highlighter(highlightBuilder);
        request.source(sourceBuilder);
        SearchResponse response = rhlclient.search(request, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        //System.out.println(" total:"+hits.getTotalHits().value);

        for (SearchHit hit : hits) {
            System.out.println("----------------");
            String hitString = hit.getSourceAsString();
            Map<String, Object> map = GsonUtil.GsonToMaps(hitString);
            System.out.println(map.toString());
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (Map.Entry<String, HighlightField> fieldEntry : highlightFields.entrySet()) {
                System.out.println("field: "+ fieldEntry.getKey()+" value:"+fieldEntry.getValue());
            }
        }
    }
}
