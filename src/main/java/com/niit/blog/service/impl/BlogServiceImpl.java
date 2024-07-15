package com.niit.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niit.blog.entity.domain.Blog;
import com.niit.blog.entity.domain.BlogCategory;
import com.niit.blog.entity.domain.BlogTag;
import com.niit.blog.entity.domain.BlogTagRelation;
import com.niit.blog.entity.vo.BlogDetailVO;
import com.niit.blog.entity.vo.BlogListVO;
import com.niit.blog.entity.vo.SimpleBlogListVO;
import com.niit.blog.mapper.BlogCategoryMapper;
import com.niit.blog.mapper.BlogMapper;
import com.niit.blog.mapper.BlogTagMapper;
import com.niit.blog.mapper.BlogTagRelationMapper;
import com.niit.blog.service.BlogService;
import com.niit.blog.util.MarkDownUtil;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.PageResult;
import com.niit.blog.util.PatternUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: BlogServiceImpl.java
 * @Description: 博客管理实现类
 * @Author: NIIT
 * @CreateTime: 2022-06-02 12:56:09
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Resource
    private BlogMapper blogMapper;
    @Resource
    private BlogCategoryMapper categoryMapper;
    @Resource
    private BlogTagMapper tagMapper;
    @Resource
    private BlogTagRelationMapper blogTagRelationMapper;

    /**
     * @return
     * @Description: 查询博客总数量
     * @Author: NIIT
     * @CreateTime: 2022-06-02 12:56:01
     */
    @Override
    public int getTotalBlogs() {
        return blogMapper.getTotalBlogs(null);
    }

    /**
     * @Description: 查询博客列表
     * @Author: NIIT
     * @CreateTime: 2022-5-27 15:44
     * @param: pageUtil
     * @return: com.niit.blog.util.PageResult
     **/
    @Override
    public PageResult getBlogsPage(PageQueryUtil pageUtil) {
        /*查询博客列表*/
        List<Blog> blogList = blogMapper.findBlogList(pageUtil);
        /*计算博客总条数*/
        int total = blogMapper.getTotalBlogs(pageUtil);
        /*对博客列表和博客数量进行一个封装*/
        PageResult pageResult = new PageResult(blogList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * @Description: 博客新增
     * @Author: NIIT
     * @CreateTime: 2022-5-27 16:15
     * @param: blog
     * @return: java.lang.String
     **/
    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory == null) {
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        } else {
            //设置博客分类名称
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            //分类的排序值加1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");

        if (tags.length > 6) {
            return "标签数量限制为6";
        }
        //保存文章
        if (blogMapper.insertSelective(blog) > 0) {
            //新增的tag对象
            List<BlogTag> tagListForInsert = new ArrayList<>();
            //所有的tag对象，用于建立关系数据
            List<BlogTag> allTagsList = new ArrayList<>();
            for (int i = 0; i < tags.length; i++) {
                BlogTag tag = tagMapper.selectByTagName(tags[i]);
                if (tag == null) {
                    //不存在就新增
                    BlogTag tempTag = new BlogTag();
                    tempTag.setTagName(tags[i]);
                    tagListForInsert.add(tempTag);
                } else {
                    allTagsList.add(tag);
                }
            }
            //新增标签数据并修改分类排序值
            if (!CollectionUtils.isEmpty(tagListForInsert)) {
                tagMapper.batchInsertBlogTag(tagListForInsert);
            }
            if (blogCategory != null) {
                categoryMapper.updateByPrimaryKeySelective(blogCategory);
            }
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            //新增关系数据
            allTagsList.addAll(tagListForInsert);
            for (BlogTag tag : allTagsList) {
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setBlogId(blog.getBlogId());
                blogTagRelation.setTagId(tag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            if (blogTagRelationMapper.batchInsert(blogTagRelations) > 0) {
                return "success";
            }
        }
        return "保存失败";
    }

    /**
     * @param blogId
     * @return
     * @Description: 根据id获取博客详情
     * @Author: NIIT
     * @CreateTime: 2022-05-30 09:56:20
     */
    @Override
    public Blog getBlogById(Long blogId) {
        return blogMapper.selectByPrimaryKey(blogId);
    }

    /**
     * @param blog
     * @return
     * @Description: 更新博客
     * @Author: NIIT
     * @CreateTime: 2022-05-30 10:49:55
     */
    @Override
    @Transactional
    public String updateBlog(Blog blog) {
        Blog blogForUpdate = blogMapper.selectByPrimaryKey(blog.getBlogId());
        if (blogForUpdate == null) {
            return "数据不存在";
        }
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setEnableComment(blog.getEnableComment());
        BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory == null) {
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        } else {
            //设置博客分类名称
            blogForUpdate.setBlogCategoryName(blogCategory.getCategoryName());
            blogForUpdate.setBlogCategoryId(blogCategory.getCategoryId());
            //分类的排序值加1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6) {
            return "标签数量限制为6";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());
        //新增的tag对象
        List<BlogTag> tagListForInsert = new ArrayList<>();
        //所有的tag对象，用于建立关系数据
        List<BlogTag> allTagsList = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            BlogTag tag = tagMapper.selectByTagName(tags[i]);
            if (tag == null) {
                //不存在就新增
                BlogTag tempTag = new BlogTag();
                tempTag.setTagName(tags[i]);
                tagListForInsert.add(tempTag);
            } else {
                allTagsList.add(tag);
            }
        }
        //新增标签数据不为空->新增标签数据
        if (!CollectionUtils.isEmpty(tagListForInsert)) {
            tagMapper.batchInsertBlogTag(tagListForInsert);
        }
        List<BlogTagRelation> blogTagRelations = new ArrayList<>();
        //新增关系数据
        allTagsList.addAll(tagListForInsert);
        for (BlogTag tag : allTagsList) {
            BlogTagRelation blogTagRelation = new BlogTagRelation();
            blogTagRelation.setBlogId(blog.getBlogId());
            blogTagRelation.setTagId(tag.getTagId());
            blogTagRelations.add(blogTagRelation);
        }
        //修改blog信息->修改分类排序值->删除原关系数据->保存新的关系数据
        if (blogCategory != null) {
            categoryMapper.updateByPrimaryKeySelective(blogCategory);
        }
        /** 删除博客与标签的所有关联记录 */
        blogTagRelationMapper.deleteByBlogId(blog.getBlogId());
        /** 重新为博客与标签（批量）插入关联记录 */
        blogTagRelationMapper.batchInsert(blogTagRelations);
        /** 根据 ID 更新数据库中博客记录信息 */
        if (blogMapper.updateByPrimaryKeySelective(blogForUpdate) > 0) {
            return "success";
        }
        return "修改失败";
    }

    /**
     * @param ids
     * @return
     * @Description: 根据博客ID批量删除博客
     * @Author: NIIT
     * @CreateTime: 2022-05-30 02:30:24
     */
    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogMapper.deleteBatch(ids) > 0;
    }

    /**
     * @param page
     * @return
     * @Description: 根据页数（page）获取博客列表，用于显示在访客首页中
     * @Author: NIIT
     * @CreateTime: 2022-05-31 02:50:00
     */
    @Override
    public PageResult getBlogsForIndexPage(int page) {
        Map params = new HashMap();
        params.put("page", page);
        //每页8条
        params.put("limit", 8);
        params.put("blogStatus", 1);//过滤发布状态下的数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        /** 获取博客列表 */
        List<Blog> blogList = blogMapper.findBlogList(pageUtil);

        /** 将博客列表转化为页面显示的VO对象List */
        List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(blogList);
        System.out.println(blogList.size());
        int total = blogMapper.getTotalBlogs(pageUtil);
        PageResult pageResult = new PageResult(blogListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * @param blogList
     * @return List<BlogListVO>
     * @Description: 转化为页面显示的对象集合，并查询显示分类图标
     * @Author: NIIT
     * @CreateTime: 2022-05-31 03:13:03
     */
    private List<BlogListVO> getBlogListVOsByBlogs(List<Blog> blogList) {
        List<BlogListVO> blogListVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(blogList)) {
            /** 获取博客列表中分类ID */
            List<Integer> categoryIds = blogList.stream().map(Blog::getBlogCategoryId).collect(Collectors.toList());
            Map<Integer, String> blogCategoryMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(categoryIds)) {
                /** 根据分类ID，查询分类对象，并放至blogCategoryMap中 */
                List<BlogCategory> blogCategories = categoryMapper.selectByCategoryIds(categoryIds);
                if (!CollectionUtils.isEmpty(blogCategories)) {
                    blogCategoryMap = blogCategories.stream().collect(Collectors.toMap(BlogCategory::getCategoryId, BlogCategory::getCategoryIcon, (key1, key2) -> key2));
                }
            }
            /** 将原始博客列表转化为BlogListVO对象集合 */
            for (Blog blog : blogList) {
                BlogListVO blogListVO = new BlogListVO();
                BeanUtils.copyProperties(blog, blogListVO);
                if (blogCategoryMap.containsKey(blog.getBlogCategoryId())) {
                    blogListVO.setBlogCategoryIcon(blogCategoryMap.get(blog.getBlogCategoryId()));
                } else {
                    blogListVO.setBlogCategoryId(0);
                    blogListVO.setBlogCategoryName("默认分类");
                    blogListVO.setBlogCategoryIcon("/user/dist/img/category/00.png");
                }
                blogListVOS.add(blogListVO);
            }
        }
        return blogListVOS;
    }

    /**
     * @param type
     * @return List<SimpleBlogListVO>
     * @Description: 获取点击数最多的9个博客
     * @Author: NIIT
     * @CreateTime: 2022-05-31 07:14:18
     */
    @Override
    public List<SimpleBlogListVO> getHotBlogsForIndex() {
        List<SimpleBlogListVO> simpleBlogListVOS = new ArrayList<>();
        List<Blog> blogs = blogMapper.getHotBlogsForIndex();
        if (!CollectionUtils.isEmpty(blogs)) {
            for (Blog blog : blogs) {
                SimpleBlogListVO simpleBlogListVO = new SimpleBlogListVO();
                BeanUtils.copyProperties(blog, simpleBlogListVO);
                simpleBlogListVOS.add(simpleBlogListVO);
            }
        }
        return simpleBlogListVOS;
    }

    /**
     * @param keyword
     * @param page
     * @return
     * @Description: 根据关键字搜索博客
     * @Author: NIIT
     * @CreateTime: 2022-05-31 08:02:21
     */
    @Override
    public PageResult getBlogsPageBySearch(String keyword, int page) {
        if (page > 0 && PatternUtil.validKeyword(keyword)) {
            Map param = new HashMap();    //封装查询条件
            param.put("page", page);    //第几页？
            param.put("limit", 8);        //每页记录数
            param.put("keyword", keyword);    //搜索关键字
            param.put("blogStatus", 1);    //过滤发布状态下的数据
            PageQueryUtil pageUtil = new PageQueryUtil(param);
            List<Blog> blogList = blogMapper.findBlogList(pageUtil);
            List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(blogList);
            int total = blogMapper.getTotalBlogs(pageUtil);
            PageResult pageResult = new PageResult(blogListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
            return pageResult;
        }
        return null;
    }

    /**
     * @param id
     * @return
     * @Description: 根据博客 ID 获取博客详情对象
     * @Author: NIIT
     * @CreateTime: 2022-05-31 11:51:31
     */
    @Override
    public BlogDetailVO getBlogDetail(Long id) {
        Blog blog = blogMapper.selectByPrimaryKey(id);
        //不为空且状态为已发布
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null) {
            return blogDetailVO;
        }
        return null;
    }

    /**
     * @param blog
     * @return BlogDetailVO
     * @Description: 转化封装页面博客详情对象
     * @Author: NIIT
     * @CreateTime: 2022-05-31 11:50:54
     */
    private BlogDetailVO getBlogDetailVO(Blog blog) {
        if (blog != null && blog.getBlogStatus() == 1) {
            //增加浏览量
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.updateByPrimaryKey(blog);
            //实例化页面渲染的博客详情对象
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));
            BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
            if (blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/user/dist/img/category/00.png");
            }
            //分类信息
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            if (!StringUtils.isEmpty(blog.getBlogTags())) {
                //标签设置
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVO.setBlogTags(tags);
            }
