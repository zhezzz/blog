package com.lincz.blog.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long authorityId;

    @NotNull
    private String authority;

    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @OrderBy("accountId asc ")
    @JoinTable(name = "account_authority",joinColumns = {@JoinColumn(name = "authorityId",referencedColumnName = "authorityId")}
            ,inverseJoinColumns = {@JoinColumn(name = "accountId",referencedColumnName = "accountId")})
    private Set<Account> accounts;

    @NotNull
    @CreatedDate
    private LocalDateTime createDate;

    @NotNull
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }


    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
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
}
