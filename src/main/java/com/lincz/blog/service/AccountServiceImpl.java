package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Authority;
import com.lincz.blog.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Page<Account> paginateGetAllAccount(Pageable pageable) {
		return accountRepository.findAll(pageable);
	}

	@Override
	public Account getAccountByUsername(String username) {
		return accountRepository.findAccountByUsername(username);
	}

	@Override
	public Account getAccountByAccountId(Long accountId) {
		return accountRepository.findById(accountId).orElse(null);
	}

	@Override
	public Account updateAccountAuthority(Long accountId, Set<Authority> authorities) {
		Account account = accountRepository.findById(accountId).orElse(null);
		account.setAuthorities(authorities);
		return account;
	}

	@Override
	public Account updateAccountInfo(Long accountId, Account accountDTO) {
		Account account = accountRepository.findById(accountId).orElse(null);
		account.setEmail(accountDTO.getEmail());
		account.setPassword(accountDTO.getPassword());
		return accountRepository.save(account);
	}

	@Override
	public Account getAccountByEmail(String email) {
		return accountRepository.findAccountByEmail(email);
	}

	@Override
	public Account createAccount(Account accountDTO) {
		Account account = new Account(accountDTO.getUsername(), accountDTO.getPassword(), accountDTO.getEmail());
		return accountRepository.save(account);
	}

	@Override
	public void deleteAccountByAccountId(Long accountId) {
		accountRepository.deleteById(accountId);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = getAccountByUsername(username);
		if (account == null) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}
		account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
		return account;
	}

	@Override
	public boolean isAccountExists(Long accountId) {
		return accountRepository.existsById(accountId);
	}
}
