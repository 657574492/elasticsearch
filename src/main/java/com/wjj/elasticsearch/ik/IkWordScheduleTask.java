package com.wjj.elasticsearch.ik;

import com.wjj.elasticsearch.shop.dao.IkWordModelMapper;
import com.wjj.elasticsearch.shop.model.IkWordModel;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;

/**
 * @author wangjunjie
 * @date 2020/4/8
 * @description ik 查询并重构索引
 */

@Configuration
@EnableScheduling
public class IkWordScheduleTask {

    @Autowired
    private RestHighLevelClient rhlClient;
    @Autowired
    private IkWordModelMapper ikWordModelMapper;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void queryUpdateIkWord() throws IOException {

        //查询20分钟之内新添加的热词
        //long preTime = System.currentTimeMillis() - 20 * 60 * 1000;
        List<IkWordModel> wordModelList = ikWordModelMapper.listIkWordByTime(0L);
        if (wordModelList == null || wordModelList.size() == 0) {
            return;
        }
        for (IkWordModel ikWordModel : wordModelList) {
            String wordString = ikWordModel.getWord();
            if (StringUtils.isBlank(wordString)) {
                continue;
            }

            UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest("shop");
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

            for (int i = 0; i < wordString.length(); ++i) {
                String word = String.valueOf(wordString.charAt(i));
                boolQueryBuilder.must(QueryBuilders.termQuery("name", word));
            }
            updateByQueryRequest.setQuery(boolQueryBuilder);
            rhlClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);

        }


    }
}
