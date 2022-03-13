package com.work.daily.mission.controller;

import com.work.daily.config.SecurityConfig;
import com.work.daily.domain.entity.User;
import com.work.daily.mission.dto.ResponseMissionDto;
import com.work.daily.mission.dto.ResponseMissionParticipantsDto;
import com.work.daily.mission.dto.ResponseMissionStateDto;
import com.work.daily.mission.service.MissionParticipantsService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MissionController.class
            , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@MockBean(JpaMetamodelMappingContext.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("MissionController :: 단위 테스트")
class MissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MissionService missionService;

    @MockBean
    private MissionParticipantsService missionParticipantsService;

    @MockBean
    private MissionStateService missionStateService;

    @Test
    @Order(1)
    @WithMockCustomUser
    @DisplayName("전체 미션 VIEW")
    public void missions() throws Exception {
        // given
        List<ResponseMissionDto> missionDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 9);
        Page<ResponseMissionDto> findAllMissions = new PageImpl<>(missionDtoList, pageable, missionDtoList.size());

        given(missionService.findAllMissions(eq(pageable), anyString())).willReturn(findAllMissions);

        // when & then
        mockMvc.perform(get("/missions"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(missionService).findAllMissions(eq(pageable), anyString());
    }

    @Test
    @Order(2)
    @WithMockCustomUser
    @DisplayName("미션 만들기 VIEW")
    public void createMissionForm() throws Exception {
        // given

        // when & then
        mockMvc.perform(get("/mission"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    @WithMockCustomUser
    @DisplayName("미션 수정 VIEW")
    public void modifyMission() throws Exception {
        // given
        ResponseMissionDto responseMissionDto = ResponseMissionDto.builder()
                                                .missionSeq(1L)
                                                .missionNm("미션1")
                                                .missionDesc("미션1 내용")
                                                .missionStDt(LocalDateTime.now())
                                                .missionStDt(LocalDateTime.now().plusMonths(1)).build();

        given(missionService.findMission(anyLong())).willReturn(responseMissionDto);

        // when & then
        mockMvc.perform(get("/mission/1/modify"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(missionService).findMission(anyLong());
    }

    @Test
    @Order(4)
    @WithMockCustomUser
    @DisplayName("미션 상세 VIEW")
    public void detailMission() throws Exception {
        ResponseMissionDto findMission = ResponseMissionDto.builder()
                                            .missionSeq(1L)
                                            .missionNm("미션1")
                                            .missionDesc("미션1 내용")
                                            .user(User.builder().userSeq(1L).userId("testUser").userNm("테스트").build())
                                            .missionStDt(LocalDateTime.now())
                                            .missionStDt(LocalDateTime.now().plusMonths(1)).build();
        List<List<LocalDate>> dateOfWeek = new ArrayList<>();
        List<ResponseMissionParticipantsDto> findAllMissionParticipantByMissionSeq = new ArrayList<>();
        List<ResponseMissionStateDto> findAllMissionState = new ArrayList<>();

        given(missionService.findMission(anyLong())).willReturn(findMission);
        given(missionService.getDateOfWeek(anyLong())).willReturn(dateOfWeek);
        given(missionParticipantsService.findAllMissionParticipantByMissionSeq(anyLong())).willReturn(findAllMissionParticipantByMissionSeq);
        given(missionStateService.findAllMissionStateByMissionSeq(anyLong())).willReturn(findAllMissionState);

        // when & then
        mockMvc.perform(get("/mission/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(missionService).findMission(anyLong());
        verify(missionService).getDateOfWeek(anyLong());
        verify(missionParticipantsService).findAllMissionParticipantByMissionSeq(anyLong());
        verify(missionStateService).findAllMissionStateByMissionSeq(anyLong());

    }
}