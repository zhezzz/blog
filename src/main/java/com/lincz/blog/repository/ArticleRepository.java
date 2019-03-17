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

	Page<Article> findAllByAccount(Account account, Pageable pageable);

	Page<Article> findAllByCategory(Category category, Pageable pageable);

	Page<Article> findAllByTags(Tag tag, Pageable pageable);

	Page<Article> findAllByAccountAndPublish(Account account, boolean publish, Pageable pageable);

	Page<Article> findAllByCategoryAndPublish(Category category, boolean publish, Pageable pageable);

	Page<Article> findAllByStickTrue(Pageable pageable);

}
