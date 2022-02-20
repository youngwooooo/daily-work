package com.work.daily.domain.repository.custom;

import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.domain.entity.Board;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> findAllBoard();
}
