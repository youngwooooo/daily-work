package com.work.daily.board.controller;

import com.work.daily.board.dto.BoardTypeDto;
import com.work.daily.board.dto.ResponseBoardCommentDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.board.service.BoardCommentService;
import com.work.daily.board.service.BoardService;
import com.work.daily.board.service.BoardTypeService;
import com.work.daily.config.SecurityConfig;
import com.work.daily.domain.entity.BoardFile;
import com.work.daily.security.access.annotation.WithMockCustomUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardController.class
            , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@MockBean(JpaMetamodelMappingContext.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private BoardCommentService boardCommentService;

    @MockBean
    private BoardTypeService boardTypeService;

    @Test
    @Order(1)
    @WithMockCustomUser
    @DisplayName("전체 게시글 VIEW")
    public void boards() throws Exception {
        // given
        List<BoardTypeDto> findAllBoardType = new ArrayList<>();
        List<ResponseBoardDto> list = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResponseBoardDto> findAllBoard = new PageImpl<>(list, pageable, list.size());

        given(boardService.findAllBoard(any(Pageable.class), anyString(), anyString())).willReturn(findAllBoard);
        given(boardTypeService.findAllBoardType()).willReturn(findAllBoardType);

        // when & then
        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(boardService).findAllBoard(any(Pageable.class), anyString(), anyString());
        verify(boardTypeService).findAllBoardType();
    }

    @Test
    @Order(2)
    @WithMockCustomUser
    @DisplayName("게시글 등록 VIEW")
    public void createBoardForm() throws Exception {
        // given
        List<BoardTypeDto> findAllBoardType = new ArrayList<>();
        given(boardTypeService.findAllBoardType()).willReturn(findAllBoardType);

        // when & then
        mockMvc.perform(get("/board"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(boardTypeService).findAllBoardType();
    }

    @Test
    @Order(3)
    @WithMockCustomUser
    @DisplayName("게시글 상세 조회 VIEW")
    public void detailBoardForm() throws Exception {
        // given
        List<BoardFile> boardFileList = new ArrayList<>();
        ResponseBoardDto responseBoardDto = ResponseBoardDto.builder()
                                                .boardSeq(1)
                                                .boardNm("게시글")
                                                .boardDesc("게시글 내용입니다")
                                                .userSeq(1)
                                                .userId("user1")
                                                .userNm("유저1")
                                                .boardFileList(boardFileList).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResponseBoardCommentDto> findAllParentBoardComment= new PageImpl<>(new ArrayList<>(), pageable, 10);
        List<ResponseBoardCommentDto> findAllChildBoardComment = new ArrayList<>();

        given(boardService.findOneBoard(anyLong())).willReturn(responseBoardDto);
        given(boardCommentService.findAllParentBoardComment(anyLong(), any(Pageable.class))).willReturn(findAllParentBoardComment);
        given(boardCommentService.findAllChildBoardComment(anyLong())).willReturn(findAllChildBoardComment);

        // when & then
        mockMvc.perform(get("/board/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(boardService).findOneBoard(anyLong());
        verify(boardService).increaseViewCount(anyLong());
        verify(boardCommentService).findAllParentBoardComment(anyLong(), any(Pageable.class));
        verify(boardCommentService).findAllChildBoardComment(anyLong());

    }

    @Test
    @Order(4)
    @WithMockCustomUser
    @DisplayName("게시글 수정 VIEW")
    public void modifyBoardForm() throws Exception {
        // given
        List<BoardFile> boardFileList = new ArrayList<>();
        ResponseBoardDto responseBoardDto = ResponseBoardDto.builder()
                                                .boardSeq(1)
                                                .boardNm("게시글")
                                                .boardDesc("게시글 내용입니다")
                                                .userSeq(1)
                                                .userId("user1")
                                                .userNm("유저1")
                                                .boardFileList(boardFileList).build();
        List<BoardTypeDto> findAllBoardType = new ArrayList<>();

        given(boardService.findOneBoard(anyLong())).willReturn(responseBoardDto);
        given(boardTypeService.findAllBoardType()).willReturn(findAllBoardType);

        // when
        mockMvc.perform(get("/board/1/modify"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(boardService).findOneBoard(anyLong());
        verify(boardTypeService).findAllBoardType();
    }
}