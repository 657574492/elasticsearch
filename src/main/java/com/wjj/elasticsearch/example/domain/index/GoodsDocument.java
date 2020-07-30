package com.wjj.elasticsearch.example.domain.index;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: wangjunjie 2020/5/10 21:37
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2020/5/10 21:37
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDocument {

    private String productSn;

    private Long productId;

    private Long brandId;

    private String brandName;

    private Long categoryId;

    private String categoryName;

    private Integer commentCount;

    private Integer hot;

    private Integer price;

    private Long publishDate;

    private Integer saleCount;

    private String subTitle;

    private String title;

    private List<ProductSku> sku;
}
