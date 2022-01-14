package com.work.daily.domain.repository;

import com.work.daily.domain.entity.User;
import com.work.daily.domain.entity.UserPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserPK> {
    Optional<User> findByUserId(String userId);
}
