package com.niit.blog.mapper;

import java.util.List;

import com.niit.blog.entity.domain.BlogTag;
import com.niit.blog.entity.vo.BlogTagCountVO;
import com.niit.blog.util.PageQueryUtil;

/**
 * @Title: BlogTagMapper.java
 * @Description: 标签管理
 */
public interface BlogTagMapper {
    /**
     * @Description: 查询到的标签数量
     * @param pageUtil
     * @return int
     */
    int getTotalTags(PageQueryUtil pageUtil);

    /*查询到的标签列表*/
    List<BlogTag> findTagList(PageQueryUtil pageUtil);

    /**
     * @Description: 根据用户输入的标签名进行查询，避免标签名重复
     * @param: tagName
     * @return: com.niit.blog.entity.domain.BlogTag
     **/
    BlogTag selectByTagName(String tagName);

    /**
     * @Description: 插入标签数据
     * @param: blogTag
     * @return: int
     **/
    int insertSelective(BlogTag blogTag);

    /**
     * @Description:删除标签
     * @param ids
     * @return
     */
    int deleteBatch(Integer[] ids);

    /**
     * @Description: 新增标签数据并修改分类排序值
     * @param: tagList
     * @return: int
     **/
    int batchInsertBlogTag(List<BlogTag> tagList);

    /**
     * @Description: 获取前20是的热门标签，按照热度由高到低排序。
     * @return List<BlogTagCount>
     */
	List<BlogTagCountVO> getHotTagsForIndex();

    int updateTagById(BlogTag blogTag);

    int removeTagById(Integer id);
}
