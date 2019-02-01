package com.lincz.blog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long permissionId;

    @NotNull
    private String permissionName;

    public Permission(@NotNull String permissionName) {
        this.permissionName = permissionName;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
