package com.work.daily.board.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.board.dto.RequestBoardCommentDto;
import com.work.daily.board.dto.ResponseBoardCommentDto;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.BoardCommentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BoardCommentService :: 단위 테스트")
public class BoardCommentServiceTest {

    @Mock
    private BoardCommentRepository boardCommentRepository;

    @InjectMocks
    private BoardCommentService boardCommentService;

    @Test
    @Order(1)
    @DisplayName("전체 댓글 조회")
    public void findAllParentBoardComment() {
        // given
        long boardSeq = 1;
        Pageable pageable = PageRequest.of(0, 10);

        List<BoardComment> list = new ArrayList<>();
        for(long i=0; i<20; i++){
            BoardComment boardComment = BoardComment.builder()
                                            .board(Board.builder().boardSeq(1).boardNm("게시글1").boardDesc("내용").build())
                                            .commentSeq((i+1))
                                            .commentDesc((i+1) + "번 댓글")
                                            .user(User.builder().userSeq((i+1)).userId("user" + (i+1)).userNm("유저" + (i+1)).build())
                                            .delYn("N")
                                            .parentCommentSeq(0)
                                            .build();

            list.add(boardComment);
        }

        Page<BoardComment> paging = new PageImpl<>(list, pageable, list.size());

        given(boardCommentRepository.findAllParentBoardComment(anyLong(), any(Pageable.class))).willReturn(paging);

        // when
        Page<ResponseBoardCommentDto> findAllParentBoardComment = boardCommentService.findAllParentBoardComment(boardSeq, pageable);

        // then
        assertThat(findAllParentBoardComment.getSize()).isEqualTo(10);
        assertThat(findAllParentBoardComment.getTotalElements()).isEqualTo(20);
        for(int i=0; i<findAllParentBoardComment.getTotalElements(); i++){
            assertThat(findAllParentBoardComment.getContent().get(i).getParentCommentSeq()).isEqualTo(0);
        }
    }

    @Test
    @Order(2)
    @DisplayName("전체 답글 조회")
    public void findAllChildBoardComment() {
        // given
        long boardSeq = 1;
        Pageable pageable = PageRequest.of(0, 10);

        List<BoardComment> list = new ArrayList<>();
        for(long i=0; i<20; i++){
            BoardComment boardComment = BoardComment.builder()
                    .board(Board.builder().boardSeq(1).boardNm("게시글1").boardDesc("내용").build())
                    .commentSeq((i+1))
                    .commentDesc((i+1) + "번 댓글")
                    .user(User.builder().userSeq((i+1)).userId("user" + (i+1)).userNm("유저" + (i+1)).build())
                    .delYn("N")
                    .parentCommentSeq((i))
                    .build();

            list.add(boardComment);
        }

        given(boardCommentRepository.findAllChildBoardComment(anyLong())).willReturn(list);

        // when
        List<ResponseBoardCommentDto> findAllChildBoardComment = boardCommentService.findAllChildBoardComment(boardSeq);

        // then
        assertThat(findAllChildBoardComment.size()).isEqualTo(20);
        for(int i=0; i<findAllChildBoardComment.size(); i++){
            assertThat(findAllChildBoardComment.get(i).getParentCommentSeq()).isEqualTo(i);
        }
    }

    @Test
    @Order(3)
    @DisplayName("댓글/답글 등록")
    public void save() {
        // given
        RequestBoardCommentDto requestBoardCommentDto = RequestBoardCommentDto.builder()
                                                            .board(Board.builder().boardSeq(1).boardNm("게시글1").boardDesc("내용").build())
                                                            .commentSeq(1)
                                                            .commentDesc(1 + "번 댓글")
                                                            .user(User.builder().userSeq(1).userId("user1").userNm("유저").build())
                                                            .delYn("N")
                                                            .parentCommentSeq(0)
                                                            .build();

        BoardComment boardComment = requestBoardCommentDto.toEntity();

        given(boardCommentRepository.save(any(BoardComment.class))).willReturn(boardComment);

        // when
        String result = boardCommentService.save(requestBoardCommentDto);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());
    }

    @Test
    @Order(4)
    @DisplayName("댓글/답글 수정")
    public void modify() {
        // given
        RequestBoardCommentDto requestBoardCommentDto = RequestBoardCommentDto.builder()
                .board(Board.builder().boardSeq(1).boardNm("게시글1").boardDesc("내용").build())
                .commentSeq(1)
                .commentDesc(1 + "번 댓글")
                .user(User.builder().userSeq(1).userId("user1").userNm("유저").build())
                .delYn("N")
                .parentCommentSeq(0)
                .build();

        BoardComment boardComment = requestBoardCommentDto.toEntity();

        given(boardCommentRepository.findById(anyLong())).willReturn(Optional.ofNullable(boardComment));

        // when
        String result = boardCommentService.modify(requestBoardCommentDto);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());

    }

    @Test
    @Order(5)
    @DisplayName("댓글/답글 삭제")
    public void delete() {
        // given
        long commentSeq = 1;
        BoardComment boardComment = BoardComment.builder()
                                        .board(Board.builder().boardSeq(1).boardNm("게시글1").boardDesc("내용").build())
                                        .commentSeq(1)
                                        .commentDesc(1 + "번 댓글")
                                        .user(User.builder().userSeq(1).userId("user1").userNm("유저").build())
                                        .delYn("N")
                                        .parentCommentSeq(0)
                                        .build();

        List<BoardComment> list = new ArrayList<>();
        for(long i=0; i<10; i++){
            BoardComment ChildBoardComment = BoardComment.builder()
                                                .board(Board.builder().boardSeq(1).boardNm("게시글1").boardDesc("내용").build())
                                                .commentSeq(i+1)
                                                .commentDesc((i+1) + "번 댓글")
                                                .user(User.builder().userSeq(1).userId("user1").userNm("유저").build())
                                                .delYn("N")
                                                .parentCommentSeq(1)
                                                .build();

            list.add(ChildBoardComment);
        }

        given(boardCommentRepository.findById(anyLong())).willReturn(Optional.ofNullable(boardComment));
        given(boardCommentRepository.findAllChildBoardComment(anyLong())).willReturn(list);

        // when
        String result = boardCommentService.delete(commentSeq);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());

    }
}