package com.lincz.blog.entity;



import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account implements UserDetails {

    private static final long serialVersionUID = 8433348872054203322L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long accountId;
    //TODO 使用UUID作为主键

    @NotNull
    @CreatedDate
    private LocalDateTime createDate;

    @NotNull
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

//    @Column(unique = true)
    @NotNull(message = "用户名不能为空")
    @Length(min = 3,max = 16)
//    @UniqueUsername
    private String username;

    @NotNull(message = "密码不能为空")
    @Length(min = 4,max = 16)
    private String password;

    @NotNull(message = "电子邮箱地址不能为空")
    @Email
    private String email;

    @NotNull
    private String avatar;

    @NotNull
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @OrderBy("authorityId asc ")
    @JoinTable(name = "account_authority",joinColumns = {@JoinColumn(name = "accountId",referencedColumnName = "accountId")}
            ,inverseJoinColumns = {@JoinColumn(name = "authorityId",referencedColumnName = "authorityId")})
    private  Set<Authority> authorities;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY,orphanRemoval = true)
    @OrderBy("createDate desc")
    @JoinColumn(name = "accountId")
    private Set<Article> articles;

    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @OrderBy("createDate desc ")
    @JoinTable(name = "favorites_article",joinColumns = {@JoinColumn(name = "accountId")}
            ,inverseJoinColumns = {@JoinColumn(name = "articleId")})
    private Set<Article> favoriteArticles;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY,orphanRemoval = true)
    @OrderBy("createDate desc")
    @JoinColumn(name = "accountId")
    private Set<Comment> comments;

    @NotNull
    private boolean accountNonExpired;

    @NotNull
    private boolean accountNonLocked;

    @NotNull
    private boolean credentialsNonExpired;

    @NotNull
    private boolean enabled;

    protected Account() {
    }

    public Account(@NotNull(message = "用户名不能为空") @Length(min = 3, max = 16) String username, @NotNull(message = "密码不能为空") @Length(min = 4, max = 16) String password, @NotNull(message = "电子邮箱地址不能为空") @Email String email, @NotNull String avatar) {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority("post_article") );
        authorities.add(new Authority("delete_article") );
        authorities.add(new Authority("update_article") );
        authorities.add(new Authority("post_comment") );
        authorities.add(new Authority("delete_comment") );
        authorities.add(new Authority("update_comment") );
        authorities.add(new Authority("delete_account") );
        authorities.add(new Authority("update_account") );
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public Long  getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Article> getFavoriteArticles() {
        return favoriteArticles;
    }

    public void setFavoriteArticles(Set<Article> favoriteArticles) {
        this.favoriteArticles = favoriteArticles;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
