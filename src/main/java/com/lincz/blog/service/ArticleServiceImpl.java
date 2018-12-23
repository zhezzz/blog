package com.lincz.blog.service;

import com.lincz.blog.entity.Article;
import com.lincz.blog.repository.ArticleRepository;
import com.lincz.blog.util.ArticleUtil;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
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

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Transactional
    @Override
    public Article postArticle(Article article) {
        articleRepository.save(article);
        return article;
    }

    @Transactional
    @Override
    public Article updateArticle(Long articleId, Article formArticle) {
        Article article = articleRepository.findById(articleId).orElse(null);
        article.setTitle(formArticle.getTitle());
        article.setSummary(formArticle.getSummary());
        return article;
    }

    @Transactional
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
        article.setPageView(article.getPageView()+1);
    }

    @Override
    public Page<Article> fullTextSearch(Pageable pageable,String keyword) {
        EntityManager em = entityManagerFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        em.getTransaction().begin();
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity( Article.class ).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder
                .keyword()
                .onFields("title","summary","content")
                .matching("data")
                .createQuery();
        javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Article.class);
        List<Article> resultList = persistenceQuery.getResultList();
        Page<Article>  result = new PageImpl<>(resultList,pageable,null == resultList ? 0 : resultList.size());
        return result;
    }

    @Override
    public Page<Article> paginateGetAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }
}
