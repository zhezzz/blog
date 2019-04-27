package com.lincz.blog.controller;

import com.lincz.blog.entity.*;
import com.lincz.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/article")
public class ArticleController {
    // TODO 限制文章和评论字数，前台ID传入验证跳转到404

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccountService accountService;


    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HttpServletRequest request;

    // 获取文章管理页面
    @GetMapping(value = "/management")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    public ModelAndView articleManagementPage(
            @PageableDefault(sort = {"articleId"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) Boolean publish, @RequestParam(required = false) Boolean stick, @RequestParam(required = false) String searchtype, @RequestParam(required = false) String keyword) {
        Page<Article> articlePage = articleService.paginateGetAllArticles(pageable);
        if (publish == null && stick == null) {
            if ("title".equalsIgnoreCase(searchtype)) {
                articlePage = articleService.paginateGetArticlesByTitleContianing(keyword, pageable);
            }
            if ("username".equalsIgnoreCase(searchtype)) {
                articlePage = articleService.paginateGetArticlesByUsername(keyword, pageable);
            }
        }
        if (publish == null && stick != null) {
            articlePage = articleService.getArticlesByStick(stick, pageable);
        }
        if (publish != null && stick == null) {
            articlePage = articleService.paginateGetArticlesByPublish(publish, pageable);
        }
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/admin/ArticleManagement");
        modelAndView.addObject("articleList", articleList);
        modelAndView.addObject("articlePage", articlePage);
        return modelAndView;
    }

    // 获取个人文章管理页面
    @GetMapping(value = "/mymanagement")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public ModelAndView myArticleManagementPage(
            @PageableDefault(sort = {"articleId"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) Boolean publish) {
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        Page<Article> articlePage;
        if (publish == null) {
            articlePage = articleService.paginateGetArticlesByAccount(currentAccount, pageable);
        } else {
            articlePage = articleService.paginateGetArticlesByAccountAndPublish(currentAccount, publish, pageable);
        }
        List<Article> articleList = articlePage.get().collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("management/user/MyArticleManagement");
        modelAndView.addObject("articleList", articleList);
        modelAndView.addObject("articlePage", articlePage);
        return modelAndView;
    }

    // 根据文章id查看文章
    @GetMapping(value = "/details/{articleId}")
    public ModelAndView articleDetails(@PathVariable Long articleId, @PageableDefault(sort = {"commentId"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Article article = articleService.getArticleByArticleId(articleId);
        Page<Comment> commentPage = commentService.paginateGetCommetsByArticle(article, pageable);
        List<Comment> commentList = commentPage.get().collect(Collectors.toList());
        articleService.increasePageView(articleId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ArticleDetails");
        modelAndView.addObject("article", article);
        modelAndView.addObject("commentList", commentList);
        modelAndView.addObject("commentPage", commentPage);
        return modelAndView;
    }

    // 发布文章
    @GetMapping(value = "/post")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public ModelAndView postArticleView() {
        String currentUsername = request.getUserPrincipal().getName();
        Account currentAccount = accountService.getAccountByUsername(currentUsername);
        Article article = new Article("标题", "请在此编写文章。", false);
        article.setAccount(currentAccount);
        article.setPageView(Long.valueOf(0));
        article.setRawContent("请在此编写文章");
        article.setStick(false);
        article.setCategory(categoryService.getCategoryByCategoryId(Long.valueOf(1)));
        articleService.createArticle(article);
        ModelAndView modelAndView = new ModelAndView("redirect:/article/update/" + article.getArticleId());
        return modelAndView;
    }

    // 获取修改文章页面
    @GetMapping(value = "/update/{articleId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public ModelAndView updateArticlePage(@PathVariable Long articleId) {
        Article article = articleService.getArticleByArticleId(articleId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("UpdateArticle");
        modelAndView.addObject(article);
        return modelAndView;
    }

    // 修改文章
    @PutMapping(value = "/{articleId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public void updateArticle(@PathVariable Long articleId, @RequestBody Article articleDTO) throws InterruptedException{
        Category category = categoryService.getCategoryByCategoryId(articleDTO.getCategory().getCategoryId());
        articleDTO.setCategory(category);
        articleService.updateArticle(articleId, articleDTO);
    }

    //修改文章状态
    @PutMapping(value = "/{articleId}/publish")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public void updateArticleStatus(@PathVariable Long articleId, @RequestBody Article articleDTO) {
        articleService.updateArticleStatus(articleId, articleDTO);
    }

    //置顶文章
    @PutMapping(value = "/{articleId}/stick")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    public void updateArticleStick(@PathVariable Long articleId, @RequestBody Article articleDTO) {
        articleService.updateArticleStick(articleId, articleDTO);
    }

    // 删除文章
    @DeleteMapping(value = "/{articleId}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public void delete(@PathVariable Long articleId) {
        articleService.deleteArticleByArticleId(articleId);
    }

    // 上传图片
    @PostMapping(value = "/{articleId}/upload")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','USER')")
    public Response uploadImage(@PathVariable Long articleId, @RequestPart(value = "upload") MultipartFile imageFile)
            throws IOException {
        String originalFileName = imageFile.getOriginalFilename();
        if (imageFile.isEmpty()) {
            return new Response(false, null);
        }
        if (originalFileName.endsWith(".jpg")
                || originalFileName.endsWith(".png")
                || originalFileName.endsWith(".gif")
                || originalFileName.endsWith(".JPG")
                || originalFileName.endsWith(".PNG")
                || originalFileName.endsWith(".GIF")) {
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;
            File file = new File("/root/data/article/image/" + articleId);
            if (!file.exists()) {
                file.mkdirs();
            }
            imageFile.transferTo(new File(file.toString() + "/" + fileName));
            return new Response(true, request.getContextPath() + "/data/article/image/" + articleId + "/" + fileName);
        } else {
            return new Response(false, null);
        }
    }

    // 定义成员内部类供图片上传返回响应json
    private class Response {
        private Boolean uploaded;

        private String url;

        public Response(Boolean uploaded, String url) {
            this.uploaded = uploaded;
            this.url = url;
        }

        public Boolean getUploaded() {
            return uploaded;
        }

        public void setUploaded(Boolean uploaded) {
            this.uploaded = uploaded;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
