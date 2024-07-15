package com.niit.blog.mapper;

import com.niit.blog.entity.domain.User;
import org.apache.ibatis.annotations.Param;


public interface UserMapper {
    User login(@Param("userName") String userName, @Param("password") String passwordMd5);

    /**
     * @Description:获取当前登录用户的信息，用于修改个人信息和密码的时候使用
     * @param loginUserId
     * @return
     */
    User selectByPrimaryKey(Integer loginUserId);

    /**
     * @Description: 修改密码和修改个人基本信息
     * @param user
     * @return
     */
    int updateByPrimaryKeySelective(User user);
}
