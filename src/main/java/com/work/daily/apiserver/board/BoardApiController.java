package com.work.daily.apiserver.board;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.ResponseDto;
import com.work.daily.board.dto.RequestBoardDto;
import com.work.daily.board.dto.ResponseBoardDto;
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

    @GetMapping("/btest")
    public Page<ResponseBoardDto> test(@PageableDefault(size = 10) Pageable pageable
                                        , @RequestParam(required = false, defaultValue = "") String search
                                        , @RequestParam(required = false, defaultValue = "") String category){
        return boardService.findAllBoard(pageable, search, category);
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
                                                    , @RequestPart(value = "fileSeqList", required = false) List<Long> fileSeqList
    ) throws IOException {
        log.info("requestBoardDto : " + requestBoardDto.toString());
        if(files != null){
            for(MultipartFile file : files){
                log.info("업로드 파일 : " + file.getOriginalFilename());
            }
        }

        log.info("fileSeqList Size : " + fileSeqList.size());
        if(fileSeqList != null){
            for(int i=0; i<fileSeqList.size(); i++){
                log.info("fileSeq : " + fileSeqList.get(i));
            }
        }

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

}
