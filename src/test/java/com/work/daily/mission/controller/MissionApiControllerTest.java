package com.work.daily.mission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.work.daily.access.ReturnResult;
import com.work.daily.apiserver.mission.MissionApiController;
import com.work.daily.config.SecurityConfig;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.User;
import com.work.daily.mission.dto.*;
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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MissionApiController.class
        , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@MockBean(JpaMetamodelMappingContext.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("MissionApiController :: ?????? ?????????")
class MissionApiControllerTest {

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
    @DisplayName("?????? ?????????")
    public void createMission() throws Exception {
        // given
        RequestMissionDto requestMissionDto = RequestMissionDto.builder()
                                                .user(User.builder().userSeq(1L).userId("writeUser").userNm("?????????").build())
                                                .missionNm("????????????")
                                                .missionDesc("????????????")
                                                .missionStDt(LocalDateTime.now())
                                                .missionEndDt(LocalDateTime.now().plusMonths(1))
                                                .releaseYn("Y")
                                                .autoAccessYn("Y")
                                                .masterYn("Y")
                                                .delYn("N")
                                                .temporaryYn("N")
                                                .build();
        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestMissionDto);
        MockMultipartFile requestMissionDtoJson = new MockMultipartFile("requestMissionDto", "jsonData", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file = new MockMultipartFile("file", "missionImage.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        given(missionService.save(any(RequestMissionDto.class), any(MultipartFile.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(multipart("/mission")
                        .file(file)
                        .file(requestMissionDtoJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(missionService).save(any(RequestMissionDto.class), any(MultipartFile.class));

    }

    @Test
    @Order(2)
    @WithMockCustomUser
    @DisplayName("?????? ??????")
    public void modifyMission() throws Exception {
        // given
        RequestMissionDto requestMissionDto = RequestMissionDto.builder()
                                                .user(User.builder().userSeq(1L).userId("writeUser").userNm("?????????").build())
                                                .missionNm("????????????")
                                                .missionDesc("????????????")
                                                .missionStDt(LocalDateTime.now())
                                                .missionEndDt(LocalDateTime.now().plusMonths(1))
                                                .releaseYn("Y")
                                                .autoAccessYn("Y")
                                                .masterYn("Y")
                                                .delYn("N")
                                                .temporaryYn("N")
                                                .build();
        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestMissionDto);
        MockMultipartFile requestMissionDtoJson = new MockMultipartFile("requestMissionDto", "jsonData", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file = new MockMultipartFile("file", "missionImage.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        given(missionService.modify(any(RequestMissionDto.class), any(MultipartFile.class))).willReturn(ReturnResult.SUCCESS.getValue());

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/mission/1");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        // when & then
        mockMvc.perform(builder
                .file(file)
                .file(requestMissionDtoJson)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(missionService).modify(any(RequestMissionDto.class), any(MultipartFile.class));
    }

    @Test
    @Order(3)
    @WithMockCustomUser
    @DisplayName("?????? ??????")
    public void deleteMission() throws Exception {
        // given
        given(missionService.delete(anyLong(), anyString())).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(delete("/mission/1")
                        .param("delYn", "N")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(missionService).delete(anyLong(), anyString());
    }

    @Test
    @Order(4)
    @WithMockCustomUser
    @DisplayName("?????? ??????")
    public void joinMission() throws Exception {
        // given
        RequestMissionParticipantsDto requestMissionParticipantsDto = RequestMissionParticipantsDto.builder()
                                                                        .missionSeq(1L)
                                                                        .userSeq(1L)
                                                                        .userId("testUser")
                                                                        .build();
        String content = new ObjectMapper().writeValueAsString(requestMissionParticipantsDto);
        given(missionParticipantsService.joinMission(any(RequestMissionParticipantsDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(post("/mission/1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(missionParticipantsService).joinMission(any(RequestMissionParticipantsDto.class));

    }

    @Test
    @Order(5)
    @WithMockCustomUser
    @DisplayName("?????? ??????")
    public void secessionMission() throws Exception {
        // given
        RequestMissionParticipantsDto requestMissionParticipantsDto = RequestMissionParticipantsDto.builder()
                                                                        .missionSeq(1L)
                                                                        .userSeq(1L)
                                                                        .userId("testUser")
                                                                        .build();
        String content = new ObjectMapper().writeValueAsString(requestMissionParticipantsDto);
        given(missionParticipantsService.secessionMission(any(RequestMissionParticipantsDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(delete("/mission/1/secession")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(missionParticipantsService).secessionMission(any(RequestMissionParticipantsDto.class));
    }

    @Test
    @Order(6)
    @WithMockCustomUser
    @DisplayName("?????? ????????? ??????")
    public void approveParticipants() throws Exception {
        // given
        RequestMissionParticipantsDto requestMissionParticipantsDto = RequestMissionParticipantsDto.builder()
                                                                        .missionSeq(1L)
                                                                        .userSeq(1L)
                                                                        .userId("testUser")
                                                                        .build();

        String content = new ObjectMapper().writeValueAsString(requestMissionParticipantsDto);

        ResponseMissionParticipantsDto responseMissionParticipantsDto = ResponseMissionParticipantsDto.builder()
                                                                            .missionSeq(1L)
                                                                            .user(User.builder().userSeq(1L).userId("testUser").userNm("?????????").build())
                                                                            .missionJoinDt(LocalDateTime.now())
                                                                            .missionJoinYn("Y")
                                                                            .missionJoinApprovalDt(LocalDateTime.now())
                                                                            .build();

        given(missionParticipantsService.approveParticipants(any(RequestMissionParticipantsDto.class))).willReturn(responseMissionParticipantsDto);

        mockMvc.perform(patch("/mission/1/approve-participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.missionSeq").value(responseMissionParticipantsDto.getMissionSeq()))
                .andExpect(jsonPath("$.data.missionJoinDt").value(responseMissionParticipantsDto.getMissionJoinDt().toString()))
                .andExpect(jsonPath("$.data.missionJoinYn").value(responseMissionParticipantsDto.getMissionJoinYn()))
                .andDo(print());

        verify(missionParticipantsService).approveParticipants(any(RequestMissionParticipantsDto.class));
    }

    @Test
    @Order(7)
    @WithMockCustomUser
    @DisplayName("?????? ????????? ??????")
    public void expulsionParticipants() throws Exception {
        // given
        RequestMissionParticipantsDto requestMissionParticipantsDto = RequestMissionParticipantsDto.builder()
                                                                            .missionSeq(1L)
                                                                            .userSeq(1L)
                                                                            .userId("testUser")
                                                                            .build();

        String content = new ObjectMapper().writeValueAsString(requestMissionParticipantsDto);

        ResponseMissionParticipantsDto responseMissionParticipantsDto = ResponseMissionParticipantsDto.builder()
                                                                            .missionSeq(1L)
                                                                            .user(User.builder().userSeq(1L).userId("testUser").userNm("?????????").build())
                                                                            .missionJoinDt(LocalDateTime.now())
                                                                            .missionJoinYn("Y")
                                                                            .missionJoinApprovalDt(LocalDateTime.now())
                                                                            .build();

        given(missionParticipantsService.expulsionParticipants(any(RequestMissionParticipantsDto.class))).willReturn(responseMissionParticipantsDto);

        // when & then
        mockMvc.perform(delete("/mission/1/expulsion-participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.userId").value(requestMissionParticipantsDto.getUserId()))
                .andDo(print());

        verify(missionParticipantsService).expulsionParticipants(any(RequestMissionParticipantsDto.class));
    }

    @Test
    @Order(8)
    @WithMockCustomUser
    @DisplayName("?????? ??????(?????? ??????) ??????")
    public void createSubmitMission() throws Exception {
        // given
        RequestMissionStateDto requestMissionStateDto = RequestMissionStateDto.builder()
                                                            .missionStateSeq(1L)
                                                            .missionStateWeek(1L)
                                                            .missionParticipants(MissionParticipants.builder()
                                                                    .missionSeq(1L)
                                                                    .user(User.builder().userSeq(1L).userId("participantUser").userNm("???????????????").build())
                                                                    .build())
                                                            .submittedMissionNm("???????????????")
                                                            .submittedMissionDesc("??????????????????")
                                                            .submittedMissionImage("submittedMissionImage.png")
                                                            .submittedMissionDt(LocalDateTime.now())
                                                            .approvalYn("N")
                                                            .build();

        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestMissionStateDto);
        MockMultipartFile requestMissionStateDtoJson = new MockMultipartFile("requestMissionStateDto", "jsonData", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file = new MockMultipartFile("file", "submittedMissionImage.png", MediaType.IMAGE_PNG_VALUE, "submittedMissionImage".getBytes());

        given(missionStateService.save(any(RequestMissionStateDto.class), any(MultipartFile.class))).willReturn(ReturnResult.SUCCESS.getValue());

        // when & then
        mockMvc.perform(multipart("/mission/1/submitMission")
                        .file(file)
                        .file(requestMissionStateDtoJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(missionStateService).save(any(RequestMissionStateDto.class), any(MultipartFile.class));

    }

    @Test
    @Order(9)
    @WithMockCustomUser
    @DisplayName("?????? ??????(?????? ??????) ?????? ??????")
    public void detailMissionState() throws Exception {
        // given
        ResponseMissionStateDto responseMissionStateDto = ResponseMissionStateDto.builder()
                                                            .missionStateSeq(1L)
                                                            .missionStateWeek(1L)
                                                            .missionSeq(1L)
                                                            .userSeq(1L)
                                                            .userId("participantUser")
                                                            .submittedMissionNm("???????????????")
                                                            .submittedMissionDesc("??????????????????")
                                                            .submittedMissionImage("submittedMissionImage.png")
                                                            .submittedMissionDt(LocalDateTime.now())
                                                            .approvalYn("Y")
                                                            .approvalDt(LocalDateTime.now().plusDays(1))
                                                            .build();

        given(missionStateService.findOneMissionState(anyLong(), anyLong())).willReturn(responseMissionStateDto);

        // when & then
        mockMvc.perform(get("/missionState/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.missionStateSeq").value(responseMissionStateDto.getMissionStateSeq()))
                .andExpect(jsonPath("$.data.missionStateWeek").value(responseMissionStateDto.getMissionStateWeek()))
                .andDo(print());

        verify(missionStateService).findOneMissionState(anyLong(), anyLong());
    }

    @Test
    @Order(10)
    @WithMockCustomUser
    @DisplayName("?????? ??????(?????? ??????) ??????(???????????? or ????????????)")
    public void modifyMissionState() throws Exception {
        // given
        RequestMissionStateDto requestMissionStateDto = RequestMissionStateDto.builder()
                                                            .missionStateSeq(1L)
                                                            .missionStateWeek(1L)
                                                            .missionParticipants(MissionParticipants.builder()
                                                                    .missionSeq(1L)
                                                                    .user(User.builder().userSeq(1L).userId("participantUser").userNm("???????????????").build())
                                                                    .build())
                                                            .submittedMissionNm("???????????????")
                                                            .submittedMissionDesc("??????????????????")
                                                            .submittedMissionImage("submittedMissionImage.png")
                                                            .submittedMissionDt(LocalDateTime.now())
                                                            .approvalYn("N")
                                                            .approvalDt(LocalDateTime.now())
                                                            .build();

        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestMissionStateDto);

        RequestMissionStateDto requestMissionStateDto2 = RequestMissionStateDto.builder()
                                                            .missionStateSeq(1L)
                                                            .missionStateWeek(1L)
                                                            .missionParticipants(MissionParticipants.builder()
                                                                    .missionSeq(1L)
                                                                    .user(User.builder().userSeq(1L).userId("participantUser").userNm("???????????????").build())
                                                                    .build())
                                                            .submittedMissionNm("???????????????")
                                                            .submittedMissionDesc("??????????????????")
                                                            .submittedMissionImage("submittedMissionImage.png")
                                                            .submittedMissionDt(LocalDateTime.now())
                                                            .approvalYn("N")
                                                            .rejectionYn("Y")
                                                            .rejectionDt(LocalDateTime.now())
                                                            .rejectionDesc("????????????")
                                                            .build();

        String content2 = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestMissionStateDto2);

        // ??????????????? ??????
        if("N".equals(requestMissionStateDto.getApprovalYn())){
            given(missionStateService.modifyMissionStateApprovalYn(any(RequestMissionStateDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

            // when & then
            mockMvc.perform(patch("/missionState/1/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                    .andDo(print());
        }

        // ???????????? ??????
        if("Y".equals(requestMissionStateDto2.getRejectionYn())){
            given(missionStateService.modifyMissionStateRejectionInfo(any(RequestMissionStateDto.class))).willReturn(ReturnResult.SUCCESS.getValue());

            // when & then
            mockMvc.perform(patch("/missionState/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content2)
                        .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                    .andDo(print());
        }

        verify(missionStateService).modifyMissionStateApprovalYn(any(RequestMissionStateDto.class));
        verify(missionStateService).modifyMissionStateRejectionInfo(any(RequestMissionStateDto.class));

    }

    @Test
    @Order(11)
    @WithMockCustomUser
    @DisplayName("?????? ??????(?????? ??????) ??????(??????, ??????, ?????????, ????????????")
    public void modifyMyMissionState() throws Exception {
        // given
        RequestMissionStateDto requestMissionStateDto = RequestMissionStateDto.builder()
                                                            .missionStateSeq(1L)
                                                            .missionStateWeek(1L)
                                                            .missionParticipants(MissionParticipants.builder()
                                                                    .missionSeq(1L)
                                                                    .user(User.builder().userSeq(1L).userId("participantUser").userNm("???????????????").build())
                                                                    .build())
                                                            .submittedMissionNm("???????????????")
                                                            .submittedMissionDesc("??????????????????")
                                                            .submittedMissionImage("submittedMissionImage.png")
                                                            .submittedMissionDt(LocalDateTime.now())
                                                            .approvalYn("N")
                                                            .build();

        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestMissionStateDto);
        MockMultipartFile requestMissionStateDtoJson = new MockMultipartFile("requestMissionStateDto", "jsonData", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file = new MockMultipartFile("file", "submittedMissionImage.png", MediaType.IMAGE_PNG_VALUE, "submittedMissionImage".getBytes());

        given(missionStateService.modifyMyMissionState(any(RequestMissionStateDto.class), any(MultipartFile.class))).willReturn(ReturnResult.SUCCESS.getValue());

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/my-missionState/1/1");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        // when & then
        mockMvc.perform(builder
                    .file(file)
                    .file(requestMissionStateDtoJson)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ReturnResult.SUCCESS.getValue()))
                .andDo(print());

        verify(missionStateService).modifyMyMissionState(any(RequestMissionStateDto.class), any(MultipartFile.class));
    }
}