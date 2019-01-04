package com.lincz.blog.service;


import com.lincz.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService  {

    Page<Comment> paginateGetCommentsByAccountId(Long accountId, Pageable pageable);

    Comment createComment(Comment comment);

    void deleteCommentByCommentId(Long commentId);

    Comment updateComment(Long commentId,Comment comment);

    Comment getArticleByArticleId(Long commentId);

}
