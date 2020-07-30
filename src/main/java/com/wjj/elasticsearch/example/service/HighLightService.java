package com.wjj.elasticsearch.example.service;


import com.alibaba.fastjson.JSONObject;
import com.wjj.elasticsearch.example.domain.index.GoodsDocument;
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
     * 高亮查询 (编号1)
     */
    public void qurey1() throws IOException {
        SearchRequest request = new SearchRequest("shop_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("subTitle","华为麒麟990"));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<b>");
        highlightBuilder.postTags("</b>");
        HighlightBuilder.Field subTitle = new HighlightBuilder.Field("subTitle");

        highlightBuilder.field(subTitle);
        sourceBuilder.highlighter(highlightBuilder);
        request.source(sourceBuilder);
        SearchResponse response = rhlclient.search(request, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            String hitString = hit.getSourceAsString();
            GoodsDocument goodsDocument = GsonUtil.parse(hitString, GoodsDocument.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("subTitle");
            String s = highlightField.fragments()[0].toString();
            goodsDocument.setSubTitle(s);
            System.out.println(GsonUtil.toJSONStringg(goodsDocument));
        }


    }
}
