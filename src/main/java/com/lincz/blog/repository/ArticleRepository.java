package com.lincz.blog.repository;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	Page<Article> findAllByPublish(boolean publish, Pageable pageable);

	Page<Article> findArticlesByAccount(Account account, Pageable pageable);

	Page<Article> findArticlesByCategory(Category category, Pageable pageable);

	//TODO
	Page<Article> findArticlesByTagsExists(Tag tag, Pageable pageable);

}
