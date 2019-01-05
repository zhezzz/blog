package com.lincz.blog.service;
import com.lincz.blog.entity.Account;
import com.lincz.blog.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Account getAccountByAccountId(Long accountId) {

        return accountRepository.findById(accountId).orElse(null);
    }

    @Transactional
    @Override
    public Account updateAccountInfo(Long accountId,Account formAccount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        account.setEmail(formAccount.getEmail());
        account.setPassword(formAccount.getPassword());
        return account;
    }

    @Transactional
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
