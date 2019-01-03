package com.lincz.blog.repository;

import com.lincz.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Override
    Page<Comment> findAll(Pageable pageable);

    Page<Comment> findAllByAccount_AccountId(Long accountId, Pageable pageable);
}
