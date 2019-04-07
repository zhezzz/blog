package com.lincz.blog.service;

import com.lincz.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    Category getCategoryByCategoryId(Long categoryId);

    List<Category> getAllCategory();

    Page<Category> paginateGetAllCategory(Pageable pageable);

    Category createCategory(Category categoryDTO);

    void deleteCategoryByCategoryId(Long categoryId);

    Category updateCategory(Long categoryId, Category categoryDTO);

    boolean isCategoryExists(Long categoryId);

}
