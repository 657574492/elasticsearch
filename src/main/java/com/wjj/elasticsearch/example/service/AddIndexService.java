package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: wangjunjie 2019/8/23 17:21
 * @Description: 添加相关索引
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/23 17:21
 */

@Service
public class AddIndexService {

    @Autowired
    private RestHighLevelClient rhlClient;
    @Autowired
    private IndexClassService indexClassService;

    public void addIndex() throws IOException {
        StarDocument starDocument = indexClassService.createStarDocument();
        IndexRequest indexRequest = new IndexRequest("star", "_doc", starDocument.getUid());
        String starIndex = GsonUtil.toJSONStringg(starDocument);
        indexRequest.source(starIndex,XContentType.JSON);
        rhlClient.index(indexRequest,RequestOptions.DEFAULT);
    }

    public void addIndex2() throws IOException {
        StarDocument starDocument = indexClassService.createStarDocument();
        IndexRequest indexRequest = new IndexRequest("star2", "_doc", starDocument.getUid());
        String starIndex = GsonUtil.toJSONStringg(starDocument);
        indexRequest.source(starIndex,XContentType.JSON);
        rhlClient.index(indexRequest,RequestOptions.DEFAULT);
    }

    public void addIndex3() throws IOException {
        StarDocument starDocument = indexClassService.createStarDocument();
        IndexRequest indexRequest = new IndexRequest("star_completion", "_doc", starDocument.getUid());
        String starIndex = GsonUtil.toJSONStringg(starDocument);
        indexRequest.source(starIndex,XContentType.JSON);
        rhlClient.index(indexRequest,RequestOptions.DEFAULT);
    }

  /*  public void addBatch() {
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < 100; i++) {
            bulkRequest.add();
        }
        rhlClient.bulkAsync()
    }*/
}
