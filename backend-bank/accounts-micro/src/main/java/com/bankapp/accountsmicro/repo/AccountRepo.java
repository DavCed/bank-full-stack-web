package com.bankapp.accountsmicro.repo;

import com.bankapp.accountsmicro.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
    Optional<Account> findByAccountNumber(Integer accountNumber);
    Optional<List<Account>> findAllByUserId(Integer userId);
}