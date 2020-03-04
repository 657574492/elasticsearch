package com.wjj.elasticsearch.shop.dao;



import com.wjj.elasticsearch.shop.model.CategoryModel;

import java.util.List;

public interface CategoryModelMapper {

    int deleteByPrimaryKey(Integer id);

    List<CategoryModel> selectAll();

    Integer countAllCategory();

    int insert(CategoryModel record);

    int insertSelective(CategoryModel record);

    CategoryModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CategoryModel record);

    int updateByPrimaryKey(CategoryModel record);
}