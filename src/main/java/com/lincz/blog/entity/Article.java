package com.lincz.blog.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long articleId;

	@NotNull
	@ManyToOne
	private Account account;

	@OneToMany(mappedBy = "article", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("createDate desc")
	private Set<Comment> comments;

// @NotNull
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@OrderBy("tagId asc ")
	@JoinTable(name = "article_tag", joinColumns = {
			@JoinColumn(name = "articleId", referencedColumnName = "articleId")}, inverseJoinColumns = {
					@JoinColumn(name = "tagId", referencedColumnName = "tagId")})
	private Set<Tag> tags;

// @NotNull
	@ManyToOne
	private Category category;

	@NotNull
	@CreatedDate
	private LocalDateTime createDate;

	@NotNull
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

	@NotNull
	private String title;

	@NotNull
	@Column(columnDefinition = "MEDIUMTEXT")
	private String content;

	@NotNull
	@Column(columnDefinition = "MEDIUMTEXT")
	private String rawContent;// 纯文本内容

	@NotNull
	private Long pageView;

	@NotNull
	private boolean publish;

	@NotNull
	private boolean stick;//置顶

	protected Article() {

	}

	public Article(@NotNull Account account, @NotNull String title, @NotNull String content, @NotNull String rawContent) {
		this.account = account;
		this.title = title;
		this.content = content;
		this.rawContent = rawContent;
		this.pageView = Long.valueOf(0);
		this.publish = false;
		this.stick = false;
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

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public boolean isStick() {
		return stick;
	}

	public void setStick(boolean stick) {
		this.stick = stick;
	}


}
