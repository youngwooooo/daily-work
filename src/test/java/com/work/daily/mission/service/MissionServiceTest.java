package com.work.daily.mission.service;

import com.work.daily.access.ReturnResult;
import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.MissionParticipantsRepository;
import com.work.daily.domain.repository.MissionRepository;
import com.work.daily.domain.repository.MissionStateRepository;
import com.work.daily.mission.dto.RequestMissionDto;
import com.work.daily.mission.dto.RequestMissionParticipantsDto;
import com.work.daily.mission.dto.ResponseMissionDto;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("MissionService :: 단위 테스트")
public class MissionServiceTest {
    private final Logger log = LoggerFactory.getLogger(MissionServiceTest.class);

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private MissionParticipantsRepository missionParticipantsRepository;

    @Mock
    private MissionStateRepository missionStateRepository;

    @InjectMocks
    private MissionService missionService;

    @Test
    @Order(1)
    @DisplayName("모든 미션 조회")
    public void findAllMissions() {
        // given
        List<Mission> list = new ArrayList<>();
        for(long i=1; i<=10; i++){
            Mission mission = Mission.builder()
                                .missionSeq(i)
                                .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                                .missionNm("미션" + i)
                                .missionDesc("미션설명" + i)
                                .missionStDt(LocalDateTime.now().minusMinutes(1))
                                .missionEndDt(LocalDateTime.now().minusDays(5))
                                .releaseYn("Y")
                                .autoAccessYn("Y")
                                .masterYn("Y")
                                .delYn("N")
                                .temporaryYn("N")
                                .missionImage("missionImage" + i + ".png")
                                .closeYn("N")
                                .build();

            list.add(mission);
        }
        Pageable pageable = PageRequest.of(0, 9);
        String search = "";
        Page<Mission> paging = new PageImpl<>(list, pageable, list.size());

        given(missionRepository.findAllMission(any(Pageable.class), anyString())).willReturn(paging);

        // when
        Page<ResponseMissionDto> findAllMissions = missionService.findAllMissions(pageable, search);

        // then
        assertThat(findAllMissions.getSize()).isEqualTo(9);
        assertThat(findAllMissions.getTotalElements()).isEqualTo(10);

    }

    @Test
    @Order(2)
    @DisplayName("미션 단건(상세) 조회")
    public void findMission() {
        // given
        long missionSeq = 1L;
        Mission mission = Mission.builder()
                            .missionSeq(1L)
                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                            .missionNm("미션")
                            .missionDesc("미션설명")
                            .missionStDt(LocalDateTime.now().minusMinutes(1))
                            .missionEndDt(LocalDateTime.now().minusDays(5))
                            .releaseYn("Y")
                            .autoAccessYn("Y")
                            .masterYn("Y")
                            .delYn("N")
                            .temporaryYn("N")
                            .missionImage("missionImage.png")
                            .closeYn("N")
                            .build();

        given(missionRepository.findMission(anyLong())).willReturn(Optional.ofNullable(mission));

        // when
        ResponseMissionDto findMission = missionService.findMission(missionSeq);

        // then
        assertThat(findMission.getMissionSeq()).isEqualTo(missionSeq);
    }

    @Test
    @Order(3)
    @DisplayName("미션시작일 ~ 미션종료일 사이 모든 날짜를 조회 및 포맷")
    public void getDateOfWeek() {
        long missionSeq = 1L;
        Mission mission = Mission.builder()
                            .missionSeq(1L)
                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                            .missionNm("미션")
                            .missionDesc("미션설명")
                            .missionStDt(LocalDateTime.now())
                            .missionEndDt(LocalDateTime.now().plusMonths(1))
                            .releaseYn("Y")
                            .autoAccessYn("Y")
                            .masterYn("Y")
                            .delYn("N")
                            .temporaryYn("N")
                            .missionImage("missionImage.png")
                            .closeYn("N")
                            .build();

        given(missionRepository.findById(anyLong())).willReturn(Optional.ofNullable(mission));

        // 미션 시작일 ~ 종료일 사이의 일 수
        long totalCount = ChronoUnit.DAYS.between(LocalDate.from(mission.getMissionStDt()), LocalDate.from(mission.getMissionEndDt()));

        // when
        List<List<LocalDate>> getDateOfWeek = missionService.getDateOfWeek(missionSeq);

        // getDateOfWeek()로 구한 일 수
        int getCount = 0;
        for(int i=0; i<getDateOfWeek.size(); i++){
            getCount += getDateOfWeek.get(i).size();
        }

        // then
        assertThat(totalCount).isEqualTo(getCount);

    }

