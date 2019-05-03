package com.lincz.blog.repository;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByPublish(Boolean publish, Pageable pageable);

    Page<Article> findAllByAccount(Account account, Pageable pageable);

    List<Article> findAriclesByAccount(Account account);

    List<Article> findTop10ByAccountOrderByCreateDateDesc(Account account);

    Page<Article> findAllByCategory(Category category, Pageable pageable);

    Page<Article> findAllByAccountAndPublish(Account account, Boolean publish, Pageable pageable);

    Page<Article> findAllByCategoryAndPublish(Category category, Boolean publish, Pageable pageable);

    Page<Article> findAllByStick(Boolean stick, Pageable pageable);

    List<Article> findAllByCreateDateBetween(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);

    List<Article> findAllByAccountAndCreateDateBetween(Account account, LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);

    Page<Article> findAllByCreateDateBetween(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime, Pageable pageable);

    Page<Article> findAllByPublishTrueAndCreateDateBetweenOrderByPageViewDesc(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime, Pageable pageable);

    Page<Article> findAllByTitleContaining(String keyword, Pageable pageable);

    Page<Article> findAllByRawContentContainingOrTitleContaining(String keyword1, String keyword2,Pageable pageable);

    Page<Article> findAllByAccount_Username(String username, Pageable pageable);

    Long countAllByAccountAndCreateDateBetween(Account account, LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);

    Long countAllByCreateDateBetween(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);

    Long countAllByAccountAndPublish(Account account, Boolean publish);

    Long countAllByCategory(Category category);

}
