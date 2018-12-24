package com.lincz.blog.service;

import com.lincz.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Article postArticle(Article article);

    Article updateArticle(Long articleId, Article formArticle);

    void deleteArticleByArticleId(Long articleId);

    Article getArticleByArticleId(Long articleId);

    void increasePageView(Long articleId);

    Page<Article> fullTextSearch(Pageable pageable, String keyword);

    Page<Article> paginateGetAllArticles(Pageable pageable);

    Page<Article> paginateGetArticlesByAccountId(Long accountId, Pageable pageable);

}
