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
	private AuthorityService authorityService;

	@Autowired
	private HttpServletRequest request;

	// 分页获取所有用户
	@GetMapping(value = "/")
	//	@PreAuthorize("hasAuthority('获取所有账号')")
	public ModelAndView paginateGetAllAccount(
			@PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Account> accountPage = accountService.paginateGetAllAccount(pageable);
		List<Account> accountList = accountPage.get().collect(Collectors.toList());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("");
		modelAndView.addObject(accountList);
		return modelAndView;
	}

	// 根据id获取用户主页，若是本人跳转到管理页面，否则跳转到游客浏览页面
	@GetMapping(value = "/{accountId}")
	public ModelAndView getAccountHomePame(@PathVariable Long accountId) {
		Account account = accountService.getAccountByAccountId(accountId);
		String currentUsername = request.getUserPrincipal().getName();
		Account currentAccount = accountService.getAccountByUsername(currentUsername);
		List<Article> recentArticlesList = articleService.getRecent10ArticlesByAccount(account);
		List<Comment> recentCommentsList = commentService.getRecent10CommentsByAccount(account);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(recentArticlesList);
		modelAndView.addObject(recentCommentsList);
		if (currentAccount.getAccountId().equals(accountId)){
			modelAndView.setViewName("AccountOverview");
		}
		else{
			modelAndView.setViewName("AccountHomePage");
		}
		return modelAndView;
	}

	// 创建新用户
	@PostMapping(value = "/")
	public Account createAccount(@RequestBody Account accountDTO) {
		Account account = accountService.createAccount(accountDTO);
		return account;
	}

	// 修改用户信息
	@PutMapping(value = "/{accountId}")
	//	@PreAuthorize("hasAuthority('修改账号')")
	public String updateAccount(@PathVariable Long accountId, @RequestBody Account accountDTO) {
		accountService.updateAccountInfo(accountId, accountDTO);
		return "redirect:/";
	}

	// 删除账号
	@DeleteMapping(value = "/{accountId}")
	//	@PreAuthorize("hasAuthority('删除账号')")
	public void deleteAccount(@PathVariable Long accountId) {
		accountService.deleteAccountByAccountId(accountId);
	}

	// 获取账户信息修改界面
	@GetMapping(value = "/{accountId}/management")
	public ModelAndView accountManagementPage(@PathVariable Long accountId) {
		Account account = accountService.getAccountByAccountId(accountId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("AccountManagenment");
		modelAndView.addObject(account);
		return modelAndView;
	}

	// 修改用户权限
	// TODO 待定
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

	//获取用户权限
	@GetMapping(value = "/getauth")
	public Collection<? extends GrantedAuthority> getget(){
		Account account = accountService.getAccountByAccountId(Long.valueOf(1));
		Collection<? extends GrantedAuthority> authorities = account.getAuthorities();
		return authorities;
	}

	// 修改头像 //TODO 或者PUT？
	@PostMapping(value = "/{accountId}/avatar")
	//	@PreAuthorize("hasAuthority('修改账号')")
	public void updateAccountAvatar(@PathVariable Long accountId, @RequestParam(value = "avatar") MultipartFile avatar)
			throws IOException {
		if (!avatar.isEmpty()){
			String fileName = avatar.getName();
			if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
				String classpath = this.getClass().getClassLoader().getResource("").getPath();
				String avatarFileName = accountId + ".png";
				File file = new File(classpath + "data/account/avatar/");
				if (!file.exists()) {
					file.mkdirs();
				}
				avatar.transferTo(new File(file.toString() + "/" + avatarFileName));
			}
		}
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
