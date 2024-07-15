package com.niit.blog.service.impl;


import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.niit.blog.entity.domain.BlogTag;
import com.niit.blog.entity.vo.BlogTagCountVO;
import com.niit.blog.mapper.BlogTagMapper;
import com.niit.blog.mapper.BlogTagRelationMapper;
import com.niit.blog.service.TagService;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;

/**
 * @Title: TagServiceImpl.java
 * @Description: 博客标签业务实现类
 */
@Service
public class TagServiceImpl implements TagService {
    @Resource
    private BlogTagMapper blogTagMapper;
    @Resource
    private BlogTagRelationMapper relationMapper;

    /**
     * @Description: 获取总标签数
     * @return
     */
    @Override
    public int getTotalTags() {
        return blogTagMapper.getTotalTags(null);
    }

    @Override
    public PageResult getBlogTagPage(PageQueryUtil pageUtil) {
        /*查询到的标签列表*/
        List<BlogTag> tags = blogTagMapper.findTagList(pageUtil);
        /*查询到的标签数量*/
        int total = blogTagMapper.getTotalTags(pageUtil);
        /*对标签列表、标签数量根据PageResult中的规则进行一个封装*/
        PageResult pageResult = new PageResult(tags, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * @Description: 新增博客标签
     * @param: tagName
     * @return: boolean
     **/
    @Override
    public boolean saveTag(String tagName) {
        /*对新增博客进行一个数据库的查询，避免标签名称重复*/
        BlogTag temp = blogTagMapper.selectByTagName(tagName);
        /*如果不重复，则插入数据*/
        if (temp == null) {
            BlogTag blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            return blogTagMapper.insertSelective(blogTag) > 0;
        }
        return false;
    }

    /**
     * @Description: 删除标签
     * @param: ids
     * @return: boolean
     **/
    @Override
    public boolean deleteBatch(Integer[] ids) {
        //已存在关联关系不删除
        List<Long> relations = relationMapper.selectDistinctTagIds(ids);
        if (!CollectionUtils.isEmpty(relations)) {
            return false;
        }
        //删除tag
        return blogTagMapper.deleteBatch(ids) > 0;
    }

    /**
     * @Description: 获取前20是的热门标签，按照热度由高到低排序。
     * @return List<BlogTagCount>
     */
    @Override
    public List<BlogTagCountVO> getHotTagsForIndex() {
        return blogTagMapper.getHotTagsForIndex();
    }
    
    
}
