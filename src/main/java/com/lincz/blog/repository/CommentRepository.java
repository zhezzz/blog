package com.lincz.blog.repository;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	Page<Comment> findAllByAccount(Account account, Pageable pageable);

	Page<Comment> findAllByArticle(Article article, Pageable pageable);

	List<Comment> findTop10ByAccountOrderByCreateDateDesc(Account account);

}
