package com.lincz.blog.service;

import com.lincz.blog.entity.Authority;

import java.util.List;

public interface AuthorityService {

    Authority getAuthorityByAuthorityId(Long authorityId);

    List<Authority> getAllAuthorities();

    Authority createAuthority(Authority authorityDTO);

    void deleteAuthorityByAuthorityId(Long authorityId);

    Authority updateAuthority(Long authorityId, Authority authorityDTO);
}
