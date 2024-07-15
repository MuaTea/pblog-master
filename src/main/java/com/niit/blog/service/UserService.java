package com.niit.blog.service;

import com.niit.blog.entity.domain.User;

/**
 * @Title: UserService.java
 * @Description: 用户登录业务接口
 */
public interface UserService {
    User login(String userName, String password);

    /*获取当前登录用户的信息，用于修改个人信息和密码的时候使用*/
    User getUserDetailById(Integer loginUserId);

    /*修改当前登录用户的密码*/
    boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);

    /*修改个人基本信息*/
    boolean updateName(Integer loginUserId, String loginUserName, String nickName);
}
