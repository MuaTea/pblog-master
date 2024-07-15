package com.niit.blog.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.niit.blog.entity.domain.Blog;
import com.niit.blog.entity.vo.BlogDetailVO;
import com.niit.blog.entity.vo.SimpleBlogListVO;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;

/**
 * @Title: BlogService.java
 * @Description: 博客管理
 */
public interface BlogService {
    /**
     * @Description: 查询博客总数量
     * @return int
     */
    int getTotalBlogs();

    /**
     * @Description: 查询博客列表
     * @param: pageUtil
     * @return: com.niit.blog.util.PageResult
     **/
    PageResult getBlogsPage(PageQueryUtil pageUtil);

    /**
     * @Description: 博客新增
     * @param: blog
     * @return: java.lang.String
     **/
    String saveBlog(Blog blog);
    
    /**
     * @Description: 根据id获取博客详情
     * @param blogId
     * @return Blog
     */
    Blog getBlogById(Long blogId);
    
    /**
     * @Description: 更新博客
     * @param blog
     * @return String
     */
    String updateBlog(Blog blog);
    
    /**
     * @Description: 根据博客ID批量删除博客
     * @param ids
     * @return Boolean
     */
    Boolean deleteBatch(Integer[] ids);
    
    /**
     * @Description: 获取首页文章列表
     * @param page 页数
     * @return PageResult
     */
    PageResult getBlogsForIndexPage(int page);
    
    /**
     * @Description: 获取点击数最多的9个博客
     * @param type
     * @return List<SimpleBlogListVO>
     */
    List<SimpleBlogListVO> getHotBlogsForIndex();
    
    /**
     * @Description: 根据关键字搜索博客
     * @param keyword
     * @param page
     * @return PageResult
     */
    PageResult getBlogsPageBySearch(String keyword, int page);
    
    /**
     * @Description: 根据博客ID获取博客详情
     * @param blogId
     * @return BlogDetailVO
     */
    BlogDetailVO getBlogDetail(Long blogId);
    
    /**
     * @Description: 根据标签获取文章列表
     * @param tagName
     * @param page
     * @return PageResult
     */
    PageResult getBlogsPageByTag(String tagName, int page);

    PageInfo<Blog> listBlogs(String blogTitle, Integer pageNum, Integer pageSize);
}
