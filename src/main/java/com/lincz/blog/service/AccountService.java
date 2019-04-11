package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AccountService extends UserDetailsService {

    Page<Account> paginateGetAllAccount(Pageable pageable);

    Account getAccountByUsername(String username);

    Account getAccountByEmail(String email);

    Account getAccountByAccountId(Long accountId);

    Account updateAccountInfo(Long accountId, Account accountDTO);

    void updateAccountAvatar(Long accountId, MultipartFile avatar) throws IOException;

    Account updateAccountStatus(Long accountId, Account accountDTO);

    Account createAccount(Account accountDTO);

    void deleteAccountByAccountId(Long accountId);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean isAccountExists(Long accountId);
}
