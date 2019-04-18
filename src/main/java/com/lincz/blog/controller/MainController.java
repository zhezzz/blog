package com.lincz.blog.controller;

import com.lincz.blog.entity.*;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.service.CategoryService;
import com.lincz.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpServletRequest request;


    @GetMapping(value = "/index")
    public ModelAndView index(
            @PageableDefault(sort = {"articleId"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) String type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentMonthStartTime = now.minusDays(now.getDayOfMonth() - 1).minusHours(now.getHour()).minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano() + 1);
        Page<Article> stickArticlePage = articleService.getArticlesByStick(true, pageable);
        List<Article> stickArticleList = stickArticlePage.get().collect(Collectors.toList());
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


    //统计预览
    //普通用户：公开未公开饼图，今年每月发布文章折线图，每月收到的评论折线图，

    //管理员：今年每月发布文章总数折线图，分类下文章数量饼图，每月评论数折线图，每月新增账号折线图---访问量 折线图？
    @GetMapping(value = "/overview")
    public ModelAndView overview() {
        Long articleQuantity = articleService.getArticleQuantity();
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        Map<Month, Long> accountArticlesLineChart = articleService.getArticleQuantityMonthlyByAccount(currentAccount);
        Map<Month, Long> accountCommentsLineChart = commentService.getCommentQuantityMonthlyByAccount(currentAccount);

        Map<Month, Long> articlesLineChart = articleService.getArticleQuantityMonthly();
        Map<String, Long> categoryPieChart = articleService.getArticleQuantityByCategory(categoryService.getAllCategory());
        Map<Month, Long> commentsLineChart = commentService.getCommentQuantityMonthly();
        Map<Month, Long> accountsLineChart = accountService.getAccountQuantityMonthly();

        Long[] accountArticlesLineChartArray = new Long[accountArticlesLineChart.size()];
        accountArticlesLineChart.forEach((k,v) -> {accountArticlesLineChartArray[k.getValue() - 1] = v;});
        Long[] accountCommentsLineChartArray = new Long[accountCommentsLineChart.size()];
        accountCommentsLineChart.forEach((k,v) -> {accountCommentsLineChartArray[k.getValue() - 1] = v;});
        Long[] articlesLineChartArray = new Long[articlesLineChart.size()];
        articlesLineChart.forEach((k,v) -> {articlesLineChartArray[k.getValue() - 1] = v;});
        Long[] commentsLineChartArray = new Long[commentsLineChart.size()];
        commentsLineChart.forEach((k,v) -> {commentsLineChartArray[k.getValue() - 1] = v;});
        Long[] accountsLineChartArray = new Long[accountsLineChart.size()];
        accountsLineChart.forEach((k,v) -> {accountsLineChartArray[k.getValue() - 1] = v;});

        List<CategoryRatio> categoryRatiosList = new ArrayList<>();
        for(Map.Entry<String, Long> entry : categoryPieChart.entrySet()){
            categoryRatiosList.add(new CategoryRatio(entry.getKey(),new BigDecimal(Double.valueOf(entry.getValue()*100/articleQuantity).doubleValue())));
        }
        CategoryRatio[] categoryRatiosArray = categoryRatiosList.toArray(new CategoryRatio[categoryPieChart.size()]);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/ManagementOverview");
        modelAndView.addObject("accountArticlesLineChartArray", accountArticlesLineChartArray);
        modelAndView.addObject("accountCommentsLineChartArray", accountCommentsLineChartArray);
        modelAndView.addObject("articlesLineChartArray", articlesLineChartArray);
        modelAndView.addObject("commentsLineChartArray", commentsLineChartArray);
        modelAndView.addObject("accountsLineChartArray", accountsLineChartArray);

        modelAndView.addObject("categoryRatiosArray", categoryRatiosArray);
        return modelAndView;
    }

    private class CategoryRatio{
        private String name;

        private BigDecimal y;

        public CategoryRatio(String name, BigDecimal y) {
            this.name = name;
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getY() {
            return y.setScale(2).doubleValue();
        }

        public void setY(BigDecimal y) {
            this.y = y;
        }
    }




}
