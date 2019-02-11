package com.lincz.blog.controller;


import com.lincz.blog.entity.Category;
import com.lincz.blog.entity.Tag;
import com.lincz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    //标签管理页面
    @GetMapping(value = "/")
    public ModelAndView tagManagementPage(){
        List<Tag> tagList = tagService.getAllTag();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("TagManagement");
        modelAndView.addObject(tagList);
        return modelAndView;
    }

    //添加标签
    @PostMapping(value = "/add")
    public Tag addTag(Tag formTag){
        Tag tag = new Tag(formTag.getTagName());
        tagService.createCategory(tag);
        return tag;
    }

    //删除标签
    @DeleteMapping(value = "/delete/{tagId}")
    public void deleteTag(@PathVariable Long tagId){
        tagService.deleteTagByTagId(tagId);
    }

    //修改标签
    @PutMapping(value = "/update/{tagId}")
    public Tag updateTag(@PathVariable Long tagId, Tag formTag){
        return tagService.updateTag(tagId,formTag);
    }



}
