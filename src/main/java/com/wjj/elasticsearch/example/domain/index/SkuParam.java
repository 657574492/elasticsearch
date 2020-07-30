package com.wjj.elasticsearch.example.domain.index;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wangjunjie 2020/5/10 21:45
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2020/5/10 21:45
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuParam {

    private String attribute;

    private String value;

}
