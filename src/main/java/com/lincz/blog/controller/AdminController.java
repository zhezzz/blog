package com.lincz.blog.controller;


import com.lincz.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    //查看所有用户信息

    //删除用户

    //封禁用户

    //删除博客

    //删除留言

    //ROOT管理员权限，可指定普通管理员


}
