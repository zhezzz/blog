package com.lincz.blog.service;

import com.lincz.blog.entity.Authority;
import com.lincz.blog.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public List<Authority> getAllAuthority() {
        return authorityRepository.findAll();
    }

    @Override
    public Authority createAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public void deleteAuthorityByAuthorityId(Long authorityId) {
        authorityRepository.deleteById(authorityId);
    }

    @Override
    public Authority updateAuthority(Long authorityId, Authority formAuthority) {
        Authority authority = authorityRepository.findById(authorityId).orElse(null);
        authority.setAuthorityName(formAuthority.getAuthorityName());
        return authority;
    }
}
