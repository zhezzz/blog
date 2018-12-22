package com.lincz.blog.entity;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.SortableField;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Indexed
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long articleId;

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY,orphanRemoval = true)
    @OrderBy("createDate desc")
    @JoinColumn(name = "articleId")
    private Set<Comment> comments;

//    @NotNull
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @OrderBy("tagId asc ")
    @JoinTable(name = "article_Tag",joinColumns = {@JoinColumn(name = "articleId",referencedColumnName = "articleId")}
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

    @Field
//    @TikaBridge
    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @NotNull
//    @SortableField
    private Long pageView;

    protected Article(){

    }

    public Article(@NotNull String title, @NotNull String summary, @NotNull String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.pageView = Long.valueOf(0);
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
//        return ArticleUtil.fileToContent(content);
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPageView() {
        return pageView;
    }

    public void setPageView(Long pageView) {
        this.pageView = pageView;
    }

}
