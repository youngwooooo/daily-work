package com.work.daily.board.controller;

import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class boardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public String boards(Model model){
        List<ResponseBoardDto> findAll = boardService.findAllBoard();
        model.addAttribute("board", findAll);
        return "/contents/board/boards";
    }

    @GetMapping("/board")
    public String createBoardForm(){
        return "/contents/board/createBoard";
    }
}
