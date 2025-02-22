package com.niit.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niit.blog.config.ConstConfiguration;
import com.niit.blog.entity.domain.BlogComment;
import com.niit.blog.entity.vo.BlogDetailVO;
import com.niit.blog.service.BlogService;
import com.niit.blog.service.CommentService;
import com.niit.blog.service.TagService;
import com.niit.blog.util.BlogUtils;
import com.niit.blog.util.PageResult;
import com.niit.blog.util.PatternUtil;
import com.niit.blog.util.Result;
import com.niit.blog.util.ResultGenerator;

@Controller
public class BlogClientController {

	@Autowired
    private BlogService blogService;
	
	@Autowired
    private TagService tagService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
    private ConstConfiguration constConfig;
	
	/** 模板路径前缀 */
	public static String theme = "blog/client";
	
	/**
	 * @Description: 访客侧首页加载
	 * @param request
	 * @return String
	 */
	@GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
		request.setAttribute("pageName", "首页");
		return this.page(request, 1);
    }
	
	/**
	 * @Description: 支持分页获取博客列表数据
	 * @param request
	 * @param pageNum 访客想要访问的指定页数
	 * @return String
	 */
	@GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
		/** 获取主页需要显示的博客内容 */
        PageResult blogPageResult = blogService.getBlogsForIndexPage(1);
        if (blogPageResult == null) {
            return "error/error_404";
        }
        /** 将博客列表分页数据封装到 blogPageResult 属性中 */
        request.setAttribute("blogPageResult", blogPageResult);
        
        /** 热门标签：展示前20个热度由高到低排序的标签 */
        request.setAttribute("hotTags", tagService.getHotTagsForIndex());
        
        /** 获取点击数最多的9个博客 */
        request.setAttribute("hotBlogs", blogService.getHotBlogsForIndex());
        return theme + "/index";
    }
	
	/**
	 * @Description: 访客首页搜索博客功能分页功能
	 * @param request
	 * @param keyword
	 * @param page
	 * @return String
	 */
	@GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageBySearch(keyword, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        /** 热门标签：展示前20个热度由高到低排序的标签 */
        request.setAttribute("hotTags", tagService.getHotTagsForIndex());
        /** 获取点击数最多的9个博客 */
        request.setAttribute("hotBlogs", blogService.getHotBlogsForIndex());
        return theme + "/list";
    }
	
	/**
	 * @Description: 查看指定标签下的所有文章
	 * @param request
	 * @param tagName
	 * @param page
	 * @return String
	 */
	@GetMapping({"/tag/{tagName}/{page}"})
    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageByTag(tagName, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "标签");
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", tagName);
        /** 热门标签：展示前20个热度由高到低排序的标签 */
        request.setAttribute("hotTags", tagService.getHotTagsForIndex());
        /** 获取点击数最多的9个博客 */
        request.setAttribute("hotBlogs", blogService.getHotBlogsForIndex());
        return theme + "/list";
    }
	
	/**
	 * @Description: 博客详情页
	 * @param request
	 * @param blogId
	 * @param commentPage
	 * @return String
	 */
	@GetMapping({"/blog/{blogId}", "/article/{blogId}"})
	public String detail(HttpServletRequest request, @PathVariable("blogId") Long blogId, @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
	    BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
	    if (blogDetailVO != null) {
	        request.setAttribute("blogDetailVO", blogDetailVO);
	        
	        /** 在博客详情页加载当前博客的评论内容 */
	        request.setAttribute("commentPageResult", commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
	    }
	    request.setAttribute("pageName", "详情");
	    return theme + "/detail";
	}
	
	@GetMapping("/link")
	public String link(HttpServletRequest request) {
		request.setAttribute("myemail", constConfig.getEmail());
		return theme+"/link";
	}
    
    /**
     * @Description: 访客在文章详情页发表评论
     * @param request
     * @param session
     * @param blogId
     * @param verifyCode
     * @param email
     * @param commentBody
     * @return Result
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                           @RequestParam String email, @RequestParam String commentBody) {
        if (!StringUtils.hasLength(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (!StringUtils.hasLength(kaptchaCode)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        String ref = request.getHeader("Referer");
        if (!StringUtils.hasLength(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!StringUtils.hasLength(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (!StringUtils.hasLength(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setEmail(email);
        comment.setCommentBody(BlogUtils.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(commentService.addComment(comment));
    }
    
}
