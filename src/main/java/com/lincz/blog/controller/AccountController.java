package com.lincz.blog.controller;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Authority;
import com.lincz.blog.entity.Comment;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.AuthorityService;
import com.lincz.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private HttpServletRequest request;

	// 用户个人主页功能，
	@GetMapping(value = "/")
	public ModelAndView accountHomePage(@PathVariable Long accountId) {
		return null;
	}

	// 获取账户信息修改界面
	@GetMapping(value = "/management/{accountId}")
	public ModelAndView accountManagementPage(@PathVariable Long accountId) {
		Account account = accountService.getAccountByAccountId(accountId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("AccountManagenment");
		modelAndView.addObject(account);
		return modelAndView;
	}

	// 修改用户邮箱密码
	@PostMapping(value = "/update/{accountId}/info")
	public String updateAccountInfo(@PathVariable Long accountId, Account formAccount) {
		accountService.updateAccountInfo(accountId, formAccount);
		return "redirect:/";
	}

	// 修改用户权限
	// TODO 暂时使用List，尽量改成Set
	@PostMapping(value = "/update/{accountId}/authority")
	public Account updateAccountAuthority(@PathVariable Long accountId, List<Long> authorityIdList) {
		Set<Authority> authorities = new HashSet<>();
		for (Long authorityId : authorityIdList) {
			Authority authority = authorityService.getAuthorityByAuthorityId(authorityId);
			if (authority != null) {
				authorities.add(authority);
			}
		}
		return accountService.updateAccountAuthority(accountId, authorities);
	}

	// 修改头像
	@PostMapping(value = "/update/{accountId}/avatar")
	public void updateAccountAvatar(@PathVariable Long accountId, @RequestParam(value = "avatar") MultipartFile avatar) throws IOException {
		String fileName = avatar.getOriginalFilename();
		if (fileName.endsWith(".jpg") || fileName.endsWith(".png")){
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String classpath = this.getClass().getClassLoader().getResource("").getPath();
            String avatarFileName = accountId + "." + extension;
            File file =new File(classpath + "data/avatar/");
            if (!file.exists()){
                file.mkdirs();
            }
            //TODO resize
            avatar.transferTo(new File(file.toString() + "/" + avatarFileName));
		}

	}

	// 获取用户所有评论（分页）
	@GetMapping(value = "/{accountId}/comments")
	public ModelAndView accountComments(@PathVariable Long accountId,
			@PageableDefault(size = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
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
			@PageableDefault(size = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		Account account = accountService.getAccountByAccountId(accountId);
		Page<Article> articlePage = articleService.paginateGetArticlesByAccount(account, pageable);
		List<Article> articleList = articlePage.get().collect(Collectors.toList());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("AccountArticles");
		modelAndView.addObject(articleList);
		return modelAndView;
	}

	// 验证用户名唯一性
	@PostMapping(value = "/verify_username_uniqueness")
	public boolean verifyUniqueness(String username) {
		if (accountService.getAccountByUsername(username) == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
