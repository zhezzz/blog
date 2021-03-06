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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/comment")
public class CommentContrller {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "/management")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    public ModelAndView commentManagementPage(@PageableDefault(sort = {"commentId"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> commentPage = commentService.paginateGetAllComments(pageable);
        List<Comment> commentList = commentPage.getContent();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/admin/CommentManagement");
        modelAndView.addObject("commentPage", commentPage);
        modelAndView.addObject("commentList", commentList);
        return modelAndView;
    }

    @GetMapping(value = "/mymanagement")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public ModelAndView myCommentManagementPage(@PageableDefault(sort = {"commentId"}, direction = Sort.Direction.DESC) Pageable pageable) {
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        Page<Comment> commentPage = commentService.paginateGetCommetsByAccount(currentAccount, pageable);
        List<Comment> commentList = commentPage.getContent();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/user/MyCommentManagement");
        modelAndView.addObject("commentPage", commentPage);
        modelAndView.addObject("commentList", commentList);
        return modelAndView;
    }

    @GetMapping(value = "/receivemanagement")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public ModelAndView myReceiveCommentManagementPage(@PageableDefault(sort = {"commentId"}, direction = Sort.Direction.DESC) Pageable pageable) {
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        List<Article> articleList = articleService.getArticlesByAccount(currentAccount);
        Page<Comment> commentPage = commentService.paginateGetReceiveCommets(articleList, pageable);
        List<Comment> commentList = commentPage.getContent();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/user/ReceiveCommentManagement");
        modelAndView.addObject("commentPage", commentPage);
        modelAndView.addObject("commentList", commentList);
        return modelAndView;
    }

    // 发表评论
    @PostMapping(value = "/{articleId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    //TODO 前台json
    public ModelAndView postComment(@PathVariable Long articleId, Comment commentDTO) {
        Article article = articleService.getArticleByArticleId(articleId);
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        commentDTO.setArticle(article);
        commentDTO.setAccount(currentAccount);
        if (commentDTO.getContent().length() >= 5 && commentDTO.getContent().length() <= 200) {
            commentService.createComment(commentDTO);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/article/details/" + articleId);
        return modelAndView;
    }

    // 删除评论
    @DeleteMapping(value = "/{commentId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteCommentByCommentId(commentId);
    }

    // 修改评论
    @PutMapping(value = "/{commentId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public void updateComment(@PathVariable Long commentId, @RequestBody Comment commentDTO) {
        Comment comment = commentService.updateComment(commentId, commentDTO);
    }

}
