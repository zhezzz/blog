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
		return commentRepository.findAllByArticle(article,pageable);
	}


	@Override
	public Comment createComment(Comment commentDTO) {
		Comment comment = new Comment(commentDTO.getContent());
		comment.setArticle(commentDTO.getArticle());
		comment.setAccount(commentDTO.getAccount());
		return commentRepository.save(comment);
	}

	@Override
	public Page<Comment> getAllComments(Pageable pageable) {
		return commentRepository.findAll(pageable);
	}

	@Override
	public void deleteCommentByCommentId(Long commentId) {
		deleteCommentByCommentId(commentId);
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
}
