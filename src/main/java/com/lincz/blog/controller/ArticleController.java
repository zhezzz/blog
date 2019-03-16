package com.lincz.blog.controller;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/article")
public class ArticleController {
	// TODO 限制文章和评论字数，前台ID传入验证跳转到404

	@Autowired
	private ArticleService articleService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private HttpServletRequest request;

	// 获取所有文章文章
	@GetMapping(value = "/")
	public ModelAndView paginateGetAllArticles(
			@PageableDefault(size = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Article> articlePage = articleService.paginateGetAllArticles(pageable);
		List<Article> articleList = articlePage.get().collect(Collectors.toList());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("");
		modelAndView.addObject(articleList);
		return modelAndView;
	}

	// 根据文章id查看文章 //TODO 分页获取评论
	@GetMapping(value = "/{articleId}")
	public ModelAndView articleDetails(@PathVariable Long articleId) {
		Article article = articleService.getArticleByArticleId(articleId);
		articleService.increasePageView(articleId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ArticleDetails");
		modelAndView.addObject(article);
		return modelAndView;
	}

	// 发布文章
	@GetMapping(value = "/post")
	public ModelAndView postArticleView() {
		String currentUsername = request.getUserPrincipal().getName();
		Account currentAccount = accountService.getAccountByUsername(currentUsername);
		Article article = new Article(currentAccount, "标题", "请在此编写文章。", "");
		articleService.createArticle(article);
		ModelAndView modelAndView = new ModelAndView("redirect:/article/update/" + article.getArticleId());
		return modelAndView;
	}

	// 获取修改文章页面
	@GetMapping(value = "/update/{articleId}")
	public ModelAndView updateArticlePage(@PathVariable Long articleId) {
		Article article = articleService.getArticleByArticleId(articleId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("UpdateArticle");
		modelAndView.addObject(article);
		return modelAndView;
	}

	// 修改文章
	@PutMapping(value = "/{articleId}")
	public ModelAndView putArticle(@PathVariable Long articleId, @RequestBody Article articleDTO) {
		articleService.updateArticle(articleId, articleDTO);
		ModelAndView modelAndView = new ModelAndView("redirect:/article/details/" + articleId);
		return modelAndView;
	}

	// 删除文章
	@DeleteMapping(value = "/{articleId}")
	public void delete(@PathVariable Long articleId) {
		articleService.deleteArticleByArticleId(articleId);
	}

	// 上传图片
	@PostMapping(value = "/{articleId}/upload")
	public Response uploadImage(@PathVariable Long articleId, @RequestPart(value = "upload") MultipartFile imageFile)
			throws IOException {
		String originalFileName = imageFile.getOriginalFilename();
		if (imageFile.isEmpty()) {
			return new Response(false, null);
		}
		if (originalFileName.endsWith(".jpg") || originalFileName.endsWith(".png")
				|| originalFileName.endsWith(".gif")) {
			String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
			String classpath = this.getClass().getClassLoader().getResource("").getPath();
			String fileName = UUID.randomUUID().toString() + "." + extension;
			File file = new File(classpath + "data/article-image/" + articleId);
			if (!file.exists()) {
				file.mkdirs();
			}
			imageFile.transferTo(new File(file.toString() + "/" + fileName));
			return new Response(true, request.getContextPath() + "/data/article-image/" + articleId + "/" + fileName);
		}
		else {
			return new Response(false, null);
		}
	}

	// 定义成员内部类供图片上传返回响应json
	private class Response {
		private Boolean uploaded;

		private String url;

		public Response(Boolean uploaded, String url) {
			this.uploaded = uploaded;
			this.url = url;
		}

		public Boolean getUploaded() {
			return uploaded;
		}

		public void setUploaded(Boolean uploaded) {
			this.uploaded = uploaded;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

}
