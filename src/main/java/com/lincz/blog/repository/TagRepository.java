package com.lincz.blog.repository;

import com.lincz.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
