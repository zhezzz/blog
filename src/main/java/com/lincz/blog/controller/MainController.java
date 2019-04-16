package com.lincz.blog.controller;

import com.lincz.blog.entity.*;
import com.lincz.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private ArticleService articleService;


    @GetMapping(value = "/index")
    public ModelAndView index(
            @PageableDefault(sort = {"articleId"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) String type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentMonthStartTime = now.minusDays(now.getDayOfMonth() - 1).minusHours(now.getHour()).minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano() + 1);
        List<Article> stickArticleList = articleService.getArticlesByStick(true);
        Page<Article> articlePage = articleService.paginateGetArticlesByPublish(true, pageable);
        ModelAndView modelAndView = new ModelAndView();
        if (type == null) {
            articlePage = articleService.paginateGetArticlesByPublish(true, pageable);
        }
        if ("popular".equalsIgnoreCase(type)) {
            //TODO
//            articlePage = articleService.paginateGetArticlesByPublishAndCreateDateAfterOrOrderByPageView(true, currentMonthStartTime, pageable);
        }
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        modelAndView.setViewName("Index");
        modelAndView.addObject("articlePage", articlePage);
        modelAndView.addObject("articleList", articleList);
        modelAndView.addObject("stickArticleList", stickArticleList);
        return modelAndView;
    }


//    @GetMapping(value = "/login")
//    public ModelAndView loginPage() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("Login");
//        return modelAndView;
//    }

    @GetMapping(value = "/register")
    public ModelAndView registerPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Register");
        return modelAndView;
    }

    @GetMapping(value = "/overview")
    public ModelAndView overview() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/ManagementOverview");
        return modelAndView;
    }


}
