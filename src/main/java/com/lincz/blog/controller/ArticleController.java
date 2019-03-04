package com.lincz.blog.controller;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import org.jsoup.Jsoup;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {
	// TODO 弃用直接post新文章，用户发起请求，后台创建一篇新文章返回给前台编辑，，前台具有编辑功能和保存功能，发布功能

	@Autowired
	private ArticleService articleService;

	@Autowired
	private AccountService accountService;

	// 根据文章id查看文章
	@GetMapping(value = "/details/{articleId}")
	public ModelAndView articleDetails(@PathVariable Long articleId) {
		Article article = articleService.getArticleByArticleId(articleId);
		articleService.increasePageView(articleId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ArticleDetails");
		modelAndView.addObject(article);
		return modelAndView;
	}

	//将文章加入用户收藏夹
	@PostMapping(value = "/addToFavorites/{articleId}")
	public void addArticleToFavorites(@PathVariable Long articleId, HttpServletRequest request){
		Article article = articleService.getArticleByArticleId(articleId);
		String currentUsername = request.getUserPrincipal().getName();
		Account account = accountService.getAccountByUsername(currentUsername);
		if (!account.getFavoriteArticles().contains(article)){
			account.getFavoriteArticles().add(article);
		}
	}

	// 发布文章
	@GetMapping(value = "/post")
	public String postArticleView(HttpServletRequest request) {
		String currentUsername = request.getUserPrincipal().getName();
		Account account = accountService.getAccountByUsername(currentUsername);
		Article article = new Article(account, "标题", "请在此编写文章。", "", Long.valueOf(0), false);
		articleService.createArticle(article);
		return "redirect:/article/update/" + article.getArticleId();
	}

	// 删除文章
	@ResponseBody
	@PostMapping(value = "/delete/{articleId}")
	public void deleteArticle(@PathVariable Long articleId) {
		articleService.deleteArticleByArticleId(articleId);
	}

	// 修改文章
	@GetMapping(value = "/update/{articleId}")
	public ModelAndView updateArticlePage(@PathVariable Long articleId) {
		Article article = articleService.getArticleByArticleId(articleId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("UpdateArticle");
		modelAndView.addObject(article);
		return modelAndView;
	}

	@PostMapping(value = "/update/{articleId}")
	public String updateArticle(@PathVariable Long articleId, Article formArticle) {
		String rawContent = Jsoup.parse(formArticle.getContent()).text();
		formArticle.setRawContent(rawContent);
		articleService.updateArticle(articleId, formArticle);
		return "redirect:/article/details/" + articleId;
	}

	public Account currentAccount() {
		return accountService.getAccountByUsername(
				((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
	}

	// 上传图片
	@RequestMapping(value = "/upload")
	public Response uploadImage(@RequestPart("upload") MultipartFile imageFile, HttpServletRequest request)
			throws IOException {
		String imageFileOriginalFilename = imageFile.getOriginalFilename();
		String fileNameExtension = imageFileOriginalFilename.substring(imageFileOriginalFilename.lastIndexOf("."));
		String localFileName = UUID.randomUUID().toString() + fileNameExtension;
		imageFile.transferTo(Paths.get("data/article/image/" + localFileName));
		// TODO 路径问题
		String url = "data/article/image/" + localFileName;
		return new Response(true, url);

	}

	// 定义成员内部类供图片上传返回响应json
	private class Response {
		private Boolean uploaded;

		private String url;

		public Response(Boolean uploaded, String url) {
			this.uploaded = uploaded;
			this.url = url;
		}
	}

}
