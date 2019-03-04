package com.lincz.blog.controller;

import com.lincz.blog.entity.Article;
import com.lincz.blog.entity.Category;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ArticleService articleService;

	// 分类管理页面
	@GetMapping(value = "/management")
	public ModelAndView categoryManagementPage() {
		List<Category> categoryList = categoryService.getAllCategory();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("CategoryManagement");
		modelAndView.addObject(categoryList);
		return modelAndView;
	}

	// 添加分类
	@PostMapping(value = "/add")
	public Category addCategory(Category formCategory) {
		Category category = new Category(formCategory.getCategoryName());
		categoryService.createCategory(category);
		return category;
	}

	// 删除分类，级联删除分类下的所有文章
	@DeleteMapping(value = "/delete/{categoryId}")
	public void deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategoryByCategoryId(categoryId);
	}

	// 修改分类
	@PutMapping(value = "/update/{categoryId}")
	public Category updateCategory(@PathVariable Long categoryId, Category formCategory) {
		return categoryService.updateCategory(categoryId, formCategory);
	}

	// 分页列出一个分类下的所有文章
	@GetMapping(value = "/{categoryId}/articles")
	public ModelAndView getArticlesByCategory(@PathVariable Long categoryId,
			@PageableDefault(size = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		Category category = categoryService.getCategoryByCategoryId(categoryId);
		Page<Article> articlePage = articleService.paginateGetArticlesByCategory(category, pageable);
		List<Article> articleList = articlePage.get().collect(Collectors.toList());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("Index");
		modelAndView.addObject(articleList);
		return modelAndView;
	}

}
