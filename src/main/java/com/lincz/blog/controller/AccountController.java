package com.lincz.blog.controller;


import com.lincz.blog.entity.Article;
import com.lincz.blog.enums.AccountRolePermissionEnum;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.util.AccountUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping(value = "/account/{accountId}")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;

    //用户个人主页功能，
    @GetMapping(value = "/")
    public ModelAndView accountHomePage(@PathVariable Long accountId){
        return null;
    }

    //获取账户信息修改界面
    @GetMapping(value = "/update")
    public ModelAndView updateAccountView(@PathVariable Long accountId){
        Account account = accountService.getAccountByAccountId(accountId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("UpdateAccount");
        modelAndView.addObject(account);
        return modelAndView;
    }

    //修改用户邮箱密码
    @PostMapping(value = "/update/info")
    public String updateAccountInfo(@PathVariable Long accountId, Account formAccount){
        accountService.updateAccountInfo(accountId,formAccount);
        return "redirect:/";
    }

    //修改头像
    @PostMapping(value = "/update/avatar")
    public void updateAccountAvatar(@PathVariable Long accountId, MultipartFile avatarFile){
        String fileName = avatarFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        File tempAvatar = new File("blog-data/account/avatar/"+accountId+"."+suffix);
        File avatar = new File("blog-data/account/avatar/"+accountId+"."+suffix);
        try {
            avatarFile.transferTo(Paths.get(tempAvatar.toURI()));
            AccountUtils.resizeImage(tempAvatar,avatar,200);
        }
        catch (IOException ex){
            System.out.println("无法写入头像文件");
        }
        accountService.updateAccountAvatar(accountId,avatar.getPath());
    }

    //获取用户所有评论（分页）
    @GetMapping(value = "/coments")
    public ModelAndView getAllComments(@PathVariable Long accountId, Pageable pageable){
        return null;
    }

    //获取用户所有文章（分页）
    @GetMapping(value = "/articles")
    public ModelAndView getAllArticles(@PathVariable Long accountId, Pageable pageable){
        Account account = accountService.getAccountByAccountId(accountId);
        Page<Article> articles = articleService.paginateGetArticlesByAccountId(accountId,pageable);
        return null;
    }






    public Account currentAccount(){
        return accountService.getAccountByUsername(((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }

}
