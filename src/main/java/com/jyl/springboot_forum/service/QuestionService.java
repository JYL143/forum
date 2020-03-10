package com.jyl.springboot_forum.service;

import com.jyl.springboot_forum.mapper.QuestionMapper;
import com.jyl.springboot_forum.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {


    @Autowired
    private QuestionMapper questionMapper;

    //按标签数组 查询对应相关问题
    public List<Question> selectRelated(Question question) {

        String tag1=question.getTag();
        String tag2="";
        String [] str_tag=tag1.split(",");//用这个方法来分割
        for (String tag:str_tag){
             tag2=tag2+tag+"|";
        }
        String tags =tag2.substring(0, tag2.length()-1);      //去除最后一个|

        question.setTag(tags);
        List<Question> questionList=questionMapper.selectRelated(question);
        question.setTag(tag1);  //这里改tag  居然会影响到控制类的tag，所以这里在设置会原trg
       return questionList;
    }
}
