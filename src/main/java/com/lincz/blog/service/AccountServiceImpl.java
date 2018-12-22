package com.lincz.blog.service;
import com.lincz.blog.entity.Account;
import com.lincz.blog.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Account getAccountByAccountId(String accountId) {

        return accountRepository.findById(accountId).orElse(null);
    }

    @Override
    public void updataAccount(Account account) {
        accountRepository.save(account);
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
    public void deleteAccountByAccountId(String accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Account accountByUsernameOrEmail = getAccountByUsername(usernameOrEmail);
        if (accountByUsernameOrEmail == null){
            accountByUsernameOrEmail = getAccountByEamil(usernameOrEmail);
        }
        if (accountByUsernameOrEmail == null){
            throw new UsernameNotFoundException("找不到用户名或邮箱");
        }

        User user = new User(accountByUsernameOrEmail.getUsername(),bCryptPasswordEncoder.encode(accountByUsernameOrEmail.getPassword()),
                accountByUsernameOrEmail.isEnabled(), accountByUsernameOrEmail.isAccountNonExpired(),
                accountByUsernameOrEmail.isCredentialsNonExpired(), accountByUsernameOrEmail.isAccountNonLocked(),
                accountByUsernameOrEmail.getAuthorities());
        return user;


   }


}
