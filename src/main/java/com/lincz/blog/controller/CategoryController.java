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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private HttpServletRequest request;

	// 分类管理页面
	@GetMapping(value = "/management")
	//	@PreAuthorize("hasAuthority('类目管理')")
	public ModelAndView categoryManagementPage() {
		List<Category> categoryList = categoryService.getAllCategory();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("CategoryManagement");
		modelAndView.addObject(categoryList);
		return modelAndView;
	}

	// 添加分类
	@PostMapping(value = "/")
	//	@PreAuthorize("hasAuthority('类目管理')")
	public Category addCategory(@RequestBody Category categoryDTO) {
		return categoryService.createCategory(categoryDTO);
	}

	//给一级分类下添加二级分类
	@PostMapping(value = "/{categoryId}")
	//	@PreAuthorize("hasAuthority('类目管理')")
	public Category addSubcategory(@PathVariable Long categoryId, @RequestBody Category categoryDTO){
		return categoryService.createSubcategory(categoryId,categoryDTO);
	}

	// 删除分类，级联删除分类下的所有文章
	@DeleteMapping(value = "/{categoryId}")
	//	@PreAuthorize("hasAuthority('权限管理')")
	public void deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategoryByCategoryId(categoryId);
	}

	// 修改分类
	@PutMapping(value = "/{categoryId}")
	//	@PreAuthorize("hasAuthority('类目管理')")
	public Category updateCategory(@PathVariable Long categoryId, @RequestBody Category categoryDTO) {
		return categoryService.updateCategory(categoryId, categoryDTO);
	}

	//根据id查询分类
	@GetMapping(value = "/{categoryId}")
	//	@PreAuthorize("hasAuthority('类目管理')")
	public Category getCategoryByCategoryId(@PathVariable Long categoryId) {
		return categoryService.getCategoryByCategoryId(categoryId);
	}

	// 分页列出一个分类下的所有文章
	@GetMapping(value = "/{categoryId}/articles")
	public ModelAndView getArticlesByCategory(@PathVariable Long categoryId,
			@PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		Category category = categoryService.getCategoryByCategoryId(categoryId);
		Page<Article> articlePage = articleService.paginateGetArticlesByCategory(category, pageable);
		List<Article> articleList = articlePage.get().collect(Collectors.toList());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("");
		modelAndView.addObject(articleList);
		return modelAndView;
	}

}
