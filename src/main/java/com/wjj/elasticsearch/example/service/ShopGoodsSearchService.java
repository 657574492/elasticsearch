package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.util.GsonUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author wangjunjie
 * @date 2020/5/18
 * @description TODO
 */

@Service
public class ShopGoodsSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void query1(String content) throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop_goods");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(QueryBuilders.matchQuery("title",content));
        boolQueryBuilder.should(QueryBuilders.matchQuery("subTitle",content));
        boolQueryBuilder.should(QueryBuilders.termQuery("brandName",content));
        boolQueryBuilder.should(QueryBuilders.termQuery("categoryName",content));

        NestedQueryBuilder skuPriceQuery = QueryBuilders.nestedQuery("sku",
                QueryBuilders.rangeQuery("sku.price").gte(300000).lte(800000), ScoreMode.None);
        boolQueryBuilder.filter(skuPriceQuery);

        BoolQueryBuilder nestBoolQueryBuilder = new BoolQueryBuilder();
        nestBoolQueryBuilder.filter(QueryBuilders.termQuery("sku.skuData.attribute","处理器"));
        nestBoolQueryBuilder.filter(QueryBuilders.termQuery("sku.skuData.value","骁龙865"));
        NestedQueryBuilder skuSkuDataQuery = QueryBuilders.nestedQuery("sku.skuData", nestBoolQueryBuilder, ScoreMode.None);
        boolQueryBuilder.filter(skuSkuDataQuery);

        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            String result = hit.getSourceAsString();
            System.out.println(GsonUtil.GsonToMaps(result));
        }

    }
}
