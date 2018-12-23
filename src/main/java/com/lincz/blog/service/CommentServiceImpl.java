package com.lincz.blog.service;

import com.lincz.blog.entity.Comment;
import com.lincz.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Page<Comment> paginateGetAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteCommentByCommentId(Long commentId) {
        deleteCommentByCommentId(commentId);
    }

    @Override
    public Comment updateComment(Long commentId, Comment formComment) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.setContent(formComment.getContent());
        return null;
    }
}
