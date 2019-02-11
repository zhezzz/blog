package com.lincz.blog.controller;

import com.lincz.blog.entity.Account;
import com.lincz.blog.repository.ArticleRepository;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class TestController
{

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;
    @Autowired
    ArticleRepository articleRepository;



    @GetMapping(value = "/test")
    public ModelAndView objectinfo(){
        ModelAndView modelAndView = new ModelAndView("test");
        return modelAndView;
    }

    @PostMapping(value = "/upload")
    public void updateAccount( MultipartFile avatarFile, Account formAccount){

    }

    public Account currentAccount(){
        return accountService.getAccountByUsername(((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }



}