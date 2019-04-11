package com.lincz.blog.controller;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    // TODO 设计任意访问的用户主页，还有用户自己管理页面，和admin管理页面类似，验证传入id，跳转404

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;


    @Autowired
    private HttpServletRequest request;

    // 获取账户信息修改界面
    @GetMapping(value = "/management")
    @PreAuthorize("hasAnyRole('ROOT')")
    public ModelAndView accountManagementPage(@PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Account> accountPage = accountService.paginateGetAllAccount(pageable);
        List<Account> accountList = accountPage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/admin/AccountManagement");
        modelAndView.addObject("accountPage", accountPage);
        modelAndView.addObject("accountList", accountList);
        return modelAndView;
    }


    // 获取账户信息修改界面
    @GetMapping(value = "/mymanagement")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public ModelAndView myAccountManagementPage() {
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/user/MyAccountSetting");
        modelAndView.addObject(currentAccount);
        return modelAndView;
    }


    // 根据id获取用户主页，
    @GetMapping(value = "/homepage/{accountId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public ModelAndView getAccountHomePame(@PathVariable Long accountId) {
        Account account = accountService.getAccountByAccountId(accountId);
        List<Article> recentArticlesList = articleService.getRecent10ArticlesByAccount(account);
        List<Comment> recentCommentsList = commentService.getRecent10CommentsByAccount(account);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(recentArticlesList);
        modelAndView.addObject(recentCommentsList);
        modelAndView.setViewName("AccountHomePage");
        return modelAndView;
    }

    // 创建新用户
    @PostMapping(value = "")
    public ModelAndView createAccount(Account accountDTO) {
        ModelAndView modelAndView = new ModelAndView();
        if (accountService.getAccountByUsername(accountDTO.getUsername()) != null) {
            modelAndView.setViewName("redirect:/register?occupy=yes");
            return modelAndView;
        }
        //TODO 更多校验
        accountService.createAccount(accountDTO);
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    // 修改用户状态
    @PutMapping(value = "/{accountId}/status")
    @PreAuthorize("hasAnyRole('ROOT')")
    public void updateAccountStatus(@PathVariable Long accountId, @RequestBody Account accountDTO) {
        if (accountDTO.getRole().equals("ROOT") || accountDTO.getRole().equals("ADMIN") || accountDTO.getRole().equals("USER")) {
            accountService.updateAccountStatus(accountId, accountDTO);
        }
    }

    // 修改个人信息
    @PutMapping(value = "/{accountId}/info")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public void updateAccount(@PathVariable Long accountId, @RequestBody Account accountDTO) {
        accountService.updateAccountInfo(accountId, accountDTO);
    }

    // 删除账号
    @DeleteMapping(value = "/{accountId}")
    @PreAuthorize("hasAnyRole('ROOT')")
    public void deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccountByAccountId(accountId);
    }

    // 修改头像
    @PostMapping(value = "/{accountId}/avatar")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public Response updateAccountAvatar(@PathVariable Long accountId, @RequestParam(value = "avatar") MultipartFile avatar) throws IOException {
        if (!avatar.isEmpty() || avatar.getOriginalFilename().endsWith(".JPG") || avatar.getOriginalFilename().endsWith(".PNG") || avatar.getOriginalFilename().endsWith(".GIF") || avatar.getOriginalFilename().endsWith(".jpg") || avatar.getOriginalFilename().endsWith(".png") || avatar.getOriginalFilename().endsWith(".gif")) {
            accountService.updateAccountAvatar(accountId, avatar);
            return new Response(null);
        }
        return new Response("上传失败");
    }

    // 获取用户所有评论（分页）
    @GetMapping(value = "/{accountId}/comments")
    public ModelAndView accountComments(@PathVariable Long accountId,
                                        @PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Account account = accountService.getAccountByAccountId(accountId);
        Page<Comment> commentPage = commentService.paginateGetCommetsByAccount(account, pageable);
        List<Comment> commentList = commentPage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("AccountComments");
        modelAndView.addObject(commentList);
        return modelAndView;
    }

    // 获取用户所有文章（分页）
    @GetMapping(value = "/{accountId}/articles")
    public ModelAndView accountArticles(@PathVariable Long accountId,
                                        @PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Account account = accountService.getAccountByAccountId(accountId);
        Page<Article> articlePage = articleService.paginateGetArticlesByAccount(account, pageable);
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("AccountHomePage");
        modelAndView.addObject(articleList);
        return modelAndView;
    }

    private class Response {
        private String error;

        public Response(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

}
