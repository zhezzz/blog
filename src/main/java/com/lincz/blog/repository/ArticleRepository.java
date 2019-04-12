package com.lincz.blog.repository;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByPublish(boolean publish, Pageable pageable);

    Page<Article> findAllByAccount(Account account, Pageable pageable);

    List<Article> findTop10ByAccountOrderByCreateDateDesc(Account account);

    Page<Article> findAllByCategory(Category category, Pageable pageable);

    Page<Article> findAllByTags(Tag tag, Pageable pageable);

    Page<Article> findAllByAccountAndPublish(Account account, boolean publish, Pageable pageable);

    Page<Article> findAllByCategoryAndPublish(Category category, boolean publish, Pageable pageable);

    List<Article> findAllByStick(boolean stick);

//    Page<Article> findAllByPublishAndCreateDateAfterOrOrderByPageViewDesc(boolean publish, LocalDateTime localDateTime, Pageable pageable);

    Page<Article> findAllByCreateDateBetween(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime, Pageable pageable);

    Page<Article> findAllByTitleContaining(String keyword, Pageable pageable);

    Page<Article> findAllByAccount_Username(String username, Pageable pageable);


}
