package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleService {

    Article createArticle(Article article);

    Article updateArticle(Long articleId, Article articleDTO);

    Article updateArticlePublishStatus(Long articleId, Article articleDTO);

    Article updateArticleStatus(Long articleId, Article articleDTO);

    void deleteArticleByArticleId(Long articleId);

    Article getArticleByArticleId(Long articleId);

    void increasePageView(Long articleId);

    Page<Article> paginateGetAllArticles(Pageable pageable);

    Page<Article> paginateGetArticlesByPublish(boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByAccount(Account account, Pageable pageable);

    Page<Article> paginateGetArticlesByAccountAndPublish(Account account, boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByCategoryAndPublish(Category category, boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByCategory(Category category, Pageable pageable);

    Page<Article> paginateGetArticlesByTags(Tag tag, Pageable pageable);

    boolean isArticleExists(Long articleId);

    boolean isArticleExistsAndPublish(Long articleId, boolean publish);

    List<Article> getArticlesByStick(boolean stick);

    List<Article> getRecent10ArticlesByAccount(Account account);

    Page<Article> paginateGetArticlesByCreatedDateBetweenAndPublish(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime, boolean publish, Pageable pageable);

}