    @Test
    @Order(4)
    @DisplayName("미션 만들기")
    public void save() throws IOException {
        // given
        RequestMissionDto requestMissionDto = RequestMissionDto.builder()
                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                            .missionNm("미션")
                            .missionDesc("미션설명")
                            .missionStDt(LocalDateTime.now())
                            .missionEndDt(LocalDateTime.now().plusMonths(1))
                            .releaseYn("Y")
                            .autoAccessYn("Y")
                            .masterYn("Y")
                            .delYn("N")
                            .temporaryYn("N")
                            .missionImage("missionImage.png")
                            .closeYn("N")
                            .build();

        Mission mission = Mission.builder()
                            .missionSeq(1L)
                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                            .missionNm("미션")
                            .missionDesc("미션설명")
                            .missionStDt(LocalDateTime.now())
                            .missionEndDt(LocalDateTime.now().plusMonths(1))
                            .releaseYn("Y")
                            .autoAccessYn("Y")
                            .masterYn("Y")
                            .delYn("N")
                            .temporaryYn("N")
                            .missionImage("missionImage.png")
                            .closeYn("N")
                            .build();

        RequestMissionParticipantsDto requestMissionParticipantsDto = RequestMissionParticipantsDto.builder()
                                                    .missionSeq(mission.getMissionSeq())
                                                    .userSeq(mission.getUser().getUserSeq())
                                                    .userId(mission.getUser().getUserId())
                                                    .missionJoinDt(LocalDateTime.now())
                                                    .missionJoinYn("Y")
                                                    .missionJoinApprovalDt(LocalDateTime.now())
                                                    .build();

        MockMultipartFile file = new MockMultipartFile("missionImage.png", "image", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        given(missionRepository.save(any(Mission.class))).willReturn(mission);
        given(missionParticipantsRepository.save(any(MissionParticipants.class))).willReturn(requestMissionParticipantsDto.toEntity());

        // when
        String result = missionService.save(requestMissionDto, file);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());

    }

    @Test
    @Order(5)
    @DisplayName("미션 수정(미션 정보 + 이미지)")
    public void modify() throws IOException {
        // given
        RequestMissionDto requestMissionDto = RequestMissionDto.builder()
                                                .missionSeq(1L)
                                                .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                                                .missionNm("미션")
                                                .missionDesc("미션설명")
                                                .missionStDt(LocalDateTime.now())
                                                .missionEndDt(LocalDateTime.now().plusMonths(1))
                                                .releaseYn("Y")
                                                .autoAccessYn("Y")
                                                .masterYn("Y")
                                                .delYn("N")
                                                .temporaryYn("N")
                                                .missionImage("missionImage.png")
                                                .closeYn("N")
                                                .build();

        Mission mission = Mission.builder()
                            .missionSeq(1L)
                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                            .missionNm("미션")
                            .missionDesc("미션설명")
                            .missionStDt(LocalDateTime.now())
                            .missionEndDt(LocalDateTime.now().plusMonths(1))
                            .releaseYn("Y")
                            .autoAccessYn("Y")
                            .masterYn("Y")
                            .delYn("N")
                            .temporaryYn("N")
                            .missionImage("modifyMissionImage.png")
                            .closeYn("N")
                            .build();

        MockMultipartFile file = new MockMultipartFile("modifyMissionImage.png", "image", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        given(missionRepository.findById(anyLong())).willReturn(Optional.ofNullable(mission));

        // when
        String result = missionService.modify(requestMissionDto, file);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());
    }

