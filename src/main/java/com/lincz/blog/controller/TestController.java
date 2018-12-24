package com.lincz.blog.controller;

import com.lincz.blog.entity.Account;
import com.lincz.blog.repository.ArticleRepository;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.util.AccountUtils;
import com.lincz.blog.util.ArticleUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
        String fileName = avatarFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        File tempAvatar = new File("blog-data/account/avatar/"+1+"."+suffix);
        File avatar = new File("blog-data/account/avatar/"+1+"."+suffix);
        try {
            avatarFile.transferTo(Paths.get(tempAvatar.toURI()));
            AccountUtils.resizeImage(tempAvatar,avatar,200);
        }
        catch (IOException ex){
            System.out.println("无法写入头像文件");
        }
        Account account = accountService.getAccountByAccountId(Long.valueOf(1));
        account.setEmail(formAccount.getEmail());
        account.setPassword(formAccount.getPassword());
        account.setAvatar(avatar.getPath());
    }

    public Account currentAccount(){
        return accountService.getAccountByUsername(((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }



}