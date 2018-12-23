package com.lincz.blog.entity;


import com.lincz.blog.enums.AccountRolePermissionEnum;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {

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

    @NotNull
    private String avatar;

    @NotNull(message = "用户名不能为空")
    @Length(min = 3,max = 16)
//    @UniqueUsername
    //TODO 用户名唯一性校验
    private String username;

    @NotNull(message = "密码不能为空")
    @Length(min = 4,max = 16)
    private String password;

    @NotNull(message = "电子邮箱地址不能为空")
    @Email
    private String email;

    @NotNull
    private String role;

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

    public Account(@NotNull String username, @NotNull String password, @NotNull String email) {
        this.username = username;
        this.avatar ="default.jpg";
        this.password = password;
        this.email = email;
        this.enabled = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
        this.role = AccountRolePermissionEnum.ROLE_USER.getRoleName();
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(this.role);
            authorities.add(grantedAuthority);
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
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
}
