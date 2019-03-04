package com.lincz.blog.controller;

import com.lincz.blog.entity.Authority;
import com.lincz.blog.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/authority")
public class AuthorityController {

	@Autowired
	private AuthorityService authorityService;

	// 权限管理页面
	@GetMapping(value = "/management")
	public ModelAndView authorityManagementPage() {
		List<Authority> authorityList = authorityService.getAllAuthority();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("AuthorityManagement");
		modelAndView.addObject(authorityList);
		return modelAndView;
	}

	// 增加权限
	@PostMapping(value = "/add")
	public Authority addAuthority(Authority formAuthority) {
		Authority authority = new Authority(formAuthority.getAuthorityName());
		authorityService.createAuthority(authority);
		return authority;
	}

	// 删除权限
	@DeleteMapping(value = "/delete/{authorityId}")
	public void deleteAuthority(@PathVariable Long authorityId) {
		authorityService.deleteAuthorityByAuthorityId(authorityId);
	}

	// 修改权限
	@PutMapping(value = "/update/{authorityId}")
	public Authority updateAuthority(@PathVariable Long authorityId, Authority formAuthority) {
		return authorityService.updateAuthority(authorityId, formAuthority);
	}

}
