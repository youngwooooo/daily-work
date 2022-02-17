package com.work.daily.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class boardController {

    @GetMapping("/boards")
    public String boards(){
        return "/contents/board/boards";
    }

    @GetMapping("/board")
    public String createBoardForm(){
        return "/contents/board/createBoard";
    }
}
