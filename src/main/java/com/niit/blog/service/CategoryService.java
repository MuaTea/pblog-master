package com.niit.blog.service;

import java.util.List;

import com.niit.blog.entity.domain.BlogCategory;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;

/**
 * @Title: CategoryService.java
 * @Description: 博客类型 Service 接口
 */
public interface CategoryService {
	/**
	 * @Description: 获取总分类数量
	 * @return int
	 */
    int getTotalCategories();

    PageResult getBlogCategoryPage(PageQueryUtil pageUtil);

    /**
     * @Description: 博客新增分类
     * @param: categoryName
     * @return: categoryIcon
     **/
    boolean saveCategory(String categoryName, String categoryIcon);

    /**
     * @Description: 分类修改
     * @param categoryId
     * @param categoryName
     * @param categoryIcon
     * @return
     */
    boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);

    /**
     * @Description: 分类删除
     * @param ids
     * @return
     */
    boolean deleteBatch(Integer[] ids);
    
    /**
     * @Description: 获取所有博客类型列表
     * @return List<BlogCategory>
     */
    List<BlogCategory> getAllCategories();

    /**
     * @Description: 根据分类ID查询分类详细信息
     * @param id
     * @return BlogCategory
     */
	BlogCategory selectById(Integer id);
}
