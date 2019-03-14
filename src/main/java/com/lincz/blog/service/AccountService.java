package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Authority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;

public interface AccountService extends UserDetailsService {

	List<Account> getAllAccount();

	Account getAccountByUsername(String username);

	Account getAccountByEmail(String email);

	Account getAccountByAccountId(Long accountId);

	Account updateAccountAuthority(Long accountId, Set<Authority> authorities);

	Account updateAccountInfo(Long accountId, Account account);

	Account updateAccountAvatar(Long accountId, String avatar);

	Account createAccount(Account account);

	void deleteAccountByAccountId(Long accountId);

	@Override
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
