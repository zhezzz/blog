package com.lincz.blog.controller;


import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Authority;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        Set<Authority> authoritySet = new HashSet<>();
        authoritySet.add(new Authority("USER") );
        Account account = new Account(formAccount.getUsername(),formAccount.getPassword(),formAccount.getEmail(), "default.jpg",authoritySet/*,true,true,true,true*/);
        accountService.createAccount(account);
        return "redirect:/";
    }


    // Login form
    @GetMapping(value = "/login")
    public String login() {
        return "Login";
    }
//
//    // Login form with error
//    @RequestMapping(value = "/Login-Error.html")
//    public String loginError(Model model) {
//        model.addAttribute("loginError", true);
//        return "Login.html";
//    }

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
        Page<Article> articlePage = articleService.paginateGetArticlesByCategory(categoryId,pageable);
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Index");
        modelAndView.addObject(articleList);
        return modelAndView;
    }


}
