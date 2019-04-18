package com.lincz.blog.repository;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByAccount(Account account, Pageable pageable);

    Page<Comment> findAllByArticle(Article article, Pageable pageable);

    List<Comment> findTop10ByAccountOrderByCreateDateDesc(Account account);

    Page<Comment> findAllByAccount_Username(String username, Pageable pageable);

    Long countCommentsByAccount(Account account);

    Long countAllByAccountAndCreateDateBetween(Account account, LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);

    Long countAllByCreateDateBetween(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);

}
