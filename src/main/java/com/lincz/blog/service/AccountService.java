package com.lincz.blog.service;


import com.lincz.blog.entity.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface AccountService extends UserDetailsService {

    List<Account> getAllAccount();

    Account getAccountByUsername(String username);

    Account getAccountByEamil(String email);

    Account getAccountByAccountId(String accountId);

    void updataAccount(Account account);

    Account createAccount(Account account);

    void deleteAccountByAccountId(String accountId);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
