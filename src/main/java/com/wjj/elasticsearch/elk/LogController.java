package com.wjj.elasticsearch.elk;


import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;


@RequestMapping("/elk")
@RestController
@Slf4j
public class LogController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @GetMapping("/log")
    public String testLogElk(int num) {
        log.info("测试日志elk开始 嘻嘻");
        if (num == 2) {
            log.error("测试错误日志elk 嘻嘻");
            int i=10/0;
        }
        log.info("测试日志elk结束 嘻嘻");
        return "success";
    }


    @GetMapping("/search/log")
    public String searchMessage() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startDateString="2020-04-14 22:00:00";
        String endDateString="2020-04-14 22:04:00";
        Date startDate = sdf.parse(startDateString);
        Date endDate = sdf.parse(endDateString);


        SearchRequest searchRequest = new SearchRequest("blog-demo");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //term 查询条件 不会被分词
        searchSourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").gte(startDate).lte(endDate));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        int count=0;
        long value = hits.getTotalHits().value;
        for (SearchHit hit : hits) {
            System.out.println("------------------------");
            String hitString = hit.getSourceAsString();
            Map<String, Object> map = GsonUtil.parseMap(hitString);
            String date = (String) map.get("@timestamp");
            System.out.println(date);
            //System.out.println(sdf.parse(date));
            System.out.println(map.get("message"));
            ++count;
        }
        System.out.println("--------------");
        System.out.println(count);
        System.out.println(value);
        return "success";
    }


}
