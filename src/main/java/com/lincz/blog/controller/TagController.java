package com.lincz.blog.controller;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Tag;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;

    // 标签管理页面
    @GetMapping(value = "/management")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    public ModelAndView tagManagementPage() {
        List<Tag> tagList = tagService.getAllTag();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("TagManagement");
        modelAndView.addObject(tagList);
        return modelAndView;
    }

    // 添加标签
    @PostMapping(value = "/")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public Tag addTag(@RequestBody Tag tagDTO) {
        return tagService.createCategory(tagDTO);
    }

    // 删除标签
    @DeleteMapping(value = "/{tagId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    public void deleteTag(@PathVariable Long tagId) {
        tagService.deleteTagByTagId(tagId);
    }

    // 修改标签
    @PutMapping(value = "/{tagId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    public Tag updateTag(@PathVariable Long tagId, @RequestBody Tag tagDTO) {
        return tagService.updateTag(tagId, tagDTO);
    }

    // 分页列出一个标签对应的所有文章
    @GetMapping(value = "/{tagId}/articles")
    public ModelAndView getArticleByTag(@PathVariable Long tagId,
                                        @PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Tag tag = tagService.getTagByTagId(tagId);
        Page<Article> articlePage = articleService.paginateGetArticlesByTags(tag, pageable);
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Index");
        modelAndView.addObject(articleList);
        return modelAndView;
    }

}
