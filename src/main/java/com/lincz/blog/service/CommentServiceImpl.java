package com.lincz.blog.service;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import com.lincz.blog.repository.ArticleRepository;
import com.lincz.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Page<Comment> paginateGetCommentsByAccountId(Long accountId, Pageable pageable) {
        return commentRepository.findAllByAccount_AccountId(accountId,pageable);
    }

    @Override
    public Comment createComment(Comment comment) {
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public void deleteCommentByCommentId(Long commentId) {
        deleteCommentByCommentId(commentId);
    }

    @Transactional
    @Override
    public Comment updateComment(Long commentId, Comment formComment) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.setContent(formComment.getContent());
        return comment;
    }

    @Override
    public Comment getArticleByArticleId(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }
}
