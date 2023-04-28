package com.example.messageservice.repository;

import com.example.messageservice.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

  Account findByEmail(String email);

  List<Account> findAllByEmailContaining(String key);

  Account findByEmailAndPassword(String email, String password);

}
