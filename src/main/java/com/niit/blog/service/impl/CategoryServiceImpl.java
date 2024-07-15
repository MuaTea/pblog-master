package com.niit.blog.service.impl;

import com.niit.blog.mapper.BlogCategoryMapper;
import com.niit.blog.entity.domain.BlogCategory;
import com.niit.blog.mapper.BlogMapper;
import com.niit.blog.service.CategoryService;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Title: CategoryServiceImpl.java
 * @Description: 博客分类业务实现类
 * @Author: NIIT
 * @CreateTime: 2022-06-02 03:19:58
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private BlogCategoryMapper blogCategoryMapper;
    @Resource
    private BlogMapper blogMapper;

    /**
     * @Description: 总分类数量
     * @Author: NIIT
     * @CreateTime: 2022-06-02 03:19:26
     * @return
     */
    @Override
    public int getTotalCategories() {
        return blogCategoryMapper.getTotalCategories(null);
    }

    @Override
    public PageResult getBlogCategoryPage(PageQueryUtil pageUtil) {
        /*查询博客分类列表*/
        List<BlogCategory> categoryList = blogCategoryMapper.findCategoryList(pageUtil);
        /*对博客分类进行计数*/
        int total = blogCategoryMapper.getTotalCategories(pageUtil);
        /*将查询到的博客分类信息进行赋值并且返回给pageResult*/
        PageResult pageResult = new PageResult(categoryList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * @Description: 分类的添加
     * @Author: NIIT
     * @CreateTime: 2022-5-27 12:17
     * @param: categoryName
     * @param: categoryIcon
     * @return: boolean
     **/
    @Override
    public boolean saveCategory(String categoryName, String categoryIcon) {
        /*判断分类名称有无重复，避免重复添加*/
        BlogCategory temp = blogCategoryMapper.selectByCategoryName(categoryName);
        if (temp == null) {
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            /*插入分类数据*/
            return blogCategoryMapper.insertSelective(blogCategory) > 0;
        }
        return false;
    }
    
    /**
     * @Description: 根据ID获取分类详情
     * @Author: NIIT
     * @CreateTime: 2022-05-30 03:25:40
     * @param id
     * @return
     */
    @Override
	public BlogCategory selectById(Integer id) {
		return blogCategoryMapper.selectByPrimaryKey(id);
	}

    /**
     * 分类修改
     * @param categoryId
     * @param categoryName
     * @param categoryIcon
     * @return
     */
    @Override
    @Transactional  //保证事务的同时成功或者同时失败
    public boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        /*根据分类id查询分类的数据，如果为空，则提示不存在，不为空，则进行修改操作*/
        BlogCategory blogCategory = blogCategoryMapper.selectByPrimaryKey(categoryId);
        if (blogCategory != null) {
            blogCategory.setCategoryIcon(categoryIcon);
            blogCategory.setCategoryName(categoryName);
            //修改分类实体
            blogMapper.updateBlogCategorys(categoryName, blogCategory.getCategoryId(), new Integer[]{categoryId});
            return blogCategoryMapper.updateByPrimaryKeySelective(blogCategory) > 0;
        }
        return false;
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //修改tb_blog表
        blogMapper.updateBlogCategorys("默认分类", 0, ids);
        //删除分类数据
        return blogCategoryMapper.deleteBatch(ids) > 0;
    }
    
    /**
     * @Description: 获取所有博客类型列表
     * @Author: NIIT
     * @CreateTime: 2022-05-30 08:12:37
     * @return
     */
    @Override
    public List<BlogCategory> getAllCategories() {
        return blogCategoryMapper.findCategoryList(null);
    }
}
