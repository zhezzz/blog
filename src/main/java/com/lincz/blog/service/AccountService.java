package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

public interface AccountService extends UserDetailsService {

    Page<Account> paginateGetAllAccount(Pageable pageable);

    Account getAccountByUsername(String username);

    Account getAccountByEmail(String email);

    Account getAccountByAccountId(Long accountId);

    Account updateAccountInfo(Long accountId, Account accountDTO);

    Account updateAccountStatus(Long accountId, Account accountDTO);

    Account createAccount(Account accountDTO);

    void deleteAccountByAccountId(Long accountId);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean isAccountExists(Long accountId);
}
