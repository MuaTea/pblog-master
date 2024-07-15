package com.niit.blog.service;

import com.niit.blog.entity.domain.BlogComment;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;

/**
 * @Title: CommentService.java
 * @Description: 评论管理
 */
public interface CommentService {
	
	/**
	 * @Description: 获取总的评论数，在后台管理首页显示
	 * @return Object
	 */
	int getTotalComments();
    
    /**
     * @Description: 访客发表评论
     * @param blogComment
     * @return Boolean
     */
    Boolean addComment(BlogComment blogComment);
    
    /**
     * @Description: 根据博客ID获取该博客的所有评论信息
     * @param blogId
     * @param page
     * @return PageResult
     */
    PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page);

    /**
     * @Description: 评论列表
     * @param pageUtil
     * @return PageResult
     */
    PageResult getCommentsPage(PageQueryUtil pageUtil);
    
    /**
     * @Description: 回复评论
     * @param commentId
     * @param replyBody
     * @return Boolean
     */
    Boolean reply(Long commentId, String replyBody);
    
    /**
     * @Description: 批量禁用
     * @param ids
     * @return Boolean
     */
    Boolean checkDone(Integer[] ids);
    
    /**
     * @Description: 批量删除
     * @param ids
     * @return Boolean
     */
    Boolean deleteBatch(Integer[] ids);
}
