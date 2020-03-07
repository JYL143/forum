package com.jyl.springboot_forum.mapper;

import com.jyl.springboot_forum.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    //点击登录，登录成功添加user信息
     void insert(User user); //默认public不用加

    //查询token
    User findByToken(@Param("token") String token);

    //查询question里的关联id字段与user表的id相同的user信息
    User findByid(@Param("id")Integer id);  //传进来的id是question表的caret关联字段

    //根据accountid看是否存在这个用户
    User findByAccountId(String accountId);

    //用户存在就更新
    void update(User dbuser);
}
