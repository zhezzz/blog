package com.lincz.blog.controller;

import com.lincz.blog.entity.Article;
import com.lincz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    @Autowired
    private ArticleService articleService;

    @PersistenceContext
    private EntityManager em;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @GetMapping("/search")
    public ModelAndView searchArticle(@PageableDefault(size = 10,sort = { "pageView" }, direction = Sort.Direction.DESC)Pageable pageable, @RequestParam(defaultValue = "")String keyword){
        Page<Article> articlePage = articleService.fullTextSearch(pageable,keyword);
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("SearchResults");
        modelAndView.addObject(articleList);
        return modelAndView;
    }


}
