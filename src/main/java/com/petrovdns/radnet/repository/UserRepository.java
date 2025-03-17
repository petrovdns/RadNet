package com.petrovdns.radnet.repository;

import com.petrovdns.radnet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findUserByUserName(@Param("userName") String userName);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(Long id);
    boolean existsUserByUserName(String userName);
}
