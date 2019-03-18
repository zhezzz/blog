package com.lincz.blog.aspect;

import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CommentService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//判断对文章评论进行删除修改时是否为文章或评论的拥有者
//@Aspect
//@Component
public class OperationAspect {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommentService commentService;


    //获取用户所有评论及信息修改操作切点
//    @Pointcut(value = "execution(public * com.lincz.blog.controller.AccountController.updateAccountAvatar(..))" +
//            " || execution(public * com.lincz.blog.controller.AccountController.updateAccountInfo(..))" +
//            " ||  execution(public * com.lincz.blog.controller.AccountController.accountManagementPage(..))" +
//            " || execution(public * com.lincz.blog.controller.AccountController.accountComments(..))")
//    public void accountPointcut(){
//    }

//    @Pointcut(value = "execution(public * com.lincz.blog.controller.ArticleController.deleteArticle(..))" +
//            " || execution(public * com.lincz.blog.controller.ArticleController.updateArticlePage(..))" +
//            " || execution(public * com.lincz.blog.controller.ArticleController.updateArticle(..))" +
//            " || execution(public * com.lincz.blog.controller.ArticleController.deleteArticle(..))")
//    public void articlePointcut(){
//    }

    @Pointcut(value = "execution(public * com.lincz.blog.controller.CommentContrller.updateCommentPage(..))" +
            " || execution(public * com.lincz.blog.controller.CommentContrller.updateComment(..))" +
            " || execution(public * com.lincz.blog.controller.CommentContrller.deleteComment(..))")
    public void commentPointcut(){
    }


    @Before(value = "accountPointcut()")
    public void accountOperation() throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        String username = request.getRemoteUser();
        Account currentAccount = accountService.getAccountByUsername(username);
        String requestURI = request.getRequestURI();
        //TODO 路径不同,全部方法，Long类型用equals
        String temp = requestURI.replaceAll("/account/.*/","").replace("/","");
        Long  accountId = Long.parseLong(temp);
        Account actualAccount = accountService.getAccountByAccountId(accountId);
        if ((currentAccount == null) || (actualAccount == null) || (actualAccount.getAccountId() != currentAccount.getAccountId())){
            response.sendRedirect("/");
        }
    }

    @Before(value = "articlePointcut()")
    public void articleOperation() throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        String username = request.getRemoteUser();
        Account currentAccount = accountService.getAccountByUsername(username);
        String requestURI = request.getRequestURI();
        String temp = requestURI.replaceAll("/article/.*/","").replace("/","");
        Long  articleId = Long.parseLong(temp);
        Account actualAccount = articleService.getArticleByArticleId(articleId).getAccount();
        if ((currentAccount == null) || (actualAccount == null) || (actualAccount.getAccountId() != currentAccount.getAccountId())){
            response.sendRedirect("/");
        }
    }

    @Before(value = "commentPointcut()")
    public void commentOperation() throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        String username = request.getRemoteUser();
        Account currentAccount = accountService.getAccountByUsername(username);
        String requestURI = request.getRequestURI();
        String temp = requestURI.replaceAll("/comment/.*/","").replace("/","");
        Long  commentId = Long.parseLong(temp);
        Account actualAccount = commentService.getCommentByCommentId(commentId).getAccount();
        if ((currentAccount == null) || (actualAccount == null) || (actualAccount.getAccountId() != currentAccount.getAccountId())){
            response.sendRedirect("/");
        }
    }



}
