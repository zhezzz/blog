package com.lincz.blog.service;

import com.lincz.blog.entity.Category;

import java.util.List;

public interface CategoryService {

	Category getCategoryByCategoryId(Long categoryId);

	List<Category> getAllCategory();

	Category createCategory(Category category);

	void deleteCategoryByCategoryId(Long categoryId);

	Category updateCategory(Long categoryId, Category category);

}
