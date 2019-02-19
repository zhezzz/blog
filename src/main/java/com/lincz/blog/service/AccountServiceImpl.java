package com.lincz.blog.service;
import com.lincz.blog.entity.Account;
import com.lincz.blog.entity.Authority;
import com.lincz.blog.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
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
    public Account updateAccountInfo(Long accountId,Account formAccount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        account.setEmail(formAccount.getEmail());
        account.setPassword(formAccount.getPassword());
        return account;
    }

    @Override
    public Account updateAccountAvatar(Long accountId, String avatar) {
        Account account = accountRepository.findById(accountId).orElse(null);
        account.setAvatar(avatar);
        return account;
    }

    @Override
    public Account getAccountByEamil(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccountByAccountId(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = getAccountByUsername(username);
        if (account == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        return account;
   }


}
