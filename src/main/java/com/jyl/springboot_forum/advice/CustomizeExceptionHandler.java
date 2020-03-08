package com.jyl.springboot_forum.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


/**
 * 全局异常处理
 */
@ControllerAdvice //3个功能全局异常处理 全局数据绑定      全局数据预处理
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class) //指明异常的处理类型  ，也就是说可以写不同的异常处理方法，这里我们写全部异常
    public ModelAndView customException(Exception e, Model model) {
        ModelAndView mv = new ModelAndView();

        model.addAttribute("message",e.getMessage());
        mv.setViewName("myerror");
        return mv;
    }
}

