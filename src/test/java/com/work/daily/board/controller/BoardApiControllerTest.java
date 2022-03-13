package com.work.daily.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.daily.access.ReturnResult;
import com.work.daily.apiserver.board.BoardApiController;
import com.work.daily.board.dto.RequestBoardCommentDto;
import com.work.daily.board.dto.RequestBoardDto;
import com.work.daily.board.service.BoardCommentService;
import com.work.daily.board.service.BoardService;
import com.work.daily.config.SecurityConfig;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardApiController.class
        , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@MockBean(JpaMetamodelMappingContext.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BoardApiController :: 단위 테스트")
public class BoardApiControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BoardService boardService;

    @MockBean
    private BoardCommentService boardCommentService;
    
    @Test
    @Order(1)
    @WithMockCustomUser
    @DisplayName("게시글 등록")
    public void createBoard() throws Exception {
        //
        RequestBoardDto requestBoardDto = RequestBoardDto.builder()
                                            .boardNm("게시글")
                                            .boardDesc("게시글내용")
                                            .boardType("QA")
                                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                                            .temporaryYn("N")
                                            .delYn("N")
                                            .build();
        String content = new ObjectMapper().writeValueAsString(requestBoardDto);

        MockMultipartFile files1 = new MockMultipartFile("files", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "test".getBytes());
        MockMultipartFile files2 = new MockMultipartFile("files", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes());
        MockMultipartFile requestBoardDtoJson = new MockMultipartFile("requestBoardDto", "json", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        given(boardService.save(any(RequestBoardDto.class), anyList())).willReturn(ReturnResult.SUCCESS.getValue());

        mockMvc.perform(multipart("/board")
                        .file(files1)
                        .file(files2)
                        .file(requestBoardDtoJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(boardService).save(any(RequestBoardDto.class), anyList());
    }

    @Test
    @Order(2)
    @WithMockCustomUser
    @DisplayName("게시글 수정")
    public void modifyBoard() throws Exception {
        // given
        RequestBoardDto requestBoardDto = RequestBoardDto.builder()
                .boardNm("게시글")
                .boardDesc("게시글내용")
                .boardType("QA")
                .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                .temporaryYn("N")
                .delYn("N")
                .build();

        List<Long> fileSeqList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        String content1 = new ObjectMapper().writeValueAsString(requestBoardDto);
        String content2 = new ObjectMapper().writeValueAsString(fileSeqList);

        MockMultipartFile files1 = new MockMultipartFile("files", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "test".getBytes());
        MockMultipartFile files2 = new MockMultipartFile("files", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes());
        MockMultipartFile requestBoardDtoJson = new MockMultipartFile("requestBoardDto", "json", MediaType.APPLICATION_JSON_VALUE, content1.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile fileSeqListJson = new MockMultipartFile("fileSeqList", "json", MediaType.APPLICATION_JSON_VALUE, content2.getBytes(StandardCharsets.UTF_8));

        given(boardService.modify(anyLong(), any(RequestBoardDto.class), anyList(), anyList())).willReturn(ReturnResult.SUCCESS.getValue());

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/board/1");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        // when & then
        mockMvc.perform(builder
                        .file(files1)
                        .file(files2)
                        .file(requestBoardDtoJson)
                        .file(fileSeqListJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(boardService).modify(anyLong(), any(RequestBoardDto.class), anyList(), anyList());
    }

    @Test
    @Order(3)
    @WithMockCustomUser
    @DisplayName("게시글 삭제")
    public void deleteBoard() throws Exception {
        // given
        long boardSeq = 1;
        given(boardService.delete(anyLong())).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(delete("/board/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(boardService).delete(anyLong());
    }

    @Test
    @Order(4)
    @WithMockCustomUser
    @DisplayName("게시글 여러개 삭제")
    public void deleteMultipleBoard() throws Exception {
        // given
        List<Long> boardSeqList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        String content = new ObjectMapper().writeValueAsString(boardSeqList);

        given(boardService.deleteMultiple(anyList())).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(delete("/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(boardService).deleteMultiple(anyList());
    }

    @Test
    @Order(5)
    @WithMockCustomUser
    @DisplayName("댓글/답글 등록")
    public void createBoardComment() throws Exception {
        // given
        RequestBoardCommentDto requestBoardCommentDto = RequestBoardCommentDto.builder()
                                                        .board(Board.builder().boardSeq(1).boardNm("게시글").boardNm("게시글내용").boardType("PR").build())
                                                        .commentSeq(1)
                                                        .commentDesc("내용")
                                                        .delYn("N")
                                                        .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build()).build();
        String content = new ObjectMapper().writeValueAsString(requestBoardCommentDto);

        given(boardCommentService.save(any(RequestBoardCommentDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(post("/board/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(boardCommentService).save(any(RequestBoardCommentDto.class));
    }

    @Test
    @Order(6)
    @WithMockCustomUser
    @DisplayName("댓글/답글 수정")
    public void modifyBoardComment() throws Exception {
        // given
        RequestBoardCommentDto requestBoardCommentDto = RequestBoardCommentDto.builder()
                                                        .board(Board.builder().boardSeq(1).boardNm("게시글").boardNm("게시글내용").boardType("PR").build())
                                                        .commentSeq(1)
                                                        .commentDesc("내용")
                                                        .delYn("N")
                                                        .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build()).build();
        String content = new ObjectMapper().writeValueAsString(requestBoardCommentDto);

        given(boardCommentService.modify(any(RequestBoardCommentDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(patch("/board/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());
    }

    @Test
    @Order(7)
    @WithMockCustomUser
    @DisplayName("댓글/답글 삭제")
    public void deleteBoardComment() throws Exception {
        // given
        given(boardCommentService.delete(anyLong())).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(delete("/board/comment/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(boardCommentService).delete(anyLong());
    }

}