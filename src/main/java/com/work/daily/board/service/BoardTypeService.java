package com.work.daily.board.service;

import com.work.daily.board.dto.BoardTypeDto;
import com.work.daily.domain.entity.BoardType;
import com.work.daily.domain.repository.BoardTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardTypeService {

    private final BoardTypeRepository boardTypeRepository;

    public List<BoardTypeDto> findAllBoardType(){
        List<BoardType> findAllBoardType = boardTypeRepository.findAll();
        return findAllBoardType.stream().map(BoardTypeDto::new).collect(Collectors.toList());
    }
}
