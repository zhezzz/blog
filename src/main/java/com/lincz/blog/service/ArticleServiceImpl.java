package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.repository.ArticleRepository;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.query.dsl.QueryBuilder;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository articleRepository;

//    @PersistenceUnit
//    private EntityManagerFactory entityManagerFactory;

    @Override
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(Long articleId, Article articleDTO) throws InterruptedException{
        Article article = articleRepository.findById(articleId).orElse(null);
        String rawContent = Jsoup.parse(articleDTO.getContent()).text();
        articleDTO.setRawContent(rawContent);
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setRawContent(articleDTO.getRawContent());
        article.setPublish(articleDTO.isPublish());
        article.setCategory(articleDTO.getCategory());
        articleRepository.save(article);
//        EntityManager em = entityManagerFactory.createEntityManager();
//        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
//        fullTextEntityManager.createIndexer().startAndWait();
        return article;
    }

    @Override
    public Article updateArticleStick(Long articleId, Article articleDTO) {
        Article article = articleRepository.findById(articleId).orElse(null);
        article.setStick(articleDTO.isStick());
        return articleRepository.save(article);
    }


    @Override
    public Article updateArticleStatus(Long articleId, Article articleDTO) {
        Article article = articleRepository.findById(articleId).orElse(null);
        article.setPublish(articleDTO.isPublish());
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticleByArticleId(Long articleId) {
        articleRepository.deleteById(articleId);
        File dir = new File("/tmp/data/article/" + articleId + "/image");
        if (dir.exists()){
            File[] files = dir.listFiles();
            if (files.length != 0){
                for (File file : files) {
                    file.delete();
                }
            }
            dir.delete();
        }
    }

    @Override
    public Article getArticleByArticleId(Long articleId) {
        return articleRepository.findById(articleId).orElse(null);
    }

    @Override
    public void increasePageView(Long articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);
        article.setPageView(article.getPageView() + 1);
        articleRepository.save(article);
    }

    @Override
    public Page<Article> paginateGetAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public Page<Article> paginateGetArticlesByAccount(Account account, Pageable pageable) {
        return articleRepository.findAllByAccount(account, pageable);
    }

    @Override
    public List<Article> getArticlesByAccount(Account account) {
        return articleRepository.findAriclesByAccount(account);
    }

    @Override
    public Map<Month, Long> getArticlePageviewQuantityByAccount(Account account, List<Article> articleList) {
        return null;
    }

    @Override
    public Page<Article> paginateGetArticlesByUsername(String username, Pageable pageable) {
        return articleRepository.findAllByAccount_Username(username, pageable);
    }

    @Override
    public Page<Article> paginateGetArticlesByTitleContianing(String keyword, Pageable pageable) {
        return articleRepository.findAllByTitleContaining(keyword, pageable);
    }

    @Override
    public Page<Article> paginateGetArticlesByRawContentContianingOrTitleContianing(String keyword, Pageable pageable) {
        return articleRepository.findAllByRawContentContainingOrTitleContaining(keyword, keyword, pageable);
    }

    @Override
    public Page<Article> paginateGetArticlesByPublish(Boolean publish, Pageable pageable) {
        Page<Article> articles = articleRepository.findAllByPublish(publish, pageable);
        return articles;
    }

    @Override
    public Page<Article> paginateGetArticlesByCategory(Category category, Pageable pageable) {
        return articleRepository.findAllByCategory(category, pageable);
    }

    @Override
    public Boolean isArticleExists(Long articleId) {
        return articleRepository.existsById(articleId);
    }

    @Override
    public Page<Article> paginateGetArticlesByAccountAndPublish(Account account, Boolean publish, Pageable pageable) {
        return articleRepository.findAllByAccountAndPublish(account, publish, pageable);
    }

    @Override
    public Page<Article> paginateGetArticlesByCategoryAndPublish(Category category, Boolean publish, Pageable pageable) {
        return articleRepository.findAllByCategoryAndPublish(category, publish, pageable);
    }

    @Override
    public Boolean isArticleExistsAndPublish(Long articleId, Boolean publish) {
        if (!articleRepository.existsById(articleId)) {
            return false;
        }
        Article article = articleRepository.findById(articleId).orElse(null);
        if (!article.isPublish()) {
            return false;
        }
        return true;
    }

    @Override
    public Page<Article> getArticlesByStick(Boolean stick, Pageable pageable) {
        return articleRepository.findAllByStick(stick, pageable);
    }

    @Override
    public Page<Article> getHotArticles(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        int years = now.getYear();
        int month = now.getMonthValue();
        LocalDateTime monthStartLocalDateTime = LocalDateTime.of(years, Month.of(month), 1, 0, 0, 0, 0);
        return articleRepository.findAllByPublishTrueAndCreateDateBetweenOrderByPageViewDesc(monthStartLocalDateTime,now,pageable);
    }

    @Override
    public List<Article> getRecent10ArticlesByAccount(Account account) {
        return articleRepository.findTop10ByAccountOrderByCreateDateDesc(account);
    }

    @Override
    public Map<Month, Long> getArticleQuantityMonthlyByAccount(Account account) {
        Map<Month, Long> accountArticlesLineChart = new LinkedHashMap<>();
        LocalDateTime now = LocalDateTime.now();
        int years = now.getYear();
        int month = now.getMonthValue();
        for (int i = 1; i <= month; i++) {
            LocalDateTime monthStartLocalDateTime = LocalDateTime.of(years, Month.of(i), 1, 0, 0, 0, 0);
            LocalDateTime monthEndLocalDateTime = monthStartLocalDateTime.plusMonths(1).minusDays(monthStartLocalDateTime.getDayOfMonth()).plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999999);
            accountArticlesLineChart.put(Month.of(i), articleRepository.countAllByAccountAndCreateDateBetween(account, monthStartLocalDateTime, monthEndLocalDateTime));
        }
        return accountArticlesLineChart;
    }

    @Override
    public Map<Month, Long> getArticleQuantityMonthly() {
        Map<Month, Long> articlesLineChart = new LinkedHashMap<>();
        LocalDateTime now = LocalDateTime.now();
        int years = now.getYear();
        int month = now.getMonthValue();
        for (int i = 1; i <= month; i++) {
            LocalDateTime monthStartLocalDateTime = LocalDateTime.of(years, Month.of(i), 1, 0, 0, 0, 0);
            LocalDateTime monthEndLocalDateTime = monthStartLocalDateTime.plusMonths(1).minusDays(monthStartLocalDateTime.getDayOfMonth()).plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999999);
            articlesLineChart.put(Month.of(i), articleRepository.countAllByCreateDateBetween(monthStartLocalDateTime, monthEndLocalDateTime));
        }
        return articlesLineChart;
    }

    @Override
    public Long getArticleQuantityByPublishTrueAndAccount(Account account) {
        return articleRepository.countAllByAccountAndPublish(account, true);
    }

    @Override
    public Map<String, Long> getArticleQuantityByCategory(List<Category> categoryList) {
        Map<String, Long> categoryPieChart = new LinkedHashMap<>();
        for (Category category : categoryList) {
            if (!articleRepository.countAllByCategory(category).equals(Long.valueOf(0))) {
                categoryPieChart.put(category.getCategoryName(), articleRepository.countAllByCategory(category));
            }
        }
        return categoryPieChart;
    }

    @Override
    public Long getArticleQuantity() {
        return articleRepository.count();
    }

//    @Override
//    public Page<Article> paginateGetArticlesByPublishAndCreateDateAfterOrOrderByPageView(Boolean publish, LocalDateTime localDateTime, Pageable pageable) {
//        articleRepository.findAllByPublishAndCreateDateAfterOrOrderByPageViewDesc(publish, localDateTime, pageable);
//        return null;
//    }

//    @Override
//    public Page<Article> fullTextSearch(Pageable pageable, String keyword) {
//        EntityManager em = entityManagerFactory.createEntityManager();
//        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
//        em.getTransaction().begin();
//        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Article.class).get();
//        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("title", "rawContent").matching(keyword).createQuery();
//        javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Article.class);
//        List<Article> resultList = persistenceQuery.getResultList();
//        em.getTransaction().commit();
//        em.close();
//        Page<Article> result = new PageImpl<>(resultList, pageable, null == resultList ? 0 : resultList.size());
//        return result;
//    }
}
