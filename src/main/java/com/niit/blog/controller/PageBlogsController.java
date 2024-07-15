package com.niit.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niit.blog.entity.domain.Blog;
import com.niit.blog.entity.domain.BlogComment;
import com.niit.blog.entity.vo.AjaxResult;
import com.niit.blog.mapper.BlogCommentMapper;
import com.niit.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class PageBlogsController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogCommentMapper commentMapper;

    @GetMapping("/blogs/list")
    public AjaxResult blogPage(@RequestParam(required = false) String blogTitle, Integer pageNum, Integer pageSize) {
        PageInfo<Blog> listBlogs = blogService.listBlogs(blogTitle, pageNum, pageSize);
        return AjaxResult.success(200, "success", listBlogs);
    }

    @PostMapping("/blogs/update")
    public AjaxResult updateBlog(@RequestBody Blog blog) {
        String updateBlog = blogService.updateBlog(blog);
        return AjaxResult.success(200, "success", updateBlog);
    }

    @DeleteMapping("/blogs/{blogId}")
    public AjaxResult deupdateBlog(@PathVariable Integer blogId) {
        Integer[] ids = {blogId};
        return blogService.deleteBatch(ids) ? AjaxResult.success(200, "删除成功", null) : AjaxResult.error(500, "删除失败");
    }

    @GetMapping("/comments/list")
    public AjaxResult getComments(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) String searchText
    ) {
        PageHelper.startPage(page,size);
        List<BlogComment> list = commentMapper.queryBlogCommentList(searchText);
        PageInfo<BlogComment> pageInfo = new PageInfo<>(list);
        return AjaxResult.success(200, "success",pageInfo );
    }
}
