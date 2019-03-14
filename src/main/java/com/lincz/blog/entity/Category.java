package com.lincz.blog.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("createDate desc")
	private Set<Article> articles;

	@NotNull
	private String categoryName;

	protected Category() {
	}

	public Category(@NotNull String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
