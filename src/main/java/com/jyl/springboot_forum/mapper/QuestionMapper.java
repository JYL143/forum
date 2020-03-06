package com.jyl.springboot_forum.mapper;


import com.jyl.springboot_forum.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    //发布文章
    void create(Question question);

    //查询文章列表
    List<Question> list();


}