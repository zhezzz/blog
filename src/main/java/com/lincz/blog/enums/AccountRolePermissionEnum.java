package com.lincz.blog.enums;

import java.util.HashSet;

public enum AccountRolePermissionEnum {
    ROLE_ROOT("ROOT","根管理员,拥有所有权限，可以分配普通管理员",new String[]{"/**"}),
    ROLE_ADMIN("ADMIN","普通管理员，除设置管理员之外的所有权限",new String[]{"/","/register","/resources/**","/login","/account/**","/article/**","/category/**","tag/**","/admin/**"}),
    ROLE_USER("USER","普通用户",new String[]{"/","/register","/resources/**","/login","/account/**","/article/**","/category/**","tag/**"}),

    ;
    private String roleName;
    private String description;
    private String[] permission;

    AccountRolePermissionEnum(String roleName, String description, String[] permission) {
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

    public String[] getPermission() {
        return permission;
    }

    public void setPermission(String[] permission) {
        this.permission = permission;
    }
}
