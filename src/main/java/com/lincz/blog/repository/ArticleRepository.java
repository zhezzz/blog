package com.lincz.blog.repository;


import com.lincz.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {

    @Override
    Page<Article> findAll(Pageable pageable);
}
