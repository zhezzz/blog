package com.lincz.blog.controller;


import com.lincz.blog.enums.AccountRolePermissionEnum;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/user")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;

    //注册用户
    @PostMapping("/register")
    public Account createBlogUser(Account formUser) throws Exception{
        Account account = new Account(formUser.getUsername(),"default.jpg",formUser.getPassword(),formUser.getEmail(), AccountRolePermissionEnum.ROLE_2.getRoleName());
        return accountService.createAccount(account);
    }

    //修改用户邮箱密码
    @PutMapping
    public void updataBlogUser(Account formUser){
        Account account = currentBlogUser();
        account.setEmail(formUser.getEmail());
        account.setPassword(formUser.getPassword());
        accountService.updataAccount(account);
    }

    //修改用头像

    //获取用户所有评论（分页）

    //用户个人主页功能，同时获取用户所有文章（分页）




    public Account currentBlogUser(){
        return accountService.getAccountByUsername(((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }

}
