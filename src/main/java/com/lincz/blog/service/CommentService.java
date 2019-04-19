package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;

public interface CommentService {

    Page<Comment> paginateGetAllComments(Pageable pageable);

    Page<Comment> paginateGetCommetsByAccount(Account account, Pageable pageable);

    Page<Comment> paginateGetCommetsByArticle(Article article, Pageable pageable);

    Comment createComment(Comment commentDTO);

    void deleteCommentByCommentId(Long commentId);

    Comment updateComment(Long commentId, Comment commentDTO);

    Comment getCommentByCommentId(Long commentId);

    boolean isCommentExists(Long commentId);

    List<Comment> getRecent10CommentsByAccount(Account account);

    Map<Month, Long> getMyCommentQuantityMonthlyByAccount(Account account);

    Map<Month, Long> getCommentQuantityMonthly();

    Long getCommentQuantityByArticle(Article article);

    Map<Month, Long> getReceiveCommentQuantityMonthlyByAccount(Account account, List<Article> articleList);


}
