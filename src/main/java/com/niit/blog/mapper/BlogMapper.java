package com.niit.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niit.blog.entity.domain.Blog;
import com.niit.blog.util.PageQueryUtil;

/**
 * @Title: BlogMapper.java
 * @Description: 博客持久层接口
 */
public interface BlogMapper {
	/**
	 * @Description: 查询博客总数量
	 * @param pageUtil
	 * @return int
	 */
	int getTotalBlogs(PageQueryUtil pageUtil);

	/**
	 * @Description: 修改分类实体
	 * @param categoryName
	 * @param categoryId
	 * @param ids
	 * @return
	 */
	int updateBlogCategorys(@Param("categoryName") String categoryName, @Param("categoryId") Integer categoryId,
			@Param("ids") Integer[] ids);

	/**
	 * @Description: 查询博客列表
	 * @param: pageUtil
	 * @return: java.util.List<com.niit.blog.entity.domain.Blog>
	 **/
	List<Blog> findBlogList(PageQueryUtil pageUtil);

	/**
	 * @Description: 新增博客
	 * @param: record
	 * @return: int
	 **/
	int insertSelective(Blog record);

	/**
	 * @Description: 根据id获取博客详情
	 * @param blogId
	 * @return Blog
	 */
	Blog selectByPrimaryKey(Long blogId);

	/**
	 * @Description: 根据ID更新博客内容
	 * @param record
	 * @return int
	 */
	int updateByPrimaryKeySelective(Blog record);

	/**
	 * @Description: 根据博客ID批量删除博客
	 * @param ids
	 * @return int
	 */
	int deleteBatch(Integer[] ids);

	/**
	 * @Description: 获取点击数最多的9个博客
	 * @return List<Blog>
	 */
	List<Blog> getHotBlogsForIndex();

	/**
	 * @Description: 修改博客所有信息
	 * @param record
	 * @return int
	 */
	int updateByPrimaryKey(Blog record);

	/**
	 * @Description: 根据标签获取标签下的文章列表（分页）
	 * @param pageUtil
	 * @return List<Blog>
	 */
	List<Blog> getBlogsPageByTagId(PageQueryUtil pageUtil);

	/**
	 * @Description: 根据标签获取标签下的文章总数
	 * @param pageUtil
	 * @return int
	 */
	int getTotalBlogsByTagId(PageQueryUtil pageUtil);

    List<Blog> selectAll(String blogTitle);
}
