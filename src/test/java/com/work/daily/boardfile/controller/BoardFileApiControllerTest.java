package com.work.daily.boardfile.controller;

import com.work.daily.apiserver.boardfile.BoardFileApiController;
import com.work.daily.boardfile.dto.BoardFileDto;
import com.work.daily.boardfile.service.BoardFileService;
import com.work.daily.config.SecurityConfig;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.Board;
import com.work.daily.domain.entity.User;
import com.work.daily.security.access.annotation.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardFileApiController.class
        , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@MockBean(JpaMetamodelMappingContext.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BoardFileApiController :: 단위 테스트")
public class BoardFileApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardFileService boardFileService;

    @Test
    @Order(1)
    @WithMockCustomUser
    @DisplayName("게시글 첨부 파일 다운로드")
    void boardFileDownload() throws Exception {
        // given
        // * 테스트를 위해서는 src/main/boardfile/... 경로에 파일이 존재해야함!
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

        given(boardFileService.findOneBoardFile(anyLong())).willReturn(boardFileDto);

        // when & then
        mockMvc.perform(get("/boardFile/download/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(boardFileService).findOneBoardFile(anyLong());

    }
}