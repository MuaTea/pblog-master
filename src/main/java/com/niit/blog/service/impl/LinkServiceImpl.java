package com.niit.blog.service.impl;

import com.niit.blog.entity.domain.BlogLink;
import com.niit.blog.mapper.BlogLinkMapper;
import com.niit.blog.service.LinkService;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Title: LinkServiceImpl.java
 * @Description: 友情链接业务实现类
 */
@Service
public class LinkServiceImpl implements LinkService {
	
	@Autowired
    private BlogLinkMapper blogLinkMapper;
	
	/**
	 * @Description: 获取友情链接总记录数
	 * @return
	 */
    @Override
    public int getTotalLinks() {
        return blogLinkMapper.getTotalLinks(null);
    }

    /**
     * 获取友情链接分页数据
     */
    @Override
    public PageResult getBlogLinkPage(PageQueryUtil pageUtil) {
        List<BlogLink> links = blogLinkMapper.findLinkList(pageUtil);
        int total = blogLinkMapper.getTotalLinks(pageUtil);
        PageResult pageResult = new PageResult(links, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * 保存友情链接
     */
    @Override
    public Boolean save(BlogLink link) {
        return blogLinkMapper.insertSelective(link) > 0;
    }
    
    /**
     * 
     * @param id
     * @return
     */
    @Override
    public BlogLink selectById(Integer id) {
        return blogLinkMapper.selectByPrimaryKey(id);
    }

    /**
     * 
     */
    @Override
    public Boolean update(BlogLink tempLink) {
        return blogLinkMapper.updateByPrimaryKeySelective(tempLink) > 0;
    }

    /**
     * 删除友情链接
     */
    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogLinkMapper.deleteBatch(ids) > 0;
    }
}
