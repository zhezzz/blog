package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {

    Article createArticle(Article article);

    Article updateArticle(Long articleId, Article articleDTO);

    Article updateArticleStick(Long articleId, Article articleDTO);

    Article updateArticleStatus(Long articleId, Article articleDTO);

    void deleteArticleByArticleId(Long articleId);

    Article getArticleByArticleId(Long articleId);

    void increasePageView(Long articleId);

    Page<Article> paginateGetAllArticles(Pageable pageable);

    Page<Article> paginateGetArticlesByPublish(boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByAccount(Account account, Pageable pageable);

    Page<Article> paginateGetArticlesByUsername(String username, Pageable pageable);

    Page<Article> paginateGetArticlesByTitleContianing(String keyword, Pageable pageable);

    Page<Article> paginateGetArticlesByAccountAndPublish(Account account, boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByCategoryAndPublish(Category category, boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByCategory(Category category, Pageable pageable);

    boolean isArticleExists(Long articleId);

    boolean isArticleExistsAndPublish(Long articleId, boolean publish);

    Page<Article> getArticlesByStick(boolean stick, Pageable pageable);

    List<Article> getRecent10ArticlesByAccount(Account account);

//    Page<Article> paginateGetArticlesByPublishAndCreateDateAfterOrOrderByPageView(boolean publish, LocalDateTime localDateTime, Pageable pageable);

}
