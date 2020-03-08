package com.jyl.springboot_forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.jyl.springboot_forum.mapper")
@SpringBootApplication
public class SpringbootForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootForumApplication.class, args);
    }

}
