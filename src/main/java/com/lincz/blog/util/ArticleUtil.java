package com.lincz.blog.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArticleUtil {

    public static void contentToFile(Long articleId, String content){
        File file =new File("blog-data/article-content/"+articleId+".txt");
        try{
            Writer out =new FileWriter(file);
            out.write(content);
            out.close();
        }
        catch (IOException ex){
            System.out.println("无法写入文章内容到文件");
        }
    }

    public static String fileToContent(String fileName){
        fileName = "blog-data/article-content/"+fileName;
        String content = new String();
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        }
        catch (IOException ex){
            System.out.println("从文件读取文章内容失败");
        }
        return content;
    }
    public static void deleteFile(String fileName){
        try {
            Files.delete(Paths.get("blog-data/article-content/"+fileName));
        }
        catch (IOException ex){
            System.out.println("无法删除文件");
        }
    }

    public static imageResponse successResponse(String[] data){
        return new imageResponse(0,data);
    }

    public static class imageResponse implements Serializable{
        private Integer errno;

        private String [] data;

        public imageResponse() {
        }

        public imageResponse(Integer errno, String[] data) {
            this.errno = errno;
            this.data = data;
        }

        public Integer getErrno() {
            return errno;
        }

        public String[] getData() {
            return data;
        }
    }


}
