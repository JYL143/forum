package com.jyl.springboot_forum.config;




import com.jyl.springboot_forum.interceptor.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 扩展Springmvc，编写一个配置类然后继承WebMvcConfigurerAdapter，不能标EnableWebMvc注解,标了的话就会全面接管springmvc
 * ctrl o可以看要重写的方法
 */
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {  //继承springmvc扩展类，

    @Autowired
    private LoginHandlerInterceptor loginHandlerInterceptor;

    /**
     * 添加拦截器组件
     * springboot2.0版本以上会拦截静态资源映射
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginHandlerInterceptor).addPathPatterns("/**");
    }

//    /**
//     * 添加视图映射方法
//     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("index").setViewName("/");
//
//    }



}
