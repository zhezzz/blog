package com.lincz.blog.service;

import com.lincz.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Article createArticle(Article article);

    Article updateArticle(Long articleId, Article formArticle);

    void deleteArticleByArticleId(Long articleId);

    Article getArticleByArticleId(Long articleId);

    void increasePageView(Long articleId);

    Page<Article> paginateGetAllArticles(Pageable pageable);

    Page<Article> paginateGetArticlesByAccount(Long accountId, Pageable pageable);

    Page<Article> paginateGetArticlesByCategory(Long categoryId, Pageable pageable);


}
