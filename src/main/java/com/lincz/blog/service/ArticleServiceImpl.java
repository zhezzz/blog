package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.entity.Tag;
import com.lincz.blog.repository.ArticleRepository;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.query.dsl.QueryBuilder;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(Long articleId, Article articleDTO) {
        Article article = articleRepository.findById(articleId).orElse(null);
        String rawContent = Jsoup.parse(articleDTO.getContent()).text();
        articleDTO.setRawContent(rawContent);
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent().replaceAll("<img", "<img class=\"img-fluid"));
        article.setRawContent(articleDTO.getRawContent());
        article.setPublish(articleDTO.isPublish());
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticlePublishStatus(Long articleId, Article articleDTO) {
        Article article = articleRepository.findById(articleId).orElse(null);
        article.setPublish(articleDTO.isPublish());
        return articleRepository.save(article);
    }


    @Override
    public Article updateArticleStatus(Long articleId, Article articleDTO) {
        Article article = articleRepository.findById(articleId).orElse(null);
        article.setStick(articleDTO.isStick());
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticleByArticleId(Long articleId) {
        articleRepository.deleteById(articleId);
        File dir = new File("/root/data/article/image/" + articleId);
        File[] files = dir.listFiles();
        for (File file : files) {
            file.delete();
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
    }

    @Override
    public Page<Article> paginateGetAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public Page<Article> paginateGetArticlesByAccount(Account account, Pageable pageable) {
        Page<Article> articles = articleRepository.findAllByAccount(account, pageable);
        return articles;
    }

    @Override
    public Page<Article> paginateGetArticlesByPublish(boolean publish, Pageable pageable) {
        Page<Article> articles = articleRepository.findAllByPublish(publish, pageable);
        return articles;
    }

    @Override
    public Page<Article> paginateGetArticlesByCategory(Category category, Pageable pageable) {
        return articleRepository.findAllByCategory(category, pageable);
    }

    @Override
    public Page<Article> paginateGetArticlesByTags(Tag tag, Pageable pageable) {
        return articleRepository.findAllByTags(tag, pageable);
    }

    @Override
    public boolean isArticleExists(Long articleId) {
        return articleRepository.existsById(articleId);
    }

    @Override
    public Page<Article> paginateGetArticlesByAccountAndPublish(Account account, boolean publish, Pageable pageable) {
        return articleRepository.findAllByAccountAndPublish(account, publish, pageable);
    }

    @Override
    public Page<Article> paginateGetArticlesByCategoryAndPublish(Category category, boolean publish, Pageable pageable) {
        return articleRepository.findAllByCategoryAndPublish(category, publish, pageable);
    }

    @Override
    public boolean isArticleExistsAndPublish(Long articleId, boolean publish) {
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
    public List<Article> getArticlesByStick(boolean stick) {
        return articleRepository.findAllByStick(stick);
    }

    @Override
    public List<Article> getRecent10ArticlesByAccount(Account account) {
        return articleRepository.findTop10ByAccountOrderByCreateDateDesc(account);
    }

    @Override
    public Page<Article> paginateGetArticlesByCreatedDateBetweenAndPublish(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime, boolean publish, Pageable pageable) {
        articleRepository.findAllByCreateDateBetweenAndPublish(startLocalDateTime, endLocalDateTime, publish, pageable);
        return null;
    }
}
