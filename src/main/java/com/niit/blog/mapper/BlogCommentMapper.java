package com.niit.blog.mapper;

import com.niit.blog.entity.domain.BlogComment;
import com.niit.blog.util.PageQueryUtil;

import java.util.List;
import java.util.Map;

/**
 * @Title: BlogCommentMapper.java
 * @Description: TODO (请对此类进行描述说明)
 */
public interface BlogCommentMapper {

    /**
     * @param map
     * @return int
     * @Description: 获取总的评论数，在后台管理首页显示
     */
    int getTotalBlogComments(Map map);

    /**
     * @param record
     * @return int
     * @Description: 访客发表评论
     */
    int insertSelective(BlogComment record);

    /**
     * @param pageUtil
     * @return List<BlogComment>
     * @Description: 查询博客评论列表，在博客详情页显示
     * @Author: NIIT
     * @CreateTime: 2022-06-01 11:20:05
     */
    List<BlogComment> findBlogCommentList(PageQueryUtil pageUtil);

    /**
     * @param commentId
     * @return BlogComment
     * @Description: 根据 ID 查询评论对象
     */
    BlogComment selectByPrimaryKey(Long commentId);

    /**
     * @param record
     * @return int
     * @Description: 根据 Id 修改评论记录
     */
    int updateByPrimaryKeySelective(BlogComment record);

    /**
     * @param ids
     * @return int
     * @Description: 根据 ID 批量禁用修改评论状态
     */
    int checkDone(Integer[] ids);

    /**
     * @param ids
     * @return int
     * @Description: 批量删除
     */
    int deleteBatch(Integer[] ids);

    List<BlogComment> queryBlogCommentList(String searchText);
}