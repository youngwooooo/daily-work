package com.work.daily.apiserver.board;

import com.work.daily.access.ReturnResult;
import com.work.daily.access.dto.ResponseDto;
import com.work.daily.board.dto.RequestBoardDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


}
