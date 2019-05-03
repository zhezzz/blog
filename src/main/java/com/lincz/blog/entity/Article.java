package com.lincz.blog.entity;

//import com.hankcs.lucene.HanLPAnalyzer;
//import org.hibernate.search.annotations.Analyzer;
//import org.hibernate.search.annotations.Field;
//import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

//@Indexed
@Entity
//@Analyzer(impl = HanLPAnalyzer.class)
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @NotNull
    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("commentId desc")
    private Set<Comment> comments;

    @NotNull
    @ManyToOne
    private Category category;

    @NotNull
    @CreatedDate
    private LocalDateTime createDate;

    @NotNull
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

//    @Field
    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

//    @Field
    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String rawContent;// 纯文本内容

    @NotNull
    private Long pageView;

    @NotNull
    private Boolean publish;

    @NotNull
    private Boolean stick;//置顶

    protected Article() {

    }

    public Article(@NotNull String title, @NotNull String content, @NotNull Boolean publish) {
        this.title = title;
        this.content = content;
        this.publish = publish;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public Long getPageView() {
        return pageView;
    }

    public void setPageView(Long pageView) {
        this.pageView = pageView;
    }

    public Boolean isPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public Boolean isStick() {
        return stick;
    }

    public void setStick(Boolean stick) {
        this.stick = stick;
    }


}
