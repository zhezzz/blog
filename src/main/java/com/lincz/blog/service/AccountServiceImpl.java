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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public Page<Account> paginateGetAccountsByEnable(Boolean enable, Pageable pageable) {
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
        Account account = new Account(accountDTO.getUsername(), accountDTO.getPassword(), accountDTO.getEmail(), "defaultavatar.jpg", "USER", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        return accountRepository.save(account);
    }

    @Override
    public void updateAccountAvatar(Long accountId, MultipartFile avatar) throws IOException {
        Account account = accountRepository.findById(accountId).orElse(null);
        File oldFile = new File("/tmp/data/account/avatar/" + account.getAvatar());
        if (oldFile.exists()){
            oldFile.delete();
        }
        String avatarFileName = accountId + avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf("."));
        File file = new File("/tmp/data/account/avatar");
        if (!file.exists()) {
            file.mkdirs();
        }
        avatar.transferTo(new File(file.toString() + "/" + avatarFileName));
        account.setAvatar(avatarFileName);
        accountRepository.save(account);
    }

    @Override
    public void deleteAccountByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        File file = new File("/tmp/data/account/avatar/" + account.getAvatar());
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
    public Boolean isAccountExists(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    @Override
    public Map<Month, Long> getAccountQuantityMonthly() {
        Map<Month, Long> accountsLineChart = new LinkedHashMap<>();
        LocalDateTime now = LocalDateTime.now();
        int years = now.getYear();
        int month = now.getMonthValue();
        for (int i = 1; i <= month; i++) {
            LocalDateTime monthStartLocalDateTime = LocalDateTime.of(years, Month.of(i), 1, 0, 0, 0, 0);
            LocalDateTime monthEndLocalDateTime = monthStartLocalDateTime.plusMonths(1).minusDays(monthStartLocalDateTime.getDayOfMonth()).plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999999);
            accountsLineChart.put(Month.of(i), accountRepository.countAllByCreateDateBetween(monthStartLocalDateTime, monthEndLocalDateTime));
        }
        return accountsLineChart;
    }
}
