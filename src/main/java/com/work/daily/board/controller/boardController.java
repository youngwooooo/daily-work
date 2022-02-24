package com.work.daily.board.controller;

import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class boardController {

    private final BoardService boardService;

    /**
     * 커뮤니티(게시글) VIEW
     * @param model
     * @return
     */
    @GetMapping("/boards")
    public String boards(Model model){
        List<ResponseBoardDto> findAll = boardService.findAllBoard();
        model.addAttribute("board", findAll);
        return "/contents/board/boards";
    }

    /**
     * 게시글 등록 VIEW
     * @return
     */
    @GetMapping("/board")
    public String createBoardForm(){
        return "/contents/board/createBoard";
    }

    /**
     * 게시글 상세 조회 VIEW
     * @param boardSeq
     * @param model
     * @return
     */
    @GetMapping("/board/{boardSeq}")
    public String detailBoardForm(@PathVariable("boardSeq") long boardSeq, Model model){
        ResponseBoardDto findOneBoard = boardService.findOneBoard(boardSeq);
        model.addAttribute("board", findOneBoard);
        return "/contents/board/detailBoard";
    }

    /**
     * 게시글 수정 VIEW
     * @param boardSeq
     * @param model
     * @return
     */
    @GetMapping("/board/{boardSeq}/modify")
    public String modifyBoardForm(@PathVariable("boardSeq") long boardSeq, Model model){
        ResponseBoardDto findOneBoard = boardService.findOneBoard(boardSeq);
        model.addAttribute("board", findOneBoard);
        return "/contents/board/modifyBoard";
    }
}
