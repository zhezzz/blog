package com.lincz.blog.controller;


import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/article")
/*,produces = "application/json;charset=UTF-8"*/
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccountService accountService;

    //根据文章id查看文章
    @GetMapping(value = "/details/{articleId}")
    public ModelAndView articleDetails(@PathVariable Long articleId){
        Article article = articleService.getArticleByArticleId(articleId);
        articleService.increasePageView(articleId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ArticleDetails");
        modelAndView.addObject(article);
        return modelAndView;
    }

    //发布文章（返回本文章页面）
    @GetMapping("/post")
    public String postArticleView(){
        return "PostArticle";
    }

    @PostMapping("/post")
    public String postArticle(Article formArticle){
        Article article = new Article(formArticle.getTitle(),formArticle.getSummary(),formArticle.getContent());
        article.setAccount(currentBlogUser());
        articleService.postArticle(article);
        return "redirect:/article/details/"+article.getArticleId();
    }


    //删除文章
    //TODO 使用ajax发送DELETE请求
    @DeleteMapping("/delete/{articleId}")
    public void deleteArticle(@PathVariable Long articleId){
        articleService.deleteArticleByArticleId(articleId);
    }

    //修改文章
    @GetMapping("/update/{articleId}")
    public ModelAndView updateArticlePage(@PathVariable Long articleId){
        Article article = articleService.getArticleByArticleId(articleId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("UpdateArticle");
        modelAndView.addObject(article);
        return modelAndView;
    }
//TODO 使用ajax发送PUT请求
    @PutMapping(value = "/update/{articleId}")
    public String updateArticle(@PathVariable Long articleId, Article formArticle){
        articleService.updateArticle(articleId, formArticle);
        return "redirect:/article/details/"+articleId;
    }


    //查看所有文章（分页）






    public Account currentBlogUser(){
        return accountService.getAccountByUsername(((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }


}
