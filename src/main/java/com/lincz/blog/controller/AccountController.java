package com.lincz.blog.controller;


import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CommentService;
import com.lincz.blog.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    //用户个人主页功能，
    @GetMapping(value = "/")
    public ModelAndView accountHomePage(@PathVariable Long accountId){
        return null;
    }

    //获取账户信息修改界面
    @GetMapping(value = "/update/{accountId}")
    public ModelAndView updateAccountView(@PathVariable Long accountId){
        Account account = accountService.getAccountByAccountId(accountId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("UpdateAccount");
        modelAndView.addObject(account);
        return modelAndView;
    }

    //修改用户邮箱密码
    @PostMapping(value = "/update/{accountId}/info")
    public String updateAccountInfo(@PathVariable Long accountId, Account formAccount){
        accountService.updateAccountInfo(accountId,formAccount);
        return "redirect:/";
    }

    //修改头像
    @PostMapping(value = "/update/{accountId}/avatar")
    public void updateAccountAvatar(@PathVariable Long accountId, MultipartFile avatarFile) throws IOException{
        //TODO 放弃上传文件，改用网络图片链接
        String fileName = avatarFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        File tempAvatar = new File("blog-data/account/avatar/"+accountId+"."+suffix);
        File avatar = new File("blog-data/account/avatar/"+accountId+"."+suffix);
        avatarFile.transferTo(Paths.get(tempAvatar.toURI()));
//        AccountUtils.resizeImage(tempAvatar,avatar,200);
        accountService.updateAccountAvatar(accountId,avatar.getPath());
    }

    //获取用户所有评论（分页）
    @GetMapping(value = "/{accountId}/coments")
    public ModelAndView getAllComments(@PathVariable Long accountId, @PageableDefault(size = 10,sort = { "createDate" }, direction = Sort.Direction.DESC) Pageable pageable){
        Page<Comment> commentPage = commentService.paginateGetCommentsByAccountId(accountId,pageable);
        List<Comment> commentList = commentPage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("AccountComments");
        modelAndView.addObject(commentList);
        return modelAndView;
    }

    //获取用户所有文章（分页）
    @GetMapping(value = "/{accountId}/articles")
    public ModelAndView getAllArticles(@PathVariable Long accountId, @PageableDefault(size = 10,sort = { "createDate" }, direction = Sort.Direction.DESC) Pageable pageable){
        Page<Article> articlePage = articleService.paginateGetArticlesByAccount(accountId,pageable);
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("AccountArticles");
        modelAndView.addObject(articleList);
        return modelAndView;
    }

    //获取当前用户
    public Account currentAccount(){
        return accountService.getAccountByUsername(((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }
}
