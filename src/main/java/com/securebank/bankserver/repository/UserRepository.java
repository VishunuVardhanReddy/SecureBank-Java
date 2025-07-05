package com.securebank.bankserver.repository;

import com.securebank.bankserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByAccountNumberAndPassword(String accountNumber, String password);
}
