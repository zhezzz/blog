package com.lincz.blog.controller;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Article;
import com.lincz.blog.repository.ArticleRepository;
import com.lincz.blog.service.AccountService;
import com.lincz.blog.service.ArticleService;
import com.lincz.blog.util.ArticleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TestController
{

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleService articleService;
    @Autowired
    ArticleRepository articleRepository;



    @GetMapping("/test")
    public ModelAndView objectinfo(){
        ModelAndView modelAndView = new ModelAndView("test");
        return modelAndView;
    }

    @RequestMapping(value = "/upload")
    public ArticleUtil.imageResponse uploadImage(MultipartFile imageFile){
//        String imageFileOriginalFilename = imageFile.getOriginalFilename();
//        String fileNameExtension = imageFileOriginalFilename.substring(imageFileOriginalFilename.indexOf("."));
//        String localFileName = UUID.randomUUID().toString()+fileNameExtension;
//        try {
//            imageFile.transferTo(Paths.get("blog-data/article-image/"+localFileName));
//        }
//        catch (IOException ex){
//            System.out.println("无法写入文件");
//        }
        String [] data = {"/blog-data/article-image/" + "978c3d9b-a930-461f-9b62-78566364023a.jpg"};
        return ArticleUtil.successResponse(data);

    }

    public Account currentAccount(){
        return accountService.getAccountByUsername(((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername());
    }



}