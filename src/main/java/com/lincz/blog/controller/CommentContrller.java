package com.lincz.blog.controller;


import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/comment")
public class CommentContrller {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;

    //发表评论
    @PostMapping(value = "/post/{articleId}")
    public String postComment(@PathVariable Long articleId, Comment formComment){
        Article article = articleService.getArticleByArticleId(articleId);
        Comment comment = new Comment(formComment.getContent());
        comment.setArticle(article);
        comment.setAccount(currentAccount());
        commentService.createComment(comment);
        comment.setArticle(article);
        return "redirect:/article/details/"+articleId;
    }

    //删除评论

    @DeleteMapping(value = "/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId){
        commentService.deleteCommentByCommentId(commentId);
    }

    //修改评论页面
    @GetMapping(value = "/update/{commentId}")
    public ModelAndView updateCommentPage(@PathVariable Long commentId){
        Comment comment = commentService.getCommentByCommentId(commentId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("UpdateComment");
        modelAndView.addObject(comment);
        return modelAndView;
    }

    //修改评论
    //TODO 使用ajax发送PUT请求
    @PutMapping(value = "/update/{commentId}")
    public String updateComment(@PathVariable Long commentId, Comment formComment){
        Comment comment = commentService.updateComment(commentId, formComment);
        return "redirect:/article/details/"+comment.getArticle().getArticleId();
    }

    @GetMapping(value = "/all")
    private ModelAndView getAllComments(@PageableDefault(size = 10,sort = { "createDate" }, direction = Sort.Direction.DESC) Pageable pageable){
        Page<Comment> commentList = commentService.getAllComments(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("AccountComments");
        modelAndView.addObject(commentList);
        return modelAndView;
    }





    public Account currentAccount(){
        return accountService.getAccountByUsername(((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }
}
