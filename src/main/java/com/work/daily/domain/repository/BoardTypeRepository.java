package com.work.daily.domain.repository;

import com.work.daily.domain.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTypeRepository extends JpaRepository<BoardType, Long> {
}
