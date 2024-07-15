package com.niit.blog.mapper;

import java.util.List;

import com.niit.blog.entity.domain.BlogLink;
import com.niit.blog.util.PageQueryUtil;

/**
 * @Title: BlogLinkMapper.java
 * @Description: 友情链接
 */
public interface BlogLinkMapper {
	
	/**
	 * @Description:根据查询条件获取友情链接列表
	 * @param pageUtil
	 * @return
	 */
	List<BlogLink> findLinkList(PageQueryUtil pageUtil);
	
	/**
	 * @Description: 根据查询条件获取友情链接记录数
	 * @param pageUtil
	 * @return int
	 */
	int getTotalLinks(PageQueryUtil pageUtil);
	
	/**
	 * @Description: 
	 * @param record
	 * @return
	 */
	int insertSelective(BlogLink record);

	/**
	 * 
	 * @param id
	 * @return
	 */
	BlogLink selectByPrimaryKey(Integer id);

	/**
	 * 
	 * @param tempLink
	 * @return
	 */
	int updateByPrimaryKeySelective(BlogLink tempLink);
	
	/**
	 * 根据 id 删除友情链接
	 * @param linkId
	 * @return
	 */
	int deleteByPrimaryKey(Integer linkId);
	
	/**
	 * 根据 id 批量删除友情链接
	 * @param ids
	 * @return
	 */
	int deleteBatch(Integer[] ids);
}