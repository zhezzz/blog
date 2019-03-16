package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

	Page<Comment> getAllComments(Pageable pageable);

	Page<Comment> paginateGetCommetsByAccount(Account account, Pageable pageable);

	Page<Comment> paginateGetCommetsByArticle(Article article, Pageable pageable);

	Comment createComment(Comment commentDTO);

	void deleteCommentByCommentId(Long commentId);

	Comment updateComment(Long commentId, Comment commentDTO);

	Comment getCommentByCommentId(Long commentId);

	boolean isCommentExists(Long commentId);

}
