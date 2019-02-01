package com.lincz.blog.service;

import com.lincz.blog.entity.Authority;

import java.util.List;

public interface AuthorityService {

    List<Authority> getAllAuthority();

    Authority createAuthority(Authority authority);

    void deleteAuthorityByAuthorityId(Long authorityId);

    Authority updateAuthority(Long authorityId, Authority authority);
}
