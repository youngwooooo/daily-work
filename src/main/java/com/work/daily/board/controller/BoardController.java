package com.work.daily.board.controller;

import com.work.daily.board.dto.BoardTypeDto;
import com.work.daily.board.dto.ResponseBoardCommentDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.board.service.BoardCommentService;
import com.work.daily.board.service.BoardService;
import com.work.daily.board.service.BoardTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardCommentService boardCommentService;
    private final BoardTypeService boardTypeService;

    /**
     * 전체 게시글(커뮤니티) VIEW
     * @param pageable
     * @param search
     * @param category
     * @param model
     * @return
     */
    @GetMapping("/boards")
    public String boards(@PageableDefault(size = 10) Pageable pageable
            , @RequestParam(required = false, defaultValue = "") String search
            , @RequestParam(required = false, defaultValue = "") String category
            , Model model)
    {
        Page<ResponseBoardDto> findAllBoard = boardService.findAllBoard(pageable, search, category);
        List<BoardTypeDto> findAllBoardType = boardTypeService.findAllBoardType();

        int firstPage = 1;  // 첫번째 페이지
        int lastPage = findAllBoard.getTotalPages(); // 마지막 페이지(게시글 전체 개수)
        int startPage = Math.max(firstPage + 1, findAllBoard.getPageable().getPageNumber() - 2); // 시작 페이지
        int endPage = Math.min(lastPage - 1, findAllBoard.getPageable().getPageNumber() + 4);    // 끝 페이지
        long totalCount = findAllBoard.getTotalElements();

        model.addAttribute("board", findAllBoard);
        model.addAttribute("boardType", findAllBoardType);

        model.addAttribute("search", search);
        model.addAttribute("category", category);

        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalCount", totalCount);


        return "contents/board/boards";
    }

    /**
     * 게시글 등록 VIEW
     * @return
     */
    @GetMapping("/board")
    public String createBoardForm(Model model){
        List<BoardTypeDto> findAllBoardType = boardTypeService.findAllBoardType();
        model.addAttribute("boardType", findAllBoardType);
        return "contents/board/createBoard";
    }

    /**
     * 게시글 상세 조회 VIEW
     * @param boardSeq
     * @param model
     * @return
     */
    @GetMapping("/board/{boardSeq}")
    public String detailBoardForm(@PathVariable("boardSeq") long boardSeq, Model model, @PageableDefault(size = 10) Pageable pageable){
        // 게시글 단건(상세) 조회
        ResponseBoardDto findOneBoard = boardService.findOneBoard(boardSeq);
        // 조회수 증가
        boardService.increaseViewCount(boardSeq);
        // 전체 댓글 조회
        Page<ResponseBoardCommentDto> findAllParentBoardComment = boardCommentService.findAllParentBoardComment(boardSeq, pageable);
        // 전체 답글 조회
        List<ResponseBoardCommentDto> findAllChildBoardComment = boardCommentService.findAllChildBoardComment(boardSeq);
        // 총 댓글 + 답글 개수
        long boardCommentTotalCount = findAllParentBoardComment.getTotalElements() + findAllChildBoardComment.size();

        model.addAttribute("board", findOneBoard);
        model.addAttribute("boardParentComment", findAllParentBoardComment);
        model.addAttribute("boardChildComment", findAllChildBoardComment);

        model.addAttribute("boardCommentTotalCount", boardCommentTotalCount);

        return "contents/board/detailBoard";
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
        List<BoardTypeDto> findAllBoardType = boardTypeService.findAllBoardType();

        model.addAttribute("boardType", findAllBoardType);
        model.addAttribute("board", findOneBoard);

        return "contents/board/modifyBoard";
    }
}