//            //设置评论数
//            Map params = new HashMap();
//            params.put("blogId", blog.getBlogId());
//            params.put("commentStatus", 1);//过滤审核通过的数据
//            blogDetailVO.setCommentCount(blogCommentMapper.getTotalBlogComments(params));
            return blogDetailVO;
        }
        return null;
    }

    /**
     * @param tagName
     * @param page
     * @return PageResult
     * @Description: 根据标标签查看文章列表
     * @Author: NIIT
     * @CreateTime: 2022-06-01 02:47:38
     */
    @Override
    public PageResult getBlogsPageByTag(String tagName, int page) {
        BlogTag tag = tagMapper.selectByTagName(tagName);
        if (tag != null && page > 0) {
            Map param = new HashMap();
            param.put("page", page);
            param.put("limit", 9);
            param.put("tagId", tag.getTagId());
            PageQueryUtil pageUtil = new PageQueryUtil(param);
            /** 根据查询条件获取博客列表 */
            List<Blog> blogList = blogMapper.getBlogsPageByTagId(pageUtil);
            /** 将原始博客列表转化为页面显示的集合对象 */
            List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(blogList);
            /** 获取文章数量，用于构造分页对象 */
            int total = blogMapper.getTotalBlogsByTagId(pageUtil);
            PageResult pageResult = new PageResult(blogListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
            return pageResult;
        }
        return null;
    }

    @Override
    public PageInfo<Blog> listBlogs(String blogTitle, Integer pageNum, Integer pageSize) {
        if (ObjectUtils.isEmpty(pageNum) || pageNum < 0) {
            pageNum = 1;
        }
        if (ObjectUtils.isEmpty(pageSize)) {
            pageSize = 10;
            if (pageSize > 100) {
                pageSize = 100;
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blog = blogMapper.selectAll(blogTitle);
        PageInfo<Blog> pageInfo = new PageInfo<>(blog);
        return pageInfo;
    }
}

