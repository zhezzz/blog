package com.lincz.blog.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
    private String authorityName;

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

    protected Authority() {
    }

    public Authority(String authority) {
        this.authorityName = authority;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + authorityName;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Authority) {
            return authorityName.equals(((Authority) obj).authorityName);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.authorityName.hashCode();
    }

    @Override
    public String toString() {
        return this.authorityName;
    }

}
