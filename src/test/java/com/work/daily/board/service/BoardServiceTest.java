package com.work.daily.board.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.board.dto.RequestBoardDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.boardfile.service.BoardFileService;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.BoardRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BoardService :: 단위 테스트")
public class BoardServiceTest {

    private final Logger log = LoggerFactory.getLogger(BoardServiceTest.class);

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardFileService boardFileService;

    @InjectMocks
    private BoardService boardService;

    @Test
    @Order(1)
    @DisplayName("게시글 전체 조회")
    public void findAllBoard() {
        // given
        String search = "";
        String category = "";
        Pageable pageable = PageRequest.of(0, 10);
        List<ResponseBoardDto> responseBoardDtoList = new ArrayList<>();
        for(long i=0; i<20; i++){
            ResponseBoardDto responseBoardDto = ResponseBoardDto.builder()
                                                .boardSeq((i+1))
                                                .boardNm((i+1) + "번 게시글")
                                                .boardDesc((i+1) + "번 게시글 내용")
                                                .boardType("NORMAL")
                                                .userSeq(1)
                                                .userId("user1")
                                                .userNm("유저1")
                                                .profileImage("profileImageUser1.png")
                                                .temporaryYn("N")
                                                .delYn("N")
                                                .viewCount(0)
                                                .build();

            responseBoardDtoList.add(responseBoardDto);
        }
        Page<ResponseBoardDto> boardPage = new PageImpl<>(responseBoardDtoList, pageable, responseBoardDtoList.size());

        given(boardRepository.findAllBoard(any(Pageable.class), anyString(), anyString())).willReturn(boardPage);

        // when
        Page<ResponseBoardDto> findAllBoard = boardService.findAllBoard(pageable, search, category);

        // then
        assertThat(findAllBoard.getSize()).isEqualTo(10);
        assertThat(findAllBoard.getTotalElements()).isEqualTo(20);

    }

    @Test
    @Order(2)
    @DisplayName("게시글 단건(상세) 조회")
    public void findOneBoard() {
        // given
        long boardSeq = 1;
        Board board = Board.builder()
                        .boardSeq(1)
                        .boardNm("1번 게시글")
                        .boardDesc("1번 게시글 내용")
                        .boardType("NORMAL")
                        .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                        .temporaryYn("N")
                        .delYn("N")
                        .viewCount(0)
                        .build();

        given(boardRepository.findOneBoard(anyLong())).willReturn(Optional.ofNullable(board));

        // when
        ResponseBoardDto findOneBoard = boardService.findOneBoard(boardSeq);

        // then
        assertThat(findOneBoard.getBoardSeq()).isEqualTo(board.getBoardSeq());
        assertThat(findOneBoard.getBoardType()).isEqualTo(board.getBoardType());
        assertThat(findOneBoard.getUserSeq()).isEqualTo(board.getUser().getUserSeq());
        assertThat(findOneBoard.getUserId()).isEqualTo(board.getUser().getUserId());

    }

