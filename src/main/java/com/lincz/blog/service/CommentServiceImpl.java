package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Comment;
import com.lincz.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Page<Comment> paginateGetCommetsByAccount(Account account, Pageable pageable) {
        return commentRepository.findAllByAccount(account, pageable);
    }

    @Override
    public Page<Comment> paginateGetCommetsByArticle(Article article, Pageable pageable) {
        return commentRepository.findAllByArticle(article, pageable);
    }


    @Override
    public Comment createComment(Comment commentDTO) {
        Comment comment = new Comment(commentDTO.getContent());
        comment.setArticle(commentDTO.getArticle());
        comment.setAccount(commentDTO.getAccount());
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> paginateGetAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public void deleteCommentByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        commentRepository.delete(comment);
    }

    @Transactional
    @Override
    public Comment updateComment(Long commentId, Comment commentDTO) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.setContent(commentDTO.getContent());
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public boolean isCommentExists(Long commentId) {
        return commentRepository.existsById(commentId);
    }

    @Override
    public List<Comment> getRecent10CommentsByAccount(Account account) {
        return commentRepository.findTop10ByAccountOrderByCreateDateDesc(account);
    }

    @Override
    public Map<Month, Long> getCommentQuantityMonthlyByAccount(Account account) {
        Map<Month, Long> accountCommentsLineChart = new LinkedHashMap<>();
        LocalDateTime now = LocalDateTime.now();
        int years = now.getYear();
        int month = now.getMonthValue();
        for (int i = 1; i <= month ; i++) {
            LocalDateTime monthStartLocalDateTime = LocalDateTime.of(years,Month.of(i),1,0,0,0,0);
            LocalDateTime monthEndLocalDateTime = monthStartLocalDateTime.plusMonths(1).minusDays(monthStartLocalDateTime.getDayOfMonth()).plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999999);
            accountCommentsLineChart.put(Month.of(i), commentRepository.countAllByAccountAndCreateDateBetween(account, monthStartLocalDateTime, monthEndLocalDateTime));
        }
        return accountCommentsLineChart;
    }

    @Override
    public Map<Month, Long> getCommentQuantityMonthly() {
        Map<Month, Long> commentsLineChart = new LinkedHashMap<>();
        LocalDateTime now = LocalDateTime.now();
        int years = now.getYear();
        int month = now.getMonthValue();
        for (int i = 1; i <= month ; i++) {
            LocalDateTime monthStartLocalDateTime = LocalDateTime.of(years,Month.of(i),1,0,0,0,0);
            LocalDateTime monthEndLocalDateTime = monthStartLocalDateTime.plusMonths(1).minusDays(monthStartLocalDateTime.getDayOfMonth()).plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999999);
            commentsLineChart.put(Month.of(i), commentRepository.countAllByCreateDateBetween(monthStartLocalDateTime, monthEndLocalDateTime));
        }
        return commentsLineChart;
    }
}
