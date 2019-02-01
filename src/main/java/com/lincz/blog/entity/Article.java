package com.lincz.blog.entity;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.TikaBridge;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Indexed
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long articleId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    //TODO 评论懒加载问题
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER,orphanRemoval = true)
    @OrderBy("createDate desc")
    @JoinColumn(name = "articleId")
    private Set<Comment> comments;

//    @NotNull
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @OrderBy("tagId asc ")
    @JoinTable(name = "article_tag",joinColumns = {@JoinColumn(name = "articleId",referencedColumnName = "articleId")}
                ,inverseJoinColumns = {@JoinColumn(name = "tagId",referencedColumnName = "tagId")})
    private Set<Tag> tags;

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @NotNull
//    @SortableField
    @CreatedDate
    private LocalDateTime createDate;

    @NotNull
//    @SortableField
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Field
    @NotNull
    private String title;

    @Field
    @NotNull
    private String summary;

//    @Field
//    @TikaBridge
    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @Field
    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String rawContent;//纯文本内容

    @NotNull
//    @SortableField
    private Long pageView;

    protected Article(){

    }

    public Article(@NotNull Account account, @NotNull String title, @NotNull String summary, @NotNull String content, @NotNull String rawContent, @NotNull Long pageView) {
        this.account = account;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.rawContent = rawContent;
        this.pageView = pageView;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

}
