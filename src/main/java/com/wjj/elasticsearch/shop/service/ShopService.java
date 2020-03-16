package com.wjj.elasticsearch.shop.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ShopService {

    Map<String, Object> searchES(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags);
}
