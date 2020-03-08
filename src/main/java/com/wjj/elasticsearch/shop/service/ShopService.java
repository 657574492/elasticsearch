package com.wjj.elasticsearch.shop.service;



import com.wjj.elasticsearch.shop.model.ShopModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public interface ShopService {


    Map<String,Object> searchES(BigDecimal longitude, BigDecimal latitude,
                                String keyword, Integer orderby, Integer categoryId, String tags) throws IOException;

    ShopModel get(Integer id);
}
