package com.lincz.blog.controller;


import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MainController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("Index");
    }

    @GetMapping("/register")
    public ModelAndView register(){
        return new ModelAndView("Register");
    }

    //注册用户
    @PostMapping("/register")
    public Account createAccount(Account formAccount) throws Exception{
        Account account = new Account(formAccount.getUsername(),formAccount.getPassword(),formAccount.getEmail());
        return accountService.createAccount(account);
    }


//    // Login form
//    @RequestMapping("/Login.html")
//    public String login() {
//        return "Login.html";
//    }
//
//    // Login form with error
//    @RequestMapping("/Login-Error.html")
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


}
