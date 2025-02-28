package com.petrovdns.radnet.repository;

import com.petrovdns.radnet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String userName);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(Long id);
}
