package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    Page<Board> findAllBoard(Pageable pageable, String search, String category);
    Optional<Board> findOneBoard(long boardSeq);
}
