package com.jyl.springboot_forum.dto;

import lombok.Data;

import java.util.List;

/**
 * 标签库
 */
@Data
public class TagDTO {
    private String categoryName;  //标签分类名
    private List<String> tags;    //标签集合
}
