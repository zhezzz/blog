package com.lincz.blog.controller;


import com.lincz.blog.entity.*;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CategoryService;
import com.lincz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;


    @GetMapping(value = "/")
    public ModelAndView index(){
        return new ModelAndView("Index");
    }

    @GetMapping(value = "/register")
    public ModelAndView register(){
        return new ModelAndView("Register");
    }

    //注册用户
    @PostMapping(value = "/register")
    public String createAccount(Account formAccount) {
        Account account = new Account(formAccount.getUsername(),formAccount.getPassword(),formAccount.getEmail(), "default.jpg");
        accountService.createAccount(account);
        return "redirect:/";
    }




    @GetMapping(value = "/all")
    public ModelAndView getAllArticle(@PageableDefault(size = 10,sort = { "createDate" }, direction = Sort.Direction.DESC) Pageable pageable){
        Page<Article> articlePage = articleService.paginateGetAllArticles(pageable);
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Index");
        modelAndView.addObject(articleList);
        return modelAndView;
    }

    //分页列出一个分类下的所有文章
    @GetMapping(value = "/category/{categoryId}")
    public ModelAndView getArticlesByCategory(@PathVariable Long categoryId, @PageableDefault(size = 10,sort = { "createDate" }, direction = Sort.Direction.DESC) Pageable pageable){
        Category category = categoryService.getCategoryByCategoryId(categoryId);
        Page<Article> articlePage = articleService.paginateGetArticlesByCategory(category,pageable);
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Index");
        modelAndView.addObject(articleList);
        return modelAndView;
    }

    //分页列出一个标签对应的所有文章
    @GetMapping(value = "/tag/{tagId}")
    public ModelAndView getArticleByTag(@PathVariable Long tagId, @PageableDefault(size = 10,sort = { "createDate" }, direction = Sort.Direction.DESC) Pageable pageable){
        Tag tag = tagService.getTagByTagId(tagId);
        Page<Article> articlePage = articleService.paginateGetArticlesByTag(tag,pageable);
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Index");
        modelAndView.addObject(articleList);
        return modelAndView;
    }

}
