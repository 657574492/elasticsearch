package com.wjj.elasticsearch.shop.service.impl;

import com.wjj.elasticsearch.shop.service.ShopService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RestHighLevelClient rhlClient;

    @Override
    public Map<String, Object> searchES(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags) {
        SearchRequest searchRequest = new SearchRequest("shop");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();



        return null;
    }
}
