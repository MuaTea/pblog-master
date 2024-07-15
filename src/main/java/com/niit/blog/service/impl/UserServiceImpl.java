package com.niit.blog.service.impl;

import com.niit.blog.entity.domain.User;
import com.niit.blog.mapper.UserMapper;
import com.niit.blog.service.UserService;
import com.niit.blog.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Title: UserServiceImpl.java
 * @Description: 用户登录业务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User login(String userName, String password) {
        /*拿到用户输入的密码使用MD5加密*/
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        /*去数据库查询用户数据*/
        return userMapper.login(userName, passwordMd5);
    }

    /**
     * @Description: 获取当前登录用户的信息，用于修改个人信息和密码的时候使用
     * @param: loginUserId
     * @return: com.niit.blog.entity.domain.User
     **/
    @Override
    public User getUserDetailById(Integer loginUserId) {
        return userMapper.selectByPrimaryKey(loginUserId);
    }

    /**
     * @Description: 修改当前登录用户的密码
     * @param: loginUserId
     * @param: originalPassword
     * @param: newPassword
     * @return: boolean
     **/
    @Override
    public boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        User user = userMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (user != null) {
            /*首先是对密码加密*/
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            /*比较原密码是否正确*/
            if (originalPasswordMd5.equals(user.getPassword())) {
                /*设置新密码并修改*/
                user.setPassword(newPasswordMd5);
                if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                    //修改成功则返回true
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @Description: 修改个人基本信息
     * @param: loginUserId
     * @param: loginUserName
     * @param: nickName
     * @return: boolean
     **/
    @Override
    public boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        User user = userMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (user != null) {
            //修改信息
            user.setUserName(loginUserName);
            user.setNickName(nickName);
            if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }
}
