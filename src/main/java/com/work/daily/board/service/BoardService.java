package com.work.daily.board.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.board.dto.RequestBoardDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.boardfile.service.BoardFileService;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileService boardFileService;

    /**
     * 전체 게시글 조회
     * @return
     */
    @Transactional(readOnly = true)
    public List<ResponseBoardDto> findAllBoard(){
        return boardRepository.findAllBoard().stream().map(ResponseBoardDto::new).collect(Collectors.toList());
    }

    /**
     * 게시글 단건(상세) 조회
     * @param boardSeq
     * @return
     */
    @Transactional(readOnly = true)
    public ResponseBoardDto findOneBoard(long boardSeq){
        Optional<Board> findBoard = boardRepository.findOneBoard(boardSeq);
        if(!findBoard.isPresent()){
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }

        ResponseBoardDto responseBoardDto = ResponseBoardDto.builder()
                .boardSeq(findBoard.get().getBoardSeq())
                .userSeq(findBoard.get().getUser().getUserSeq())
                .userId(findBoard.get().getUser().getUserId())
                .userNm(findBoard.get().getUser().getUserNm())
                .profileImage(findBoard.get().getUser().getProfileImage())
                .insDtm(findBoard.get().getInsDtm())
                .boardNm(findBoard.get().getBoardNm())
                .boardDesc(findBoard.get().getBoardDesc())
                .delYn(findBoard.get().getDelYn())
                .temporaryYn(findBoard.get().getTemporaryYn())
                .boardType(findBoard.get().getBoardType())
                .boardFileList(findBoard.get().getBoardFileList())
                .build();

        return responseBoardDto;
    }


    /**
     * 게시글 등록
     * @param requestBoardDto
     * @param files
     * @return
     * @throws IOException
     */
    @Transactional
    public String save(RequestBoardDto requestBoardDto, List<MultipartFile> files) throws IOException {

        Board SavedBoard = boardRepository.save(requestBoardDto.toEntity());

        if(files != null){
            String result = boardFileService.save(files, SavedBoard);

            if(ReturnResult.SUCCESS.getValue().equals(result)){
                return ReturnResult.SUCCESS.getValue();
            }
        }

        return ReturnResult.SUCCESS.getValue();
    }
}
