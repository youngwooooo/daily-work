package com.work.daily.apiserver.board;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.ResponseDto;
import com.work.daily.board.dto.RequestBoardCommentDto;
import com.work.daily.board.dto.RequestBoardDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.board.service.BoardCommentService;
import com.work.daily.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardApiController {

    private final BoardService boardService;
    private final BoardCommentService boardCommentService;

    @GetMapping("/btest/{boardSeq}")
    public ResponseBoardDto test(@PathVariable("boardSeq") long boardSeq){
        return boardService.findOneBoard(boardSeq);
    }

    /**
     * 게시글 등록
     * @param requestBoardDto
     * @param files
     * @return
     * @throws IOException
     */
    @PostMapping("/board")
    public ResponseEntity<ResponseDto> createBoard(@RequestPart(value = "requestBoardDto") RequestBoardDto requestBoardDto
                                                    , @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        String result = boardService.save(requestBoardDto, files);

        if(!ReturnResult.SUCCESS.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("게시글 등록이 실패하였습니다. 다시 시도해주세요.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.CREATED.value()).data(result).message("게시글 등록이 완료되었습니다.").build(), HttpStatus.CREATED);
    }

    /**
     * 게시글 수정
     * @description 1. 게시글 제목, 내용, 카테고리 수정
     *              2. 첨부파일 수정 시, 삭제 및 업로드
     * @param boardSeq
     * @param requestBoardDto
     * @param files
     * @param fileSeqList
     * @return
     * @throws IOException
     */
    @PatchMapping("/board/{boardSeq}")
    public ResponseEntity<ResponseDto> modifyBoard(@PathVariable("boardSeq") long boardSeq
                                                    , @RequestPart(value = "requestBoardDto") RequestBoardDto requestBoardDto
                                                    , @RequestPart(value = "files", required = false) List<MultipartFile> files
                                                    , @RequestPart(value = "fileSeqList", required = false) List<Long> fileSeqList) throws IOException
    {
        String result = boardService.modify(boardSeq, requestBoardDto, files, fileSeqList);

        if(!ReturnResult.SUCCESS.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("게시글 수정에 실패하였습니다. 다시 시도해주세요.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(result).message("게시글 수정이 완료되었습니다.").build(), HttpStatus.OK);
    }

    /**
     * 게시글 삭제
     * @description 게시글의 삭제여부를 N으로 수정
     * @param boardSeq
     * @return
     */
    @DeleteMapping("/board/{boardSeq}")
    public ResponseEntity<ResponseDto> deleteBoard(@PathVariable("boardSeq") long boardSeq){

        String result = boardService.delete(boardSeq);

        if(!ReturnResult.SUCCESS.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("게시글 삭제가 실패하였습니다. 다시 시도해주세요.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(result).message("게시글 삭제가 완료되었습니다.").build(), HttpStatus.OK);
    }

    /**
     * 댓글/답글 등록
     * @param requestBoardCommentDto
     * @return
     */
    @PostMapping("/board/comment")
    public ResponseEntity<ResponseDto> createBoardComment(@RequestBody RequestBoardCommentDto requestBoardCommentDto){
        String result = boardCommentService.save(requestBoardCommentDto);

        if(!ReturnResult.SUCCESS.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("댓글/답글 등록에 실패하였습니다. 다시 시도해주세요.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.CREATED.value()).data(result).message("댓글/답글 등록이 완료되었습니다.").build(), HttpStatus.CREATED);
    }

    /**
     * 댓글/답글 수정
     * @param commentSeq
     * @param requestBoardCommentDto
     * @return
     */
    @PatchMapping("/board/comment/{commentSeq}")
    public ResponseEntity<ResponseDto> modifyBoardComment(@PathVariable("commentSeq") long commentSeq, @RequestBody RequestBoardCommentDto requestBoardCommentDto){
        String result = boardCommentService.modify(requestBoardCommentDto);

        if(!ReturnResult.SUCCESS.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("댓글/답글 수정에 실패하였습니다. 다시 시도해주세요.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(result).message("댓글/답글 수정이 완료되었습니다.").build(), HttpStatus.OK);
    }

    /**
     * 댓글/답글 삭제
     * @description 댓글/답글 삭제여부 Y로 변경
     * @param commentSeq
     * @return
     */
    @DeleteMapping("/board/comment/{commentSeq}")
    public ResponseEntity<ResponseDto> deleteBoardComment(@PathVariable("commentSeq") long commentSeq){
        String result = boardCommentService.delete(commentSeq);

        if(!ReturnResult.SUCCESS.getValue().equals(result)){
            return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).data(result).message("댓글/답글 삭제에 실패하였습니다. 다시 시도해주세요.").build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseDto.builder().status(HttpStatus.OK.value()).data(result).message("댓글/답글 삭제가 완료되었습니다.").build(), HttpStatus.OK);
    }
}
