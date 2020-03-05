package com.jyl.springboot_forum.mapper;

import com.jyl.springboot_forum.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    //点击登录，登录成功添加user信息
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
     void insert(User user); //默认public不用加

    //查询token
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}
