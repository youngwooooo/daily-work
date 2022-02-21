package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    List<Board> findAllBoard();
    Optional<Board> findOneBoard(long boardSeq);
}
