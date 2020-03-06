package com.wjj.elasticsearch.shop.index;


import com.wjj.elasticsearch.shop.model.CategoryModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * shop 索引
 */
public class ShopIndex {

    private Integer id;

    private String name;

    private BigDecimal remarkScore;

    private Integer pricePerMan;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Integer distance;

    private Integer categoryId;

    private CategoryModel categoryModel;

    private String tags;
}
