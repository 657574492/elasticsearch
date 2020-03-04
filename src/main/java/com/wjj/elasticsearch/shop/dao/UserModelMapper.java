package com.wjj.elasticsearch.shop.dao;


import com.wjj.elasticsearch.shop.model.UserModel;
import org.apache.ibatis.annotations.Param;

public interface UserModelMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserModel record);

    int insertSelective(UserModel record);

    UserModel selectByPrimaryKey(Integer id);

    UserModel selectByTelphoneAndPassword(@Param("telphone") String telphone, @Param("password") String password);

    int updateByPrimaryKeySelective(UserModel record);

    int updateByPrimaryKey(UserModel record);

    Integer countAllUser();
}