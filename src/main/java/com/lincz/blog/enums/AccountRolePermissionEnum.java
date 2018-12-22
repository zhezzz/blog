package com.lincz.blog.enums;

import java.util.HashSet;

public enum AccountRolePermissionEnum {
    ROLE_0("ROLE_ROOT","根管理员,拥有所有权限，可以分配普通管理员",null),
    ROLE_1("ROLE_ADMIN","普通管理员，除设置管理员之外的所有权限",null),
    ROLE_2("ROLE_USER","普通用户",null),

    ;
    private String roleName;
    private String description;
    private HashSet<String> permission;

    AccountRolePermissionEnum(String roleName, String description, HashSet<String> permission) {
        this.roleName = roleName;
        this.description = description;
        this.permission = permission;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashSet<String> getPermission() {
        return permission;
    }

    public void setPermission(HashSet<String> permission) {
        this.permission = permission;
    }
}
