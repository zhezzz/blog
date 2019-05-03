package com.lincz.blog.repository;

import com.lincz.blog.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByUsername(String username);

    Account findAccountByEmail(String email);

    Page<Account> findAllByEnabled(Boolean enable, Pageable pageable);

    Long countAllByCreateDateBetween(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);

}
