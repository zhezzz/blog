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
	@PostMapping(value = "/")
	public Authority addAuthority(@RequestBody Authority authorityDTO) {
		Authority authority = authorityService.createAuthority(authorityDTO);
		return authority;
	}

	//查找所有权限
	@GetMapping(value = "/")
	public List<Authority> getAllAuthority(){
		List<Authority> authorityList = authorityService.getAllAuthority();
		return authorityList;
	}

	//根据id查找权限
	@GetMapping(value = "/{authorityId}")
	public Authority getAuthorityByAuthorityId(@PathVariable Long authorityId){
		Authority authority = authorityService.getAuthorityByAuthorityId(authorityId);
		return authority;
	}


	// 删除权限
	@DeleteMapping(value = "/{authorityId}")
	public void deleteAuthority(@PathVariable Long authorityId) {
		authorityService.deleteAuthorityByAuthorityId(authorityId);
	}

	// 修改权限
	@PutMapping(value = "/{authorityId}")
	public Authority updateAuthority(@PathVariable Long authorityId, @RequestBody Authority authorityDTO) {
		return authorityService.updateAuthority(authorityId, authorityDTO);
	}

}
