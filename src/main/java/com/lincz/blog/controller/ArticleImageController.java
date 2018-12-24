package com.lincz.blog.controller;

import com.lincz.blog.util.ArticleUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class ArticleImageController {

    //上传图片
    @RequestMapping(value = "/uploadimage")
    public ArticleUtils.imageResponse uploadImage(MultipartFile imageFile, HttpServletRequest request){
        String imageFileOriginalFilename = imageFile.getOriginalFilename();
        String fileNameExtension = imageFileOriginalFilename.substring(imageFileOriginalFilename.indexOf("."));
        String localFileName = UUID.randomUUID().toString()+fileNameExtension;
        try {
            imageFile.transferTo(Paths.get("blog-data/article-image/"+localFileName));
        }
        catch (IOException ex){
            System.out.println("无法写入文件");
        }
        //TODO 路径问题
        String [] data = {"blog-data/article-image/" + localFileName};
        return ArticleUtils.successResponse(data);
    }
}
