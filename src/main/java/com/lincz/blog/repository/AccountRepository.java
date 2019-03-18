package com.lincz.blog.repository;

import com.lincz.blog.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findAccountByUsername(String username);

	Account findAccountByEmail(String email);

}
