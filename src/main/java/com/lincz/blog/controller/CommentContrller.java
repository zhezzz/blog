package com.lincz.blog.controller;


import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CommentService;
import com.lincz.blog.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping(value = "/comment")
public class CommentContrller {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;

    @PostMapping(value = "/post/{articleId}")
    public String postComment(@PathVariable Long articleId, Comment formComment){
        Article article = articleService.getArticleByArticleId(articleId);
        Comment comment = new Comment(formComment.getContent());
        comment.setArticle(article);
        comment.setAccount(currentAccount());
        commentService.createComment(comment);
        article.getComments().add(comment);
        return "redirect:/article/details/"+articleId;
    }



    public Account currentAccount(){
        return accountService.getAccountByUsername(((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }
}
