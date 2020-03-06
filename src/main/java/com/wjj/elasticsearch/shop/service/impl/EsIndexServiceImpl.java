package com.wjj.elasticsearch.shop.service.impl;

import com.wjj.elasticsearch.example.util.GsonUtil;
import com.wjj.elasticsearch.shop.dao.ShopModelMapper;
import com.wjj.elasticsearch.shop.service.EsIndexService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class EsIndexServiceImpl implements EsIndexService {

    @Autowired
    private RestHighLevelClient rhlClient;
    @Autowired
    private ShopModelMapper shopModelMapper;

    @Override
    public void createShopIndex() throws IOException {
        List<Map<String, Object>> list = shopModelMapper.buildESQuery(null, null, null);
        for (Map<String, Object> map : list) {
            IndexRequest indexRequest = new IndexRequest("shop", "_doc", String.valueOf((int) map.get("id")));
            String shopIndex = GsonUtil.GsonString(map);
            indexRequest.source(shopIndex, XContentType.JSON);
            rhlClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }
}
