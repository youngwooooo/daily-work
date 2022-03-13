package com.work.daily.boardfile.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.boardfile.dto.BoardFileDto;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.BoardFile;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.BoardFileRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BoardFileService :: 단위 테스트")
public class BoardFileServiceTest {

    @Mock
    private BoardFileRepository boardFileRepository;

    @InjectMocks
    private BoardFileService boardFileService;

    @Test
    @Order(1)
    @DisplayName("게시글 파일 업로드")
    public void save() throws IOException {
        // given
        List<MultipartFile> files = new ArrayList<>();
        for(int i=0; i<3; i++){
            MockMultipartFile file = new MockMultipartFile("files", (i+1) + ".PNG", MediaType.IMAGE_PNG_VALUE, "PNG".getBytes());
            files.add(file);
        }

        Board board = Board.builder()
                        .boardSeq(1)
                        .boardNm("게시글1")
                        .boardDesc("게시글1 내용")
                        .boardType("NORMAL")
                        .user(User.builder().userSeq(1).userId("user1").userNm("유저1").userPw("user11234!").role(UserRole.USER).build())
                        .delYn("N")
                        .temporaryYn("N")
                        .build();

        List<BoardFile> boardFileList = new ArrayList<>();
        for(long i=0; i<3; i++){
            BoardFile boardFile = BoardFile.builder()
                                    .fileSeq(i+1)
                                    .fileSize(10000)
                                    .fileUploadPath("/boardfile/1/" + (i+1) + ".PNG")
                                    .fileServerNm((i+1) + ".PNG")
                                    .fileOriginNm((i+1) + ".PNG")
                                    .insDtm(LocalDateTime.now())
                                    .imageYn("Y")
                                    .board(board)
                                    .build();

            boardFileList.add(boardFile);
        }

        given(boardFileRepository.saveAll(anyList())).willReturn(boardFileList);

        // when
        String result = boardFileService.save(files, board);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());
    }

    @Test
    @Order(2)
    @DisplayName("게시글 파일 조회")
    public void findOneBoardFile() {
        // given
        long fileSeq = 1;

        Board board = Board.builder()
                        .boardSeq(1)
                        .boardNm("게시글1")
                        .boardDesc("게시글1 내용")
                        .boardType("NORMAL")
                        .user(User.builder().userSeq(1).userId("user1").userNm("유저1").userPw("user11234!").role(UserRole.USER).build())
                        .delYn("N")
                        .temporaryYn("N")
                        .build();

        BoardFileDto boardFileDto = BoardFileDto.builder()
                                    .fileSeq(1)
                                    .fileSize(10000)
                                    .fileUploadPath("/boardfile/1/1.PNG")
                                    .fileServerNm("1.PNG")
                                    .fileOriginNm("1.PNG")
                                    .insDtm(LocalDateTime.now())
                                    .imageYn("Y")
                                    .board(board)
                                    .build();

        BoardFile boardFile = BoardFile.builder()
                                .fileSeq(1)
                                .fileSize(10000)
                                .fileUploadPath("/boardfile/1/1.PNG")
                                .fileServerNm("1.PNG")
                                .fileOriginNm("1.PNG")
                                .insDtm(LocalDateTime.now())
                                .imageYn("Y")
                                .board(board)
                                .build();

        given(boardFileRepository.findById(anyLong())).willReturn(Optional.ofNullable(boardFile));

        // when
        BoardFileDto findOneBoardFile = boardFileService.findOneBoardFile(fileSeq);

        // then
        assertThat(findOneBoardFile.getBoard().getBoardSeq()).isEqualTo(board.getBoardSeq());
        assertThat(findOneBoardFile.getFileSeq()).isEqualTo(boardFileDto.getFileSeq());
        assertThat(findOneBoardFile.getFileUploadPath()).isEqualTo(boardFileDto.getFileUploadPath());
    }

    @Test
    @Order(3)
    @DisplayName("게시글 파일 삭제")
    public void delete() {
        // given
        List<Long> fileSeqList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        Board board = Board.builder()
                        .boardSeq(1)
                        .boardNm("게시글1")
                        .boardDesc("게시글1 내용")
                        .boardType("NORMAL")
                        .user(User.builder().userSeq(1).userId("user1").userNm("유저1").userPw("user11234!").role(UserRole.USER).build())
                        .delYn("N")
                        .temporaryYn("N")
                        .build();

        BoardFile boardFile = BoardFile.builder()
                                .fileSeq(1)
                                .fileSize(10000)
                                .fileUploadPath("/boardfile/1/1.PNG")
                                .fileServerNm("1.PNG")
                                .fileOriginNm("1.PNG")
                                .insDtm(LocalDateTime.now())
                                .imageYn("Y")
                                .board(board)
                                .build();

        given(boardFileRepository.findById(anyLong())).willReturn(Optional.ofNullable(boardFile));

        // when
        boardFileService.delete(fileSeqList);
    }
}