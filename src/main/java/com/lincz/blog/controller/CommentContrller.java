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
import java.util.stream.Collectors;

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
    private ModelAndView commentManagementPage(@PageableDefault(sort = {"commentId"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> commentPage = commentService.paginateGetAllComments(pageable);
        List<Comment> commentList = commentPage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/admin/CommentManagement");
        modelAndView.addObject("commentList", commentList);
        modelAndView.addObject("commentPage", commentPage);
        return modelAndView;
    }

    @GetMapping(value = "/mymanagement")
    private ModelAndView myCommentManagementPage(@PageableDefault(sort = {"commentId"}, direction = Sort.Direction.DESC) Pageable pageable) {
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        Page<Comment> commentPage = commentService.paginateGetCommetsByAccount(currentAccount,pageable);
        List<Comment> commentList = commentPage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/admin/CommentManagement");
        modelAndView.addObject("commentList", commentList);
        modelAndView.addObject("commentPage", commentPage);
        return modelAndView;
    }

    @GetMapping(value = "/")
    //	@PreAuthorize("hasAuthority('获取所有评论')")
    private ModelAndView getAllComments(
            @PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> commentList = commentService.paginateGetAllComments(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("AccountComments");
        modelAndView.addObject(commentList);
        return modelAndView;
    }

    // 发表评论
    @PostMapping(value = "/{articleId}")
    //	@PreAuthorize("hasAuthority('发布评论')")
    public String postComment(@PathVariable Long articleId, @RequestBody Comment commentDTO) {
        Article article = articleService.getArticleByArticleId(articleId);
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        commentDTO.setArticle(article);
        commentDTO.setAccount(currentAccount);
        if (commentDTO.getContent().length() >= 30 && commentDTO.getContent().length() <= 200) {
            commentService.createComment(commentDTO);
        }
        return "redirect:/article/details/" + articleId;
    }

    // 删除评论
    @DeleteMapping(value = "/{commentId}")
    //	@PreAuthorize("hasAuthority('删除评论')")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteCommentByCommentId(commentId);
    }

    // 修改评论页面
    @GetMapping(value = "/update/{commentId}")
    //	@PreAuthorize("hasAuthority('修改评论')")
    public ModelAndView updateCommentPage(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentByCommentId(commentId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("UpdateComment");
        modelAndView.addObject(comment);
        return modelAndView;
    }

    // 修改评论
    @PutMapping(value = "/{commentId}")
    //	@PreAuthorize("hasAuthority('修改评论')")
    public String updateComment(@PathVariable Long commentId, @RequestBody Comment commentDTO) {
        Comment comment = commentService.updateComment(commentId, commentDTO);
        return "redirect:/article/details/" + comment.getArticle().getArticleId();
    }

    // 根据id查询评论
    @GetMapping(value = "/{commentId}")
    public Comment getCommentByCommentId(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentByCommentId(commentId);
        return comment;
    }

}