    @Test
    @Order(6)
    @DisplayName("미션 삭제(삭제여부 Y)")
    public void delete() {
        // given
        long missionSeq = 1;
        String delYn = "Y";
        Mission mission = Mission.builder()
                            .missionSeq(1L)
                            .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                            .missionNm("미션")
                            .missionDesc("미션설명")
                            .missionStDt(LocalDateTime.now())
                            .missionEndDt(LocalDateTime.now().plusMonths(1))
                            .releaseYn("Y")
                            .autoAccessYn("Y")
                            .masterYn("Y")
                            .delYn("N")
                            .temporaryYn("N")
                            .missionImage("modifyMissionImage.png")
                            .closeYn("N")
                            .build();

        given(missionRepository.findById(anyLong())).willReturn(Optional.ofNullable(mission));

        // when
        String result = missionService.delete(missionSeq, delYn);

        // then
        assertThat(result).isEqualTo(ReturnResult.SUCCESS.getValue());

    }

    @Test
    @Order(7)
    @DisplayName("최근 참여 미션 4건 조회")
    public void findLatelyParticipationMission() {
        // given
        String userId = "participantUser1";

        List<List<MissionParticipants>> missionParticipantsList = new ArrayList<>();
        List<MissionParticipants> temp = new ArrayList<>();
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                MissionParticipants missionParticipants = MissionParticipants.builder()
                        .missionSeq(i+1)
                        .userSeq(j+1)
                        .userId("participantUser" + (j+1))
                        .missionJoinYn("Y")
                        .missionJoinDt(LocalDateTime.now())
                        .missionJoinApprovalDt(LocalDateTime.now())
                        .build();

                temp.add(missionParticipants);
            }
            missionParticipantsList.add(temp);
            temp = new ArrayList<>();

        }

        List<Mission> missionList = new ArrayList<>();
        for(int i=0; i<4; i++){
            Mission mission = Mission.builder()
                    .missionSeq((long) (i+1))
                    .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                    .missionNm((i+1) + "번 미션")
                    .missionDesc((i+1) + "번 미션설명")
                    .missionStDt(LocalDateTime.now())
                    .missionEndDt(LocalDateTime.now().plusMonths(1))
                    .releaseYn("Y")
                    .autoAccessYn("Y")
                    .masterYn("Y")
                    .delYn("N")
                    .temporaryYn("N")
                    .missionImage("missionImage.png")
                    .closeYn("N")
                    .build();

            missionList.add(mission);
        }

        given(missionRepository.findLatelyParticipationMission(anyString())).willReturn(missionList);

        // when
        List<ResponseMissionDto> findLatelyParticipationMission = missionService.findLatelyParticipationMission(userId);

        // then
        assertThat(findLatelyParticipationMission.get(0).getMissionSeq()).isEqualTo(missionParticipantsList.get(0).get(0).getMissionSeq());
        assertThat(findLatelyParticipationMission.get(1).getMissionSeq()).isEqualTo(missionParticipantsList.get(1).get(0).getMissionSeq());
        assertThat(findLatelyParticipationMission.get(2).getMissionSeq()).isEqualTo(missionParticipantsList.get(2).get(0).getMissionSeq());
        assertThat(findLatelyParticipationMission.get(3).getMissionSeq()).isEqualTo(missionParticipantsList.get(3).get(0).getMissionSeq());

    }

    @Test
    @Order(8)
    @DisplayName("최근 참여 미션 전체 조회")
    public void findAllLatelyParticipationMission() {
        // given
        String userId = "participantUser1";
        Pageable pageable = PageRequest.of(0, 8);
        String search = "";

        List<List<MissionParticipants>> missionParticipantsList = new ArrayList<>();
        List<MissionParticipants> temp = new ArrayList<>();
        for(int i=0; i<8; i++){
            for(int j=0; j<4; j++){
                MissionParticipants missionParticipants = MissionParticipants.builder()
                        .missionSeq(i+1)
                        .userSeq(j+1)
                        .userId("participantUser" + (j+1))
                        .missionJoinYn("Y")
                        .missionJoinDt(LocalDateTime.now())
                        .missionJoinApprovalDt(LocalDateTime.now())
                        .build();

                temp.add(missionParticipants);
            }
            missionParticipantsList.add(temp);
            temp = new ArrayList<>();

        }

        List<Mission> missionList = new ArrayList<>();
        for(long i=0; i<10; i++){
            Mission mission = Mission.builder()
                    .missionSeq((i+1))
                    .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                    .missionNm((i+1) + "번 미션")
                    .missionDesc((i+1) + "번 미션설명")
                    .missionStDt(LocalDateTime.now())
                    .missionEndDt(LocalDateTime.now().plusMonths(1))
                    .releaseYn("Y")
                    .autoAccessYn("Y")
                    .masterYn("Y")
                    .delYn("N")
                    .temporaryYn("N")
                    .missionImage("missionImage.png")
                    .closeYn("N")
                    .build();

            missionList.add(mission);
        }

        Page<Mission> missionPage = new PageImpl<>(missionList, pageable, missionList.size());

        given(missionRepository.findAllLatelyParticipationMission(anyString(), any(Pageable.class), anyString())).willReturn(missionPage);

        // when
        Page<ResponseMissionDto> findAllLatelyParticipationMission = missionService.findAllLatelyParticipationMission(userId, pageable, search);

        // then
        assertThat(findAllLatelyParticipationMission.getSize()).isEqualTo(8);
        assertThat(findAllLatelyParticipationMission.getTotalElements()).isEqualTo(10);
        for(int i=0; i<findAllLatelyParticipationMission.getSize(); i++){
            assertThat(findAllLatelyParticipationMission.getContent().get(i).getMissionSeq()).isEqualTo(missionParticipantsList.get(i).get(0).getMissionSeq());
        }

    }

    @Test
    @Order(9)
    @DisplayName("최근 작성한 미션 4건 조회")
    public void findLatelyCreatedMission() {
        // given
        String userId = "user1";
        List<Mission> missionList = new ArrayList<>();
        for(long i=0; i<4; i++){
            Mission mission = Mission.builder()
                    .missionSeq((i+1))
                    .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                    .missionNm((i+1) + "번 미션")
                    .missionDesc((i+1) + "번 미션설명")
                    .missionStDt(LocalDateTime.now())
                    .missionEndDt(LocalDateTime.now().plusMonths(1))
                    .releaseYn("Y")
                    .autoAccessYn("Y")
                    .masterYn("Y")
                    .delYn("N")
                    .temporaryYn("N")
                    .missionImage("missionImage.png")
                    .closeYn("N")
                    .build();

            missionList.add(mission);
        }

        given(missionRepository.findLatelyCreatedMission(anyString())).willReturn(missionList);

        // when
        List<ResponseMissionDto> findLatelyCreatedMission = missionService.findLatelyCreatedMission(userId);

        // then
        for(int i=0; i<findLatelyCreatedMission.size(); i++){
            assertThat(findLatelyCreatedMission.get(i).getUser().getUserId()).isEqualTo(userId);
        }
    }

    @Test
    @Order(10)
    @DisplayName("나의 작성 미션 전체 조회")
    public void findAllLatelyCreatedMission() {
        // given
        String userId = "user1";
        Pageable pageable = PageRequest.of(0, 8);
        String search = "";

        List<Mission> missionList = new ArrayList<>();
        for(long i=0; i<10; i++){
            Mission mission = Mission.builder()
                    .missionSeq((i+1))
                    .user(User.builder().userSeq(1).userId("user1").userNm("유저1").build())
                    .missionNm((i+1) + "번 미션")
                    .missionDesc((i+1) + "번 미션설명")
                    .missionStDt(LocalDateTime.now())
                    .missionEndDt(LocalDateTime.now().plusMonths(1))
                    .releaseYn("Y")
                    .autoAccessYn("Y")
                    .masterYn("Y")
                    .delYn("N")
                    .temporaryYn("N")
                    .missionImage("missionImage.png")
                    .closeYn("N")
                    .build();

            missionList.add(mission);
        }
        Page<Mission> missionPage = new PageImpl<>(missionList, pageable, missionList.size());

        given(missionRepository.findAllLatelyCreatedMission(anyString(), any(Pageable.class), anyString())).willReturn(missionPage);

        // when
        Page<ResponseMissionDto> findAllLatelyCreatedMission = missionService.findAllLatelyCreatedMission(userId, pageable, search);

        // then
        assertThat(findAllLatelyCreatedMission.getSize()).isEqualTo(8);
        assertThat(findAllLatelyCreatedMission.getTotalElements()).isEqualTo(10);
        for(int i=0; i<findAllLatelyCreatedMission.getSize(); i++){
            assertThat(findAllLatelyCreatedMission.getContent().get(i).getUser().getUserId()).isEqualTo(userId);
        }
    }

}