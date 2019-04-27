package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface ArticleService {

    Article createArticle(Article article);

    Article updateArticle(Long articleId, Article articleDTO) throws InterruptedException;

    Article updateArticleStick(Long articleId, Article articleDTO);

    Article updateArticleStatus(Long articleId, Article articleDTO);

    void deleteArticleByArticleId(Long articleId);

    Article getArticleByArticleId(Long articleId);

    void increasePageView(Long articleId);

    Page<Article> paginateGetAllArticles(Pageable pageable);

    Page<Article> paginateGetArticlesByPublish(boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByAccount(Account account, Pageable pageable);

    List<Article> paginateGetArticlesByAccount(Account account);

    Page<Article> paginateGetArticlesByUsername(String username, Pageable pageable);

    Page<Article> paginateGetArticlesByTitleContianing(String keyword, Pageable pageable);

    Page<Article> paginateGetArticlesByRawContentContianingOrTitleContianing(String keyword, Pageable pageable);

    Page<Article> paginateGetArticlesByAccountAndPublish(Account account, boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByCategoryAndPublish(Category category, boolean publish, Pageable pageable);

    Page<Article> paginateGetArticlesByCategory(Category category, Pageable pageable);

    boolean isArticleExists(Long articleId);

    boolean isArticleExistsAndPublish(Long articleId, boolean publish);

    Page<Article> getArticlesByStick(boolean stick, Pageable pageable);

    List<Article> getRecent10ArticlesByAccount(Account account);

//    Page<Article> paginateGetArticlesByPublishAndCreateDateAfterOrOrderByPageView(boolean publish, LocalDateTime localDateTime, Pageable pageable);

    Map<Month, Long> getArticleQuantityMonthlyByAccount(Account account);

    Map<Month, Long> getArticleQuantityMonthly();

    Long getArticleQuantityByPublishTrueAndAccount(Account account);

    Map<String ,Long> getArticleQuantityByCategory(List<Category> categoryList);

    Long getArticleQuantity();

    Map<Month, Long> getArticlePageviewQuantityByAccount(Account account, List<Article> articleList);

//    Map<Month, Long> getArticlePageviewQuantity(List<Article> articleList);

//    Page<Article> fullTextSearch(Pageable pageable,String keyword);

}
