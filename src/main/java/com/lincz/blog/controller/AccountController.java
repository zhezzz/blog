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
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;


    //修改用户邮箱密码
    @PutMapping("/put")
    public void updateAccount(Account formAccount){
        Account account = currentAccount();
        account.setEmail(formAccount.getEmail());
        account.setPassword(formAccount.getPassword());
        accountService.updateAccount(account);
    }

    //修改用头像

    //获取用户所有评论（分页）

    //用户个人主页功能，同时获取用户所有文章（分页）




    public Account currentAccount(){
        return accountService.getAccountByUsername(((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }

}
