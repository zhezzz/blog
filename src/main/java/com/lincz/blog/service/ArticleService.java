package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

	Article createArticle(Article article);

	Article updateArticle(Long articleId, Article articleDTO);

	void deleteArticleByArticleId(Long articleId);

	Article getArticleByArticleId(Long articleId);

	void increasePageView(Long articleId);

	Page<Article> paginateGetAllArticles(Pageable pageable);

	Page<Article> paginateGetArticlesByPublish(boolean publish, Pageable pageable);

	Page<Article> paginateGetArticlesByAccount(Account account, Pageable pageable);

	Page<Article> paginateGetArticlesByAccountAndPublish(Account account, boolean publish, Pageable pageable);

	Page<Article> paginateGetArticlesByCategoryAndPublish(Category category, boolean publish, Pageable pageable);

	Page<Article> paginateGetArticlesByCategory(Category category, Pageable pageable);

// Page<Article> paginateGetArticlesByTag(Tag tag, Pageable pageable);

	boolean isArticleExists(Long articleId);

	boolean isArticleExistsAndPublish(Long articleId, boolean publish);

	Page<Article> paginateGetArticlesByStickTrue(Pageable pageable);

}
