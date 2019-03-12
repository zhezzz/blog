package com.lincz.blog.controller;

import com.lincz.blog.entity.*;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private AccountService accountService;

	@GetMapping(value = "/")
	public ModelAndView index() {
		return new ModelAndView("Index");
	}

	@GetMapping(value = "/register")
	public ModelAndView registerPage() {
		return new ModelAndView("Register");
	}

	// 注册用户
	// TODO 加密存储密码
	@PostMapping(value = "/register")
	public String createAccount(Account formAccount) {
		Account account = new Account(formAccount.getUsername(), formAccount.getPassword(), formAccount.getEmail());
		accountService.createAccount(account);
		return "redirect:/";
	}

	@GetMapping(value = "/all")
	public ModelAndView getAllArticles(
			@PageableDefault(size = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Article> articlePage = articleService.paginateGetAllArticlesByPublish(true, pageable);
		List<Article> articleList = articlePage.get().collect(Collectors.toList());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("Index");
		modelAndView.addObject(articleList);
		return modelAndView;
	}

}
