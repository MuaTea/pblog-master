package com.niit.blog.controller;

import com.niit.blog.entity.domain.BlogTag;
import com.niit.blog.entity.vo.AjaxResult;
import com.niit.blog.mapper.BlogTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagsController {
    @Autowired
    private BlogTagMapper tagService;

    @GetMapping("/list")
    public AjaxResult queryTagList() {
        List<BlogTag> list = tagService.findTagList(null);
        return AjaxResult.success(200, "获取成功", list);
    }

    @PostMapping("/update")
    public AjaxResult updateTagById(@RequestBody BlogTag blogTag) {
        return tagService.updateTagById(blogTag) > 0 ? AjaxResult.success(200, "更新成功", null) : AjaxResult.error(500, "更新失败");
    }

    @PostMapping("/remove/{id}")
    public AjaxResult removeTagById(@PathVariable Integer id) {
        return tagService.removeTagById(id) > 0 ? AjaxResult.success(200, "删除成功", null) : AjaxResult.error(500, "删除失败");
    }


}
