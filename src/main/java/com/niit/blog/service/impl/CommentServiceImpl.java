package com.niit.blog.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.niit.blog.entity.domain.BlogComment;
import com.niit.blog.mapper.BlogCommentMapper;
import com.niit.blog.service.CommentService;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;

/**
 * @Title: CommentServiceImpl.java
 * @Description: 评论管理业务实现类
 */
@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
    private BlogCommentMapper blogCommentMapper;
	
	/**
	 * @Description: 获取总的评论数，在后台管理首页显示
	 * @return
	 */
    @Override
    public int getTotalComments() {
    	return blogCommentMapper.getTotalBlogComments(null);
    }

    /**
     * @Description: 访客发表评论
     * @param blogComment
     * @return
     */
    @Override
    public Boolean addComment(BlogComment blogComment) {
        return blogCommentMapper.insertSelective(blogComment) > 0;
    }
    
    /**
     * @Description: 根据博客ID获取该博客的所有评论信息
     * @param blogId
     * @param page
     * @return
     */
    @Override
    public PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page) {
        if (page < 1) {
            return null;
        }
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("page", page);
        //每页8条
        params.put("limit", 8);
        params.put("blogId", blogId);
        params.put("commentStatus", 0);	//只显示未被禁用的评论信息
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        List<BlogComment> comments = blogCommentMapper.findBlogCommentList(pageUtil);
        if (!CollectionUtils.isEmpty(comments)) {
            int total = blogCommentMapper.getTotalBlogComments(pageUtil);
            PageResult pageResult = new PageResult(comments, total, pageUtil.getLimit(), pageUtil.getPage());
            return pageResult;
        }
        return null;
    }

    /**
     * @Description: 后台管理——评论列表
     * @param pageUtil
     * @return
     */
	@Override
	public PageResult getCommentsPage(PageQueryUtil pageUtil) {
		/** 查询评论列表 */
		List<BlogComment> comments = blogCommentMapper.findBlogCommentList(pageUtil);
		/** 查询评论数量 */
        int total = blogCommentMapper.getTotalBlogComments(pageUtil);
        /** 封装页面列表分页对象 */
        PageResult pageResult = new PageResult(comments, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
	}
	
	/**
	 * @Description: 回复评论
	 * @param commentId
	 * @param replyBody
	 * @return
	 */
	@Override
    public Boolean reply(Long commentId, String replyBody) {
        BlogComment blogComment = blogCommentMapper.selectByPrimaryKey(commentId);
        //blogComment不为空且状态为正常显示 0 ，则继续后续操作
        if (blogComment != null && blogComment.getCommentStatus().intValue() == 0) {
            blogComment.setReplyBody(replyBody);
            blogComment.setReplyCreateTime(new Date());
            return blogCommentMapper.updateByPrimaryKeySelective(blogComment) > 0;
        }
        return false;
    }
	
	/**
	 * @Description: 后台管理——批量禁用功能
	 * @param ids
	 * @return
	 */
	@Override
    public Boolean checkDone(Integer[] ids) {
        return blogCommentMapper.checkDone(ids) > 0;
    }
	
	/**
	 * @Description: 后台管理——批量删除
	 * @param ids
	 * @return
	 */
	@Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogCommentMapper.deleteBatch(ids) > 0;
    }
}
