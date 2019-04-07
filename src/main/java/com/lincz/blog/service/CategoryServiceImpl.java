package com.lincz.blog.service;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }


    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> paginateGetAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category createCategory(Category categoryDTO) {
        Category category = new Category(categoryDTO.getCategoryName());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryByCategoryId(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Category updateCategory(Long categoryId, Category categoryDTO) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        category.setCategoryName(categoryDTO.getCategoryName());
        return categoryRepository.save(category);
    }

    @Override
    public boolean isCategoryExists(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }
}
