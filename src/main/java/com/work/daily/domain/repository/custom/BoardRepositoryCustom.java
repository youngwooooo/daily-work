package com.work.daily.domain.repository.custom;

import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardRepositoryCustom {
    Page<ResponseBoardDto> findAllBoard(Pageable pageable, String search, String category);
    Optional<Board> findOneBoard(long boardSeq);
}
