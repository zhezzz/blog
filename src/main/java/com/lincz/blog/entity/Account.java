package com.lincz.blog.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @NotNull
    @CreatedDate
    private LocalDateTime createDate;

    @NotNull
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @NotNull(message = "用户名不能为空")
    @Length(min = 2, max = 10)
    private String username;

    @NotNull(message = "密码不能为空")
    @Length(min = 4, max = 12)
    private String password;

    @NotNull(message = "电子邮箱地址不能为空")
    @Email
    private String email;

    @NotNull
    private String avatar;

    @NotNull
    private String role;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("articleId desc")
    private Set<Article> articles;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("commentId desc")
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

    public Account(@NotNull(message = "用户名不能为空") @Length(min = 2, max = 10) String username, @NotNull(message = "密码不能为空") @Length(min = 4, max = 12) String password, @NotNull(message = "电子邮箱地址不能为空") @Email String email, @NotNull String avatar, @NotNull String role, @NotNull boolean accountNonExpired, @NotNull boolean accountNonLocked, @NotNull boolean credentialsNonExpired, @NotNull boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        this.role = role;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.role));
        return grantedAuthorities;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
