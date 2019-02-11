package com.lincz.blog.controller;


import com.lincz.blog.entity.Category;
import com.lincz.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    //分类管理页面
    @GetMapping(value = "/")
    public ModelAndView categoryManagementPage(){
        List<Category> categoryList = categoryService.getAllCategory();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("CategoryManagement");
        modelAndView.addObject(categoryList);
        return modelAndView;
    }

    //添加分类
    @PostMapping(value = "/add")
    public Category addCategory(Category formCategory){
        Category category = new Category(formCategory.getCategoryName());
        categoryService.createCategory(category);
        return category;
    }

    //删除分类，级联删除分类下的所有文章
    @DeleteMapping(value = "/delete/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategoryByCategoryId(categoryId);
    }

    //修改分类
    @PutMapping(value = "/update/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, Category formCategory){
        return categoryService.updateCategory(categoryId,formCategory);
    }



}
