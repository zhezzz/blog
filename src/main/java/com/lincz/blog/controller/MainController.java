package com.lincz.blog.controller;

import com.lincz.blog.entity.*;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CategoryService;
import com.lincz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

	@Autowired
	private TagService tagService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private AccountService accountService;

	@GetMapping(value = "/index")
	public ModelAndView index(
			@PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		List<Tag> tagList = tagService.getAllTag();
		List<Article> stickArticleList = articleService.getStickArticles();
		Page<Article> articlePage = articleService.paginateGetArticlesByPublish(true, pageable);
		List<Article> articleList = articlePage.get().collect(Collectors.toList());
		List<Category> categoryList = categoryService.getAllCategory();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("Index");
		modelAndView.addObject("categoryList", categoryList);
		modelAndView.addObject("articlePage", articlePage);
		modelAndView.addObject("tagList", tagList);
		modelAndView.addObject("articleList", articleList);
		modelAndView.addObject("stickArticleList", stickArticleList);
		return modelAndView;
	}

	public ModelAndView hotest(
			@PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime currentMonthStartTime = now.minusDays(now.getDayOfMonth() - 1).minusHours(now.getHour()).minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano() + 1);
		List<Tag> tagList = tagService.getAllTag();
		List<Article> stickArticleList = articleService.getStickArticles();
		Page<Article> articlePage = articleService.paginateGetArticlesByPublish(true, pageable);
		List<Article> articleList = articlePage.get().collect(Collectors.toList());
		List<Category> categoryList = categoryService.getAllCategory();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("Index");
		modelAndView.addObject("categoryList", categoryList);
		modelAndView.addObject("articlePage", articlePage);
		modelAndView.addObject("tagList", tagList);
		modelAndView.addObject("articleList", articleList);
		modelAndView.addObject("stickArticleList", stickArticleList);
		return modelAndView;
	}

	@GetMapping(value = "/login")
	public ModelAndView loginPage(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("Login");
		return modelAndView;
	}

	@GetMapping(value = "/register")
	public ModelAndView registerPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("Register");
		return modelAndView;
	}

	// 注册用户
	@PostMapping(value = "/register")
	public String createAccount(Account accountDTO) {
		if (accountService.getAccountByUsername(accountDTO.getUsername()) != null){
			return "redirect:/register?occupy=yes";
		}
		//TODO 更多校验
		Account account = new Account(accountDTO.getUsername(), accountDTO.getPassword(), accountDTO.getEmail());
		accountService.createAccount(account);
		return "redirect:/";
	}

}
