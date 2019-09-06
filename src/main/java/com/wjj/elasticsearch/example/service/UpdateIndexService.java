package com.wjj.elasticsearch.example.service;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wangjunjie 2019/8/29 14:58
 * @Description: 修改文档数据
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/29 14:58
 */

@Service
public class UpdateIndexService {

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     * POST star/_doc/10/_update
     * {
     *   "doc":{
     *     "username": "路人ABD"
     *   }
     * }
     * @throws IOException
     */
    public void updateIndex1() throws IOException {
        Map<String, Object> map = new HashMap<>(3);
        map.put("username","路人A");

        UpdateRequest updateRequest = new UpdateRequest("star", "_doc", "10");
        updateRequest.doc(map);

        rhlClient.update(updateRequest,RequestOptions.DEFAULT);
    }
}
