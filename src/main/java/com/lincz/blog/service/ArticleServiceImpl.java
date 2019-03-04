package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.entity.Tag;
import com.lincz.blog.repository.ArticleRepository;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleRepository articleRepository;

	@Override
	public Article createArticle(Article article) {
		articleRepository.save(article);
		return article;
	}

	@Override
	public Article updateArticle(Long articleId, Article formArticle) {
		Article article = articleRepository.findById(articleId).orElse(null);
		article.setTitle(formArticle.getTitle());
		article.setContent(formArticle.getContent());
		article.setRawContent(formArticle.getRawContent());
		return article;
	}

	@Override
	public void deleteArticleByArticleId(Long articleId) {
		articleRepository.deleteById(articleId);
	}

	@Override
	public Article getArticleByArticleId(Long articleId) {
		return articleRepository.findById(articleId).orElse(null);
	}

	@Override
	public void increasePageView(Long articleId) {
		Article article = articleRepository.findById(articleId).orElse(null);
		article.setPageView(article.getPageView() + 1);
	}

	@Override
	public Page<Article> paginateGetAllArticles(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	@Override
	public Page<Article> paginateGetArticlesByAccount(Account account, Pageable pageable) {
		Page<Article> articles = articleRepository.findArticlesByAccount(account, pageable);
		return articles;
	}

	@Override
	public Page<Article> paginateGetAllArticlesByIsPublic(Boolean isPublic, Pageable pageable) {
		Page<Article> articles = articleRepository.findAllByIsPublic(isPublic, pageable);
		return articles;
	}

	@Override
	public Page<Article> paginateGetArticlesByCategory(Category category, Pageable pageable) {
		return articleRepository.findArticlesByCategory(category, pageable);
	}

	@Override
	public Page<Article> paginateGetArticlesByTag(Tag tag, Pageable pageable) {
		return articleRepository.findArticlesByTagsExists(tag, pageable);
	}
}
