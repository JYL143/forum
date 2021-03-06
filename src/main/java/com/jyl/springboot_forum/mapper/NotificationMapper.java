package com.jyl.springboot_forum.mapper;


import com.jyl.springboot_forum.model.Notification;

import java.util.List;

public interface NotificationMapper {

    //添加通知
    void insert(Notification notification);

    //查询该用户有几个未读的回复
    Long getnumber(Integer id);

    //查询这个用户的最新回复,按时间最慢排序
    List<Notification> list2(Integer id);

    //根据主键id查询他的outerid
    Long getouterid(Integer id);

    //将未读改成已读
    void updateStatus(Integer id);
}