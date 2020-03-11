package com.jyl.springboot_forum.controller;


import com.jyl.springboot_forum.cache.TagCache;
import com.jyl.springboot_forum.mapper.QuestionMapper;

import com.jyl.springboot_forum.model.Question;
import com.jyl.springboot_forum.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * 发布文章控制类
 * restful风格， 比如publish方法，get请求就渲染页面，post请求就执行请求
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    //编辑问题方法
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        Question question = questionMapper.getById(id);  //查询原来的问题信息
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());  //查出id然后放到隐藏域里 然后点击发布问题就根据有没有id来做添加问题还是更新问题
        model.addAttribute("tags", TagCache.get());  //将标签库数据放进去

        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());  //将标签库数据放进去
        return "publish";
    }


    //发布问题
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
            @RequestParam("id")Integer id, //根据有没有id来做添加问题还是更新问题
            HttpServletRequest request,
            Model model){

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());  //将标签库数据放进去

        if (title ==null || title =="") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description ==null || description =="") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag ==null || tag =="") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        String invalid = TagCache.filterInvalid(tag);  //传入标签，检测是否输入非法标签
        if (StringUtils.isNotBlank(invalid)) { //如果不为空，说明里面存在识别不了的标签
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            model.addAttribute("error","用户未登录");
            return  "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId()); //关联user表的id值，多表查询用
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        if (id==null){  //添加操作
            questionMapper.create(question);
        }else {  //更新操作
            question.setId(id);
            questionMapper.update(question);
        }

        return "redirect:/";



    }

}
