package com.lincz.blog.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long tagId;

    @NotNull
    private String tagName;

    @NotNull
    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @OrderBy("createDate desc ")
    @JoinTable(name = "article_tag",joinColumns = {@JoinColumn(name = "tagId",referencedColumnName = "tagId")}
                ,inverseJoinColumns = {@JoinColumn(name = "articleId",referencedColumnName = "articleId")})
    private Set<Article> articles;


    protected Tag() {
    }

    public Tag(@NotNull String tagName) {
        this.tagName = tagName;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

}
