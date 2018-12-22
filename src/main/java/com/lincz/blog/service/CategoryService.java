package com.lincz.blog.service;


import com.lincz.blog.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();

    Category createCategory(Category category);

    Category deleteCategoryByCategoryId(Long CategoryId);

    Category updateCategory(Category category);


}
