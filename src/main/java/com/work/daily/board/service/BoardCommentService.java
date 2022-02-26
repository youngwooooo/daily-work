package com.work.daily.board.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.board.dto.RequestBoardCommentDto;
import com.work.daily.board.dto.ResponseBoardCommentDto;
import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.repository.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;

    /**
     * 전체 댓글 조회
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<ResponseBoardCommentDto> findAllParentBoardComment(long boardSeq, Pageable pageable){
        return boardCommentRepository.findAllParentBoardComment(boardSeq, pageable).map(ResponseBoardCommentDto::new);
    }

    /**
     * 전체 답글 조회
     * @param boardSeq
     * @return
     */
    @Transactional(readOnly = true)
    public List<ResponseBoardCommentDto> findAllChildBoardComment(long boardSeq){
        return boardCommentRepository.findAllChildBoardComment(boardSeq).stream().map(ResponseBoardCommentDto::new).collect(Collectors.toList());
    }

    /**
     * 댓글/답글 등록
     * @param requestBoardCommentDto
     * @return
     */
    @Transactional
    public String save(RequestBoardCommentDto requestBoardCommentDto){
        boardCommentRepository.save(requestBoardCommentDto.toEntity());
        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 댓글/답글 수정
     * @param requestBoardCommentDto
     * @return
     */
    @Transactional
    public String modify(RequestBoardCommentDto requestBoardCommentDto) {
        Optional<BoardComment> findBoardComment = boardCommentRepository.findById(requestBoardCommentDto.getCommentSeq());
        if(!findBoardComment.isPresent()){
            throw new IllegalArgumentException("해당 댓글/답글이 존재하지 않습니다. 댓글/답글 번호 : " + requestBoardCommentDto.getCommentSeq());
        }

        findBoardComment.get().modifyBoardComment(requestBoardCommentDto.getCommentDesc());

        return ReturnResult.SUCCESS.getValue();
    }

    /**
     * 댓글/답글 삭제
     * @description 댓글/답글 삭제여부 Y로 변경
     * @param commentSeq
     * @return
     */
    @Transactional
    public String delete(long commentSeq) {
        Optional<BoardComment> findBoardComment = boardCommentRepository.findById(commentSeq);
        if(!findBoardComment.isPresent()){
            throw new IllegalArgumentException("해당 댓글/답글이 존재하지 않습니다. 댓글/답글 번호 : " + commentSeq);
        }

        findBoardComment.get().deleteBoardComment("Y");

        return ReturnResult.SUCCESS.getValue();
    }
}
