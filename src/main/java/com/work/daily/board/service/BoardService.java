package com.work.daily.board.service;

import com.work.daily.board.dto.BoardDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<ResponseBoardDto> findAllBoard(){
        return boardRepository.findAllBoard().stream().map(ResponseBoardDto::new).collect(Collectors.toList());
    }
}
