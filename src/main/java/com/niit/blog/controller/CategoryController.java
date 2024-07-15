package com.niit.blog.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.niit.blog.entity.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niit.blog.entity.domain.BlogCategory;
import com.niit.blog.service.CategoryService;
import com.niit.blog.util.PageQueryUtil;
import com.niit.blog.util.Result;
import com.niit.blog.util.ResultGenerator;

/**
 * @Description: 博客分类控制器
 * @Author: NIIT
 * @CreateTime: 2022-5-27 10:16
 **/
@Controller
@RequestMapping("/user")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @Description: 分类数据页面
     * @Author: NIIT
     * @CreateTime: 2022-5-27 10:16
     * @param: request
     * @return: java.lang.String
     **/
    @GetMapping("/categories")
    public String categoryPage(HttpServletRequest request) {
        request.setAttribute("path", "categories");
        return "user/category";
    }

     /**
      * @Description: 查询分类列表
      * @Author: NIIT
      * @CreateTime: 2022-5-27 10:17
      * @param: params
      * @return: com.niit.blog.util.Result
      **/
    @RequestMapping(value = "/categories/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(categoryService.getBlogCategoryPage(pageUtil));
    }

    /**
     * @Description: 新增博客分类
     * @Author: NIIT
     * @CreateTime: 2022-5-27 10:37
     * @param: categoryName
     * @param: categoryIcon
     * @return: com.niit.blog.util.Result
     **/
    @RequestMapping(value = "/categories/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName,
                       @RequestParam("categoryIcon") String categoryIcon) {
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }
        if (categoryService.saveCategory(categoryName, categoryIcon)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }

    /**
     * @Description: 根据ID查询分类详情对象
     * @Author: NIIT
     * @CreateTime: 2022-05-30 03:22:52
     * @param id
     * @return Result
     */
    @GetMapping("/categories/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id) {
    	BlogCategory link = categoryService.selectById(id);
        return ResultGenerator.genSuccessResult(link);
    }
    
    /**
     * @Description: 分类修改
     * @Author: NIIT
     * @CreateTime: 2022-5-27 12:34
     * @param: categoryId
     * @param: categoryName
     * @param: categoryIcon
     * @return: com.niit.blog.util.Result
     **/
    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestParam("categoryId") Integer categoryId,
                         @RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryIcon") String categoryIcon) {
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }
        if (categoryService.updateCategory(categoryId, categoryName, categoryIcon)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }


    /**
     * @Description: 分类删除
     * @Author: NIIT
     * @CreateTime: 2022-5-27 12:37
     * @param: ids
     * @return: com.niit.blog.util.Result
     **/
    @RequestMapping(value = "/categories/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (categoryService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    /**
     * 获取分类列表
     */
    @ResponseBody
    @GetMapping("/category/info")
    public AjaxResult queryCategoryList(){
        List<BlogCategory> allCategories = categoryService.getAllCategories();
        return new AjaxResult(200,"获取成功",allCategories);
    }
}
