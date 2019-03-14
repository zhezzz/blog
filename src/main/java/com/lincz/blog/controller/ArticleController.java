package com.lincz.blog.controller;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private HttpServletRequest request;

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
	public void addArticleToFavorites(@PathVariable Long articleId){
		Article article = articleService.getArticleByArticleId(articleId);
		String currentUsername = request.getUserPrincipal().getName();
		Account currentAccount = accountService.getAccountByUsername(currentUsername);
		if (!currentAccount.getFavoriteArticles().contains(article)){
			currentAccount.getFavoriteArticles().add(article);
		}
	}

	// 发布文章
	@GetMapping(value = "/post")
	public String postArticleView() {
		String currentUsername = request.getUserPrincipal().getName();
		Account currentAccount = accountService.getAccountByUsername(currentUsername);
		Article article = new Article(currentAccount, "标题", "请在此编写文章。", "");
		articleService.createArticle(article);
		return "redirect:" + request.getContextPath() + "/article/update/" + article.getArticleId();
	}

	//测试putmapping
	@PutMapping(value = "/{id}")
	public String putArticle(@PathVariable Long id, Article formArticle){
		String rawContent = Jsoup.parse(formArticle.getContent()).text();
		formArticle.setRawContent(rawContent);
		articleService.updateArticle(id, formArticle);
		return "redirect:/article/details/" + id;
	}

	//测试deletemapping
	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public void delete(@PathVariable Long id) {
		articleService.deleteArticleByArticleId(id);
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

	// 上传图片
	@RequestMapping(value = "/upload/{articleId}")
	@ResponseBody
	public Response uploadImage(@PathVariable Long articleId, @RequestPart(value = "upload") MultipartFile imageFile)
			throws IOException {
		String originalFileName = imageFile.getOriginalFilename();
		if (imageFile.isEmpty()){
			return new Response(false, null);
		}
		if (originalFileName.endsWith(".jpg") || originalFileName.endsWith(".png")){
			String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
			String classpath = this.getClass().getClassLoader().getResource("").getPath();
			String fileName = UUID.randomUUID().toString() + "." + extension;
			File file =new File(classpath + "data/article-image/" + articleId);
			if (!file.exists()){
				file.mkdirs();
			}
			imageFile.transferTo(new File(file.toString() + "/" + fileName));
			return new Response(true, "/data/article-image/" + articleId + "/" + fileName);
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
