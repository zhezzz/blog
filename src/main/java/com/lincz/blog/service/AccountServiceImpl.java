package com.lincz.blog.service;

import com.lincz.blog.entity.Account;
import com.lincz.blog.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
    public Page<Account> paginateGetAccountsByEnable(boolean enable, Pageable pageable) {
        return accountRepository.findAllByEnabled(enable, pageable);
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
    public Account updateAccountInfo(Long accountId, Account accountDTO) {
        Account account = accountRepository.findById(accountId).orElse(null);
        account.setEmail(accountDTO.getEmail());
        account.setPassword(accountDTO.getPassword());
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccountStatus(Long accountId, Account accountDTO) {
        Account account = accountRepository.findById(accountId).orElse(null);
        account.setEnabled(accountDTO.isEnabled());
        account.setRole(accountDTO.getRole());
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Account createAccount(Account accountDTO) {
        Account account = new Account(accountDTO.getUsername(), accountDTO.getPassword(), accountDTO.getEmail(), "default-avatar.jpg", "USER", true, true, true, true);
        return accountRepository.save(account);
    }

    @Override
    public void updateAccountAvatar(Long accountId, MultipartFile avatar) throws IOException {
        Account account = accountRepository.findById(accountId).orElse(null);
        File oldFile = new File("/root/data/account/avatar/" + account.getAvatar());
        if (oldFile.exists()){
            oldFile.delete();
        }
        String avatarFileName = accountId + avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf("."));
        File file = new File("/root/data/account/avatar");
        if (!file.exists()) {
            file.mkdirs();
        }
        avatar.transferTo(new File(file.toString() + "/" + avatarFileName));
        account.setAvatar(avatarFileName);
    }

    @Override
    public void deleteAccountByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        File file = new File("/root/data/account/avatar/" + account.getAvatar());
        if (file.exists()){
            file.delete();
        }
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