    @Test
    @Order(3)
    @DisplayName("게시글 등록")
    public void save() throws IOException {
        // given
        RequestBoardDto requestBoardDto = RequestBoardDto.builder()
                                            .boardNm("제목")
                                            .boardDesc("내용")
                                            .boardType("PR")
                                            .delYn("N")
                                            .temporaryYn("N")
                                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                                            .build();

        Board board = Board.builder()
                        .boardSeq(1)
                        .boardNm("제목")
                        .boardDesc("내용")
                        .boardType("PR")
                        .delYn("N")
                        .temporaryYn("N")
                        .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                        .build();

        List<MultipartFile> files = new ArrayList<>();
        for(int i=0; i<5; i++){
            MockMultipartFile file = new MockMultipartFile("boardImage" + (i+1) + ".png", "image", MediaType.IMAGE_PNG_VALUE, "image".getBytes());
            files.add(file);
        }

        given(boardRepository.save(any(Board.class))).willReturn(board);
        given(boardFileService.save(anyList(), any(Board.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when
        String result = boardService.save(requestBoardDto, files);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());
    }

    @Test
    @Order(4)
    @DisplayName("게시글 수정")
    public void modify() throws IOException {
        // given
        long boardSeq = 1;

        RequestBoardDto requestBoardDto = RequestBoardDto.builder()
                .boardNm("제목")
                .boardDesc("내용")
                .boardType("PR")
                .delYn("N")
                .temporaryYn("N")
                .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                .build();

        Board board = Board.builder()
                .boardSeq(1)
                .boardNm("제목")
                .boardDesc("내용")
                .boardType("PR")
                .delYn("N")
                .temporaryYn("N")
                .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                .build();

        List<MultipartFile> files = new ArrayList<>();
        for(int i=0; i<5; i++){
            MockMultipartFile file = new MockMultipartFile("boardImage" + (i+1) + ".png", "image", MediaType.IMAGE_PNG_VALUE, "image".getBytes());
            files.add(file);
        }

        List<Long> fileSeqList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        given(boardRepository.findById(anyLong())).willReturn(Optional.ofNullable(board));
        given(boardFileService.save(anyList(), any(Board.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when
        String result = boardService.modify(boardSeq, requestBoardDto, files, fileSeqList);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());

        verify(boardFileService).delete(anyList());
    }

    @Test
    @Order(5)
    @DisplayName("게시글 삭제")
    public void delete() {
        // given
        long boardSeq = 1;

        Board board = Board.builder()
                .boardSeq(1)
                .boardNm("제목")
                .boardDesc("내용")
                .boardType("PR")
                .delYn("N")
                .temporaryYn("N")
                .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                .build();

        given(boardRepository.findById(anyLong())).willReturn(Optional.ofNullable(board));

        // when
        String result = boardService.delete(boardSeq);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());
    }

    @Test
    @Order(6)
    @DisplayName("게시글 여러개 삭제")
    public void deleteMultiple() {
        // given
        List<Long> boardSeqList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<Board> boardList = new ArrayList<>();
        for(long i=0; i<3; i++){
            Board board = Board.builder()
                            .boardSeq(i+1)
                            .boardNm((i+1) + "번 게시글")
                            .boardDesc((i+1) + "번 게시글")
                            .boardType("PR")
                            .delYn("N")
                            .temporaryYn("N")
                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                            .build();

            boardList.add(board);
        }

        given(boardRepository.findAllById(anyList())).willReturn(boardList);

        // when
        String result = boardService.deleteMultiple(boardSeqList);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());
    }

    @Test
    @Order(7)
    @DisplayName("최근 작성한 게시글 10개 조회")
    public void findBoardCountTen() {
        // given
        String userId = "user1";

        List<ResponseBoardDto> responseBoardDtoList = new ArrayList<>();
        for(long i=0; i<10; i++){
            ResponseBoardDto responseBoardDto = ResponseBoardDto.builder()
                    .boardSeq((i+1))
                    .boardNm((i+1) + "번 게시글")
                    .boardDesc((i+1) + "번 게시글 내용")
                    .boardType("NORMAL")
                    .userSeq(1)
                    .userId("user1")
                    .userNm("유저1")
                    .profileImage("profileImageUser1.png")
                    .temporaryYn("N")
                    .delYn("N")
                    .viewCount(0)
                    .build();

            responseBoardDtoList.add(responseBoardDto);
        }

        given(boardRepository.findBoardCountTen(anyString())).willReturn(responseBoardDtoList);

        // when
        List<ResponseBoardDto> findBoardCountTen = boardService.findBoardCountTen(userId);

        // then
        assertThat(findBoardCountTen.size()).isEqualTo(10);
        assertThat(findBoardCountTen.get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    @Order(8)
    @DisplayName("나의 게시글 전체 조회")
    public void findAllMyBoard() {
        Pageable pageable = PageRequest.of(0, 10);
        String search = "";
        String category = "";
        String userId = "user1";

        List<ResponseBoardDto> responseBoardDtoList = new ArrayList<>();
        for(long i=0; i<20; i++){
            ResponseBoardDto responseBoardDto = ResponseBoardDto.builder()
                    .boardSeq((i+1))
                    .boardNm((i+1) + "번 게시글")
                    .boardDesc((i+1) + "번 게시글 내용")
                    .boardType("NORMAL")
                    .userSeq(1)
                    .userId("user1")
                    .userNm("유저1")
                    .profileImage("profileImageUser1.png")
                    .temporaryYn("N")
                    .delYn("N")
                    .viewCount(0)
                    .build();

            responseBoardDtoList.add(responseBoardDto);
        }

        Page<ResponseBoardDto> responseBoardDtoPage = new PageImpl<>(responseBoardDtoList, pageable, responseBoardDtoList.size());

        given(boardRepository.findAllMyBoard(any(Pageable.class), anyString(), anyString(), anyString())).willReturn(responseBoardDtoPage);

        // when
        Page<ResponseBoardDto> findAllMyBoard = boardService.findAllMyBoard(pageable, search, category, userId);

        // then
        assertThat(findAllMyBoard.getSize()).isEqualTo(10);
        assertThat(findAllMyBoard.getTotalElements()).isEqualTo(20);
        assertThat(findAllMyBoard.getContent().get(0).getUserId()).isEqualTo(userId);

    }

}