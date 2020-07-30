package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.StarDocument;
import com.wjj.elasticsearch.example.util.GsonUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Author: wangjunjie 2019/8/29 11:07
 * @Description: 查询自动补全  效率好于 prefix wildcard
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/29 11:07
 */

@Service
public class QueryCompletionService {

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     *
     * 自动补全 completion
     * POST star_completion/_search
     * {
     *   "size": 0,
     *   "suggest":{
     *     "username-suggester" : {
     *       "prefix":"路人",
     *        "completion": {
     *         "field": "username"，
     *         "size":2
     *         }
     *     }
     *   }
     * }
     * @throws IOException
     */
    public void queryCompletion1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("star_completion");
        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("username-suggester",
                SuggestBuilders.completionSuggestion("username").prefix("路人").size(2));
        sourceBuilder.suggest(suggestBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        CompletionSuggestion suggestion = response.getSuggest().getSuggestion("username-suggester");
        List<CompletionSuggestion.Entry.Option> options = suggestion.getOptions();

        //总数
        System.out.println(options.size());
        for (CompletionSuggestion.Entry.Option option : options) {
            SearchHit hit = option.getHit();
            StarDocument starDocument = GsonUtil.parse(hit.getSourceAsString(), StarDocument.class);
            System.out.println(starDocument.toString());
        }


    }
}
