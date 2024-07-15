package com.niit.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niit.blog.entity.domain.BlogCategory;
import com.niit.blog.util.PageQueryUtil;

/**
 * @Title: BlogCategoryMapper.java
 * @Description: 分类管理
 */
public interface BlogCategoryMapper {
    List<BlogCategory> findCategoryList(PageQueryUtil pageUtil);

    /**
     * @Description: 获取总分类数量
     * @param pageUtil
     * @return int
     */
    int getTotalCategories(PageQueryUtil pageUtil);

    /**
     * @Description: 判断分类名称有无重复，避免重复添加
     * @param categoryName
     * @return
     */
    BlogCategory selectByCategoryName(String categoryName);

    /**
     * @Description: 插入分类数据
     * @param blogCategory
     * @return
     */      
    int insertSelective(BlogCategory blogCategory);

    /**
     * @Description: 根据ID查询分类记录     
     * @param categoryId
     * @return BlogCategory
     */
    BlogCategory selectByPrimaryKey(Integer categoryId);

    /**
     * @Description: 对查询到的要修改的分类进行更新操作
     * @param blogCategory
     * @return
     */
    int updateByPrimaryKeySelective(BlogCategory blogCategory);


    /**
     * @Description: 删除分类
     * @param ids
     * @return
     */
    int deleteBatch(Integer[] ids);
    
    /**
     * @Description: 根据分类ID查询对象集合     
     * @param categoryIds
     * @return List<BlogCategory>
     */
    List<BlogCategory> selectByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);
}
