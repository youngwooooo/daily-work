package com.work.daily.user.controller;

import com.work.daily.board.dto.BoardTypeDto;
import com.work.daily.board.dto.ResponseBoardDto;
import com.work.daily.board.service.BoardService;
import com.work.daily.board.service.BoardTypeService;
import com.work.daily.config.SecurityConfig;
import com.work.daily.mission.dto.ResponseMissionDto;
import com.work.daily.mission.dto.ResponseMissionStateDto;
import com.work.daily.mission.service.MissionService;
import com.work.daily.mission.service.MissionStateService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class
            , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@MockBean(JpaMetamodelMappingContext.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserController :: 단위 테스트")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MissionService missionService;

    @MockBean
    private MissionStateService missionStateService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private BoardTypeService boardTypeService;

    @Test
    @Order(1)
    @WithMockCustomUser
    @DisplayName("마이페이지 VIEW")
    public void myPage() throws Exception {
        // given
        List<ResponseMissionDto> findLatelyParticipationMission = new ArrayList<>();
        List<ResponseMissionDto> findLatelyCreatedMission = new ArrayList<>();
        List<ResponseBoardDto> findBoardCountTen = new ArrayList<>();

        given(missionService.findLatelyParticipationMission(anyString())).willReturn(findLatelyParticipationMission);
        given(missionService.findLatelyCreatedMission(anyString())).willReturn(findLatelyCreatedMission);
        given(boardService.findBoardCountTen(anyString())).willReturn(findBoardCountTen);

        // when & then
        mockMvc.perform(get("/user/mypage"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(missionService).findLatelyParticipationMission(anyString());
        verify(missionService).findLatelyCreatedMission(anyString());
        verify(boardService).findBoardCountTen(anyString());
    }

    @Test
    @Order(2)
    @WithMockCustomUser
    @DisplayName("마이페이지 - 나의 참여 미션 VIEW")
    public void myParticipationMission() throws Exception {
        // given
        List<ResponseMissionDto> missionDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 8);
        Page<ResponseMissionDto> findAllLatelyParticipationMission = new PageImpl<>(missionDtoList, pageable, missionDtoList.size());
        given(missionService.findAllLatelyParticipationMission(anyString(), eq(pageable), anyString())).willReturn(findAllLatelyParticipationMission);

        // when & then
        mockMvc.perform(get("/user/mypage/my-participation-mission"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(missionService).findAllLatelyParticipationMission(anyString(), eq(pageable), anyString());
    }

    @Test
    @Order(3)
    @WithMockCustomUser
    @DisplayName("마이페이지 - 나의 작성 미션 VIEW")
    public void myCreatedMission() throws Exception {
        // given
        List<ResponseMissionDto> missionDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 8);
        Page<ResponseMissionDto> findAllLatelyCreatedMission = new PageImpl<>(missionDtoList, pageable, missionDtoList.size());

        given(missionService.findAllLatelyCreatedMission(anyString(), eq(pageable), anyString())).willReturn(findAllLatelyCreatedMission);

        // when & then
        mockMvc.perform(get("/user/mypage/my-created-mission"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(missionService).findAllLatelyCreatedMission(anyString(), eq(pageable), anyString());

    }

    @Test
    @Order(4)
    @WithMockCustomUser
    @DisplayName("마이페이지 - 나의 미션 현황 VIEW")
    public void myMissionState() throws Exception {
        // given
        List<ResponseMissionStateDto> missionStateDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 8);
        Page<ResponseMissionStateDto> findAllMyMissionState = new PageImpl<>(missionStateDtoList, pageable, missionStateDtoList.size());

        given(missionStateService.findAllMyMissionState(anyString(), eq(pageable), anyString())).willReturn(findAllMyMissionState);

        // when & then
        mockMvc.perform(get("/user/mypage/my-mission-state"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(missionStateService).findAllMyMissionState(anyString(), eq(pageable), anyString());
    }

    @Test
    @Order(5)
    @WithMockCustomUser
    @DisplayName("마이페이지 - 나의 게시글 VIEW")
    public void myBoard() throws Exception {
        List<BoardTypeDto> findAllBoardType = new ArrayList<>();
        List<ResponseBoardDto> boardDtoList = new ArrayList<>();
        for(long i=0; i<2; i++){
            ResponseBoardDto responseBoardDto = ResponseBoardDto.builder()
                                                    .boardSeq(i)
                                                    .boardNm("게시글" + i)
                                                    .boardDesc("내용")
                                                    .build();
            boardDtoList.add(responseBoardDto);
        }
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResponseBoardDto> findAllMyBoard = new PageImpl<>(boardDtoList, pageable, boardDtoList.size());

        given(boardTypeService.findAllBoardType()).willReturn(findAllBoardType);
        given(boardService.findAllMyBoard(eq(pageable), anyString(), anyString(), anyString())).willReturn(findAllMyBoard);

        // when & then
        mockMvc.perform(get("/user/mypage/my-board"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(boardTypeService).findAllBoardType();
        verify(boardService).findAllMyBoard(eq(pageable), anyString(), anyString(), anyString());
    }

    @Test
    @Order(6)
    @WithMockCustomUser
    @DisplayName("마이페이지 - 계정관리 VIEW")
    public void myAccountForm() throws Exception {
        // given

        // when & then
        mockMvc.perform(get("/user/my-account"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(7)
    @WithMockCustomUser
    @DisplayName("마이페이지 - 비밀번호 변경 VIEW")
    public void modifyPasswordForm() throws Exception {
        // given

        // when & then
        mockMvc.perform(get("/user/my-account/password"))
                .andExpect(status().isOk())
                .andDo(print());
    }

}