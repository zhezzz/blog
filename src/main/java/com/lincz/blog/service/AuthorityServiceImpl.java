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
	public Authority getAuthorityByAuthorityId(Long authorityId) {
		return authorityRepository.findById(authorityId).orElse(null);
	}

	@Override
	public List<Authority> getAllAuthorities() {
		return authorityRepository.findAll();
	}

	@Override
	public Authority createAuthority(Authority authorityDTO) {
		Authority authority = new Authority(authorityDTO.getAuthorityName());
		return authorityRepository.save(authority);
	}

	@Override
	public void deleteAuthorityByAuthorityId(Long authorityId) {
		authorityRepository.deleteById(authorityId);
	}

	@Override
	public Authority updateAuthority(Long authorityId, Authority authorityDTO) {
		Authority authority = authorityRepository.findById(authorityId).orElse(null);
		authority.setAuthorityName(authorityDTO.getAuthorityName());
		return authority;
	}
}
