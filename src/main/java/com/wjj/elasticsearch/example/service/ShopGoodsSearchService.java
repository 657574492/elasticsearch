package com.wjj.elasticsearch.example.service;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangjunjie
 * @date 2020/5/18
 * @description TODO
 */

@Service
public class ShopGoodsSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void query1(String content) {
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

    }
}
