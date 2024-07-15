package com.niit.blog.mapper;

import com.niit.blog.entity.domain.BlogTagRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title: BlogTagRelationMapper.java
 * @Description: 博客与标签的关联关系
 */
public interface BlogTagRelationMapper {
    /*查询标签之间的关联关系*/
    List<Long> selectDistinctTagIds(Integer[] ids);

    /**
     * @Description: 新增关系数据
     * @param: blogTagRelationList
     * @return: int
     **/
    int batchInsert(@Param("relationList") List<BlogTagRelation> blogTagRelationList);
    
    /**
     * @Description: 根据博客ID 删除其所有标签记录
     * @param blogId
     * @return int
     */
    int deleteByBlogId(Long blogId);
}
