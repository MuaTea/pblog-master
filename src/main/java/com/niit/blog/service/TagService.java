package com.niit.blog.service;

import java.util.List;

import com.niit.blog.entity.vo.BlogTagCountVO;
import com.niit.blog.entity.vo.SimpleBlogListVO;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;

/**
 * @Title: TagService.java
 * @Description: 博客标签业务接口
 */
public interface TagService {
	/**
	 * @Description: 获取总标签数
	 * @return int
	 */
    int getTotalTags();

    /**
     * @Description: 查询标签的分页数据并进行封装
     * @param: pageUtil
     * @return: com.niit.blog.util.PageResult
     **/
    PageResult getBlogTagPage(PageQueryUtil pageUtil);

    /**
     * @Description: 新增博客标签
     * @param: tagName
     * @return: boolean
     **/
    boolean saveTag(String tagName);

    /**
     * @Description: 删除标签
     * @param: ids
     * @return: boolean
     **/
    boolean deleteBatch(Integer[] ids);
    
    /**
     * @Description: 获取前20是的热门标签，按照热度由高到低排序。
     * @return List<BlogTagCount>
     */
    List<BlogTagCountVO> getHotTagsForIndex();
    
}
