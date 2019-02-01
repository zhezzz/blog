package com.lincz.blog.service;

import com.lincz.blog.entity.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> getAllPermission();

    Permission createPermission(Permission permission);

    void deletePermissionByPermissionId(Long permissionId);

    Permission updatePermission(Long permissionId,Permission permission);
}
