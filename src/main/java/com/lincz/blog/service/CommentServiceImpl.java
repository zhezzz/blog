package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import com.lincz.blog.repository.ArticleRepository;
import com.lincz.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Page<Comment> paginateGetCommetsByAccount(Account account, Pageable pageable) {
        return commentRepository.findCommentsByAccount(account,pageable);
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getAllComments(Pageable pageable){
        return commentRepository.findAll(pageable);
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
    public Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }
}
