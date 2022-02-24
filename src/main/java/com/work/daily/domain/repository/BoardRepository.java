package com.work.daily.domain.repository;

import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.custom.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    Optional<Board> findByUser(User user);
}
