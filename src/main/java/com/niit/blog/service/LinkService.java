package com.niit.blog.service;

import com.niit.blog.entity.domain.BlogLink;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;

/**
 * @Title: LinkService.java
 * @Description: 友情链接实体类
 */
public interface LinkService {
	
	/**
	 * @Description: 获取友情链接总记录数
	 * @return int
	 */
    int getTotalLinks();
    
    /**
     * 查询友链的分页数据
     * @param pageUtil
     * @return
     */
    PageResult getBlogLinkPage(PageQueryUtil pageUtil);
    
    /**
     * 保存友情链接
     * @param link
     * @return
     */
    Boolean save(BlogLink link);

	/**
	 * 
	 * @param id
	 * @return
	 */
	BlogLink selectById(Integer id);

	/**
	 * 
	 * @param tempLink
	 * @return
	 */
	Boolean update(BlogLink tempLink);
	
	/**
	 * 根据 id 删除友情链接
	 * @param ids
	 * @return
	 */
	Boolean deleteBatch(Integer[] ids);
}
