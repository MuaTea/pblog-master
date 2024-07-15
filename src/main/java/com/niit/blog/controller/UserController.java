package com.niit.blog.controller;

import com.niit.blog.entity.domain.Blog;
import com.niit.blog.entity.domain.User;
import com.niit.blog.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户控制器
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;

    /**
     * @param request
     * @return String
     * @Description: 管理员首页
     * @Author: NIIT
     * @CreateTime: 2022-06-02 10:13:26
     */
    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");

        /** 总文章数 */
        request.setAttribute("blogCount", blogService.getTotalBlogs());

        /** 总评论数 */
        request.setAttribute("commentCount", commentService.getTotalComments());

        /** 分类数量 */
        request.setAttribute("categoryCount", categoryService.getTotalCategories());

        /** 总标签数 */
        request.setAttribute("tagCount", tagService.getTotalTags());

        /** 友情链接 */
        request.setAttribute("linkCount", linkService.getTotalLinks());

        return "user/index";
    }

    /* 管理员登录页面 */
    @GetMapping({"/login"})
    public String login() {
        return "user/login";
    }

    /**
     * 执行登录处理
     *
     * @param userName
     * @param password
     * @param verifyCode
     * @param session
     * @return
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode, HttpSession session) {
        if (!StringUtils.hasText(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "user/login";
        }
        if (!StringUtils.hasText(userName) || !StringUtils.hasText(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "user/login";
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (!StringUtils.hasText(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            return "user/login";
        }
        User user = userService.login(userName, password);
        if (user != null) {
            session.setAttribute("nickName", user.getNickName());
            session.setAttribute("userId", user.getUserId());
            // session过期时间设置为7200秒 即两小时
            // session.setMaxInactiveInterval(60 * 60 * 2);
            return "redirect:/user/index";
        } else {
            session.setAttribute("errorMsg", "登陆失败,账号/密码错误！");
            return "user/login";
        }
    }

    /**
     * @Description: 退出登录
     * @Author: NIIT
     * @CreateTime: 2022-5-31 10:58
     * @param: request
     * @return: java.lang.String
     **/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        /* 退出登录之后我们需要清除Session中的用户信息 */
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "user/login";
    }

    /**
     * @Description: 基本信息的修改和修改密码
     * @Author: NIIT
     * @CreateTime: 2022-5-31 11:19
     * @param: request
     * @return: java.lang.String
     **/
    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        /* 获取当前登录信息 */
        Integer loginUserId = (int) request.getSession().getAttribute("userId");
        User user = userService.getUserDetailById(loginUserId);
        /* 如果为空，则返回登录页面 */
        if (user == null) {
            return "user/login";
        }
        request.setAttribute("path", "profile");
        /* 如果不为空，则获取当前登录用户的姓名和昵称 */
        request.setAttribute("userName", user.getUserName());
        request.setAttribute("nickName", user.getNickName());
        return "user/profile";
    }

    /**
     * @Description: 修改密码
     * @Author: NIIT
     * @CreateTime: 2022-5-31 14:43
     * @param: request
     * @param: originalPassword
     * @param: newPassword
     * @return: java.lang.String
     **/
    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        /* 判断参数（旧密码和新密码）是否为空 */
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        /* 判断如果参数不为空，则是从数据库拿取用户的信息 */
        Integer loginUserId = (int) request.getSession().getAttribute("userId");
        if (userService.updatePassword(loginUserId, originalPassword, newPassword)) {
            /* 修改成功后清空session中的数据，前端控制跳转至登录页 */
            request.getSession().removeAttribute("userId");
            request.getSession().removeAttribute("user");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        } else {
            return "修改失败";
        }
    }

    /**
     * @Description: 修改个人基本信息
     * @Author: NIIT
     * @CreateTime: 2022-5-31 11:20
     * @param: request
     * @param: loginUserName
     * @param: nickName
     * @return: java.lang.String
     **/
    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, @RequestParam("userName") String userName,
                             @RequestParam("nickName") String nickName) {
        /* 判断参数是否为空 */
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        /* 获取当前登录用户的id，然后根据id查询信息并修改 */
        Integer loginUserId = (int) request.getSession().getAttribute("userId");
        if (userService.updateName(loginUserId, userName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    /**
     * 进入编写博客内容页面
     */
    @GetMapping("/blogs/edit")
    public String publishPageContent(Model model) {
        model.addAttribute("blog", new Blog());
        return "user/publish";
    }

    /**
     * 最终的发布
     */
    @ResponseBody
    @PostMapping("/blog/publish")
    public String finalPublishPageContent(
            @RequestParam String blogTitle,
            @RequestParam String blogContent,
            @RequestParam String blogTags,
            @RequestParam String blogCover,
            @RequestParam String blogCategoryName
    ) {
        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl("");
        // 图片
        blog.setBlogCoverImage("http://localhost:8090/"+blogCover);
        blog.setBlogStatus(new Byte("1"));
        blog.setBlogContent(blogContent);
        blog.setBlogTags(blogTags);
        String[] split = blogCategoryName.split(",");
        blog.setBlogCategoryName(split[0]);
        blog.setBlogCategoryId(Integer.valueOf(split[1]));
        blogService.saveBlog(blog);
        return "发布成功";
    }

    @GetMapping("/tags")
    public String getAllTags() {
        return "user/tags";
    }

    @GetMapping("/blogs")
    public String toBolgsPage() {
        return "user/blogs";
    }

    @GetMapping("/comments")
    public String toCommentPage() {
        return "user/comments";
    }
}
