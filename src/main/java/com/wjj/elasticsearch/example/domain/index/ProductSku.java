package com.wjj.elasticsearch.example.domain.index;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author EDZ
 * @date 2020/5/14
 * @description TODO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSku {

    private Integer price;

    private String skuSn;

    private Long skuId;

    private List<SkuParam> skuData;
}
