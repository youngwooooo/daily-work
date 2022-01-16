package com.work.daily.domain.repository;

import com.work.daily.domain.entity.User;
import com.work.daily.domain.pk.UserPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UserPK> {
    Optional<User> findByUserId(String userId);
}
