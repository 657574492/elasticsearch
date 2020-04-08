package com.wjj.elasticsearch.shop.dao;


import com.wjj.elasticsearch.shop.model.IkWordModel;
import com.wjj.elasticsearch.shop.model.ShopModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IkWordModelMapper {


    List<IkWordModel> listIkWord();

    List<IkWordModel> listIkWordByTime(@Param("time")Long time);
}