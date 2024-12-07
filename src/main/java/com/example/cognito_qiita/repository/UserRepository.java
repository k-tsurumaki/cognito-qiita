package com.example.cognito_qiita.repository;

import com.example.cognito_qiita.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByDeletedFlgFalse();
    Optional<User> findByEmailAndDeletedFlgFalse(String email);
}
