package com.bankapp.accountsmicro.repo;

import com.bankapp.accountsmicro.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> { }