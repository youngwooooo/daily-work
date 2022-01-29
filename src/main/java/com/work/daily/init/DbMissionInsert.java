package com.work.daily.init;

import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.MissionState;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.MissionParticipantsRepository;
import com.work.daily.domain.repository.MissionRepository;
import com.work.daily.domain.repository.MissionStateRepository;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.mission.dto.RequestMissionDto;
import com.work.daily.mission.dto.RequestMissionParticipantsDto;
import com.work.daily.mission.dto.RequestMissionStateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Configuration
@Component("DbMissionInsert")
public class DbMissionInsert {
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final MissionParticipantsRepository missionParticipantsRepository;
    private final MissionStateRepository missionStateRepository;

    /**
     * @filename : DbMissionInsert.java
     * @description : 미션 및 미션 마스터 등록 (총 3개 미션 등록)
     * @author : 이준범
     */
    @Bean("missionInfoInsert")
    @DependsOn(value = {"userInfoInsert"})
    public void missionInfoInsert() {
        List<Mission> missionMasterDto = createMissionMasterDto();
        missionRepository.saveAll(missionMasterDto);

    }


    /**
     * @filename : DbMissionInsert.java
     * @description : 미션 참여자 등록 (test 데이터 google, kakao, naver 로 등록)
     * @author : 이준범
     */
    @Bean("missionParticipantsInsert")
    @DependsOn(value = {"missionInfoInsert"})
    public void missionParticipantsInsert() {
        List<MissionParticipants> missionParticipantsDto = createMissionParticipantsDto();
        missionParticipantsRepository.saveAll(missionParticipantsDto);
    }


    /**
     * @filename : DbMissionInsert.java
     * @description : 미션 현황 등록
     * @author : 이준범
     */
    @Bean("missionStateInsert")
    @DependsOn(value = {"missionParticipantsInsert"})
    public void missionStateInsert() {
        List<MissionState> missionStateDto = createMissionStateDto();
        missionStateRepository.saveAll(missionStateDto);
    }











    // 미션 , 미션 마스터 등록
    private List<Mission> createMissionMasterDto() {
        List<Mission> saveMissionDtoList = new ArrayList<>();

        saveMissionDtoList.add(
                RequestMissionDto.builder()
                    .missionNm("한상우의 코딩특강")
                    .missionDesc("선후배가 함께하는 코딩 특강으로 코딩 know잼이 되세요!")
                    .missionStDt(LocalDateTime.now())
                    .missionEndDt(LocalDateTime.now().plusMonths(1))
                    .releaseYn("Y")
                    .autoAccessYn("N")
                    .masterYn("Y")
                    .user(userRepository.findByUserId("sangWooHan").get())
                    .delYn("N")
                    .temporaryYn("N")
                    .missionImage("/main/resources/static/img/common/basic_mission.jpg")
                    .build()
                    .toEntity()
        );

        saveMissionDtoList.add(
                RequestMissionDto.builder()
                        .missionNm("이준범의 한걸음씩 나가는 코딩교실")
                        .missionDesc("나도 모르는데 너도 모르니? 그러니까 같이 공부하자!")
                        .missionStDt(LocalDateTime.now())
                        .missionEndDt(LocalDateTime.now().plusMonths(1))
                        .releaseYn("Y")
                        .autoAccessYn("N")
                        .masterYn("Y")
                        .user(userRepository.findByUserId("junBeomLee").get())
                        .delYn("N")
                        .temporaryYn("N")
                        .missionImage("/main/resources/static/img/common/basic_mission.jpg")
                        .build()
                        .toEntity()
        );

        saveMissionDtoList.add(
                RequestMissionDto.builder()
                        .missionNm("이영우의 코딩스터디")
                        .missionDesc("웹 개발자로 취업하기 위해 발버둥을 쳐보자!!")
                        .missionStDt(LocalDateTime.now())
                        .missionEndDt(LocalDateTime.now().plusMonths(1))
                        .releaseYn("Y")
                        .autoAccessYn("Y")
                        .masterYn("Y")
                        .user(userRepository.findByUserId("youngWooLee").get())
                        .delYn("N")
                        .temporaryYn("N")
                        .missionImage("/main/resources/static/img/common/basic_mission.jpg")
                        .build()
                        .toEntity()
        );

        return saveMissionDtoList;
    }

    // 미션 참여자 등록
    private List<MissionParticipants> createMissionParticipantsDto() {
        String missionJoinYn = "";
        List<MissionParticipants> saveMissionParticipantsDtoList = new ArrayList<>();
        List<Mission> allMissions = missionRepository.findAll();
        List<User> allUsers = userRepository.findAll();

        for (Mission allMission : allMissions) {
            if (allMission.getMissionNm().contains("한상우")) {
                for (int i = 0; i < 6; i++) {
                    String userId = "googleUser" + i;
                    List<User> collect = allUsers.stream()
                            .filter(user -> user.getUserId().equals(userId))
                            .collect(Collectors.toList());

                    if (i % 2 == 1) {
                        missionJoinYn = "N";
                    } else {
                        missionJoinYn = "Y";
                    }

                    saveMissionParticipantsDtoList.add(RequestMissionParticipantsDto.builder()
                            .missionSeq(allMission.getMissionSeq())
                            .userSeq(collect.get(0).getUserSeq())
                            .userId(collect.get(0).getUserId())
                            .missionJoinDt(LocalDateTime.now())
                            .missionJoinYn(missionJoinYn)
                            .missionJoinApprovalDt(LocalDateTime.now())
                            .build()
                            .toEntity());
                }
            } else if (allMission.getMissionNm().contains("이준범")) {
                for (int i = 0; i < 6; i++) {
                    String userId = "kakaoUser" + i;
                    List<User> collect = allUsers.stream()
                            .filter(user -> user.getUserId().equals(userId))
                            .collect(Collectors.toList());

                    if (i % 3 == 0) {
                        missionJoinYn = "N";
                    } else {
                        missionJoinYn = "Y";
                    }
                    saveMissionParticipantsDtoList.add(RequestMissionParticipantsDto.builder()
                            .missionSeq(allMission.getMissionSeq())
                            .userSeq(collect.get(0).getUserSeq())
                            .userId(collect.get(0).getUserId())
                            .missionJoinDt(LocalDateTime.now())
                            .missionJoinYn(missionJoinYn)
                            .missionJoinApprovalDt(LocalDateTime.now())
                            .build()
                            .toEntity());
                }
            } else if (allMission.getMissionNm().contains("이영우")) {
                missionJoinYn = "Y";
                for (int i = 0; i < 6; i++) {
                    String userId = "naverUser" + i;
                    List<User> collect = allUsers.stream()
                            .filter(user -> user.getUserId().equals(userId))
                            .collect(Collectors.toList());

                    saveMissionParticipantsDtoList.add(RequestMissionParticipantsDto.builder()
                            .missionSeq(allMission.getMissionSeq())
                            .userSeq(collect.get(0).getUserSeq())
                            .userId(collect.get(0).getUserId())
                            .missionJoinDt(LocalDateTime.now())
                            .missionJoinYn(missionJoinYn)
                            .missionJoinApprovalDt(LocalDateTime.now())
                            .build()
                            .toEntity());
                }
            }
        }

        return saveMissionParticipantsDtoList;
    }


    // 미션 현황 등록
    private List<MissionState> createMissionStateDto() {
        List<MissionState> saveMissionStateDtoList = new ArrayList<>();

        List<MissionParticipants> allMissionParticipants = missionParticipantsRepository.findAll();

        for (MissionParticipants allMissionParticipant : allMissionParticipants) {

            if (allMissionParticipant.getUserId().contains("google")) { // 한상우 미션

                if ("googleUser0".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "자바 메모리 구조", "자바의 기본적인 메모리 구조에 대한 학습", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "자바 GC 알고리즘", "자바의 기본적인 GC 알고리즘에 대한 학습", "image test 2", "Y"));
                    saveMissionStateDtoList.add(createMissionState(3, allMissionParticipant, "스프링 프레임워크 구조", "스프링 프레임워크 기본 원리에 대한 학습", "image test 3", "Y"));
                    saveMissionStateDtoList.add(createMissionState(4, allMissionParticipant, "스프링 컨테이너 구조", "스프링 컨테이너에 대한 학습", "image test 3", "N"));
                } else if ("googleUser1".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "자바 메모리 구조", "자바의 기본적인 메모리 구조에 대한 학습", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "자바 GC 알고리즘", "자바의 기본적인 GC 알고리즘에 대한 학습", "image test 2", "N"));
                } else if ("googleUser2".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "자바 메모리 구조", "자바의 기본적인 메모리 구조에 대한 학습", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "자바 GC 알고리즘", "자바의 기본적인 GC 알고리즘에 대한 학습", "image test 2", "Y"));
                    saveMissionStateDtoList.add(createMissionState(3, allMissionParticipant, "스프링 프레임워크 구조", "스프링 프레임워크 기본 원리에 대한 학습", "image test 3", "N"));
                } else {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "자바 메모리 구조", "자바의 기본적인 메모리 구조에 대한 학습", "image test 1", "N"));
                }

            } else if (allMissionParticipant.getUserId().contains("kakao")) { // 이준범 미션

                if ("kakaoUser0".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "난 어느회사와 어울릴까", "목표를 정해보자", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "자바란 무엇인가?", "자바 공부를 위한 단계별 학습", "image test 2", "Y"));
                    saveMissionStateDtoList.add(createMissionState(3, allMissionParticipant, "자바 언어 기초", "자바 언어의 기초에 대한 학습", "image test 3", "Y"));
                    saveMissionStateDtoList.add(createMissionState(4, allMissionParticipant, "메모리구조부터 하나씩", "자바 메모리 구조에 대한 학습", "image test 3", "N"));
                } else if ("kakaoUser1".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "난 어느회사와 어울릴까", "목표를 정해보자", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "자바란 무엇인가?", "자바 공부를 위한 단계별 학습", "image test 2", "N"));
                } else {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "자바 메모리 구조", "자바의 기본적인 메모리 구조에 대한 학습", "image test 1", "N"));
                }

            } else if (allMissionParticipant.getUserId().contains("naver")) { // 이영우 미션

                if ("naverUser0".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "개발자가 되기 위한 첫걸음", "나는 어떤 개발자가 되려는가", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "어떤 공부부터 해야할까", "웹 개발자가 되기 위해 어떤 공부부터 시작해야할까", "image test 2", "Y"));
                    saveMissionStateDtoList.add(createMissionState(3, allMissionParticipant, "프로젝트 수행에서 역할 나누기", "프로젝트를 시작하기 전, 각자 역할을 나누는 방법", "image test 3", "Y"));
                    saveMissionStateDtoList.add(createMissionState(4, allMissionParticipant, "스프링 기본 세팅", "스프링 기본 세팅으로 화면 띄우기", "image test 4", "Y"));
                    saveMissionStateDtoList.add(createMissionState(5, allMissionParticipant, "URI 매핑하기", "Controller 를 활용한 URI 매핑", "image test 5", "N"));
                } else if ("naverUser1".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "개발자가 되기 위한 첫걸음", "나는 어떤 개발자가 되려는가", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "어떤 공부부터 해야할까", "웹 개발자가 되기 위해 어떤 공부부터 시작해야할까", "image test 2", "Y"));
                    saveMissionStateDtoList.add(createMissionState(3, allMissionParticipant, "프로젝트 수행에서 역할 나누기", "프로젝트를 시작하기 전, 각자 역할을 나누는 방법", "image test 3", "Y"));
                    saveMissionStateDtoList.add(createMissionState(4, allMissionParticipant, "스프링 기본 세팅", "스프링 기본 세팅으로 화면 띄우기", "image test 4", "N"));
                } else if ("naverUser2".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "개발자가 되기 위한 첫걸음", "나는 어떤 개발자가 되려는가", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "어떤 공부부터 해야할까", "웹 개발자가 되기 위해 어떤 공부부터 시작해야할까", "image test 2", "N"));
                } else if ("naverUser3".equals(allMissionParticipant.getUserId())) {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "개발자가 되기 위한 첫걸음", "나는 어떤 개발자가 되려는가", "image test 1", "Y"));
                    saveMissionStateDtoList.add(createMissionState(2, allMissionParticipant, "어떤 공부부터 해야할까", "웹 개발자가 되기 위해 어떤 공부부터 시작해야할까", "image test 2", "Y"));
                    saveMissionStateDtoList.add(createMissionState(3, allMissionParticipant, "프로젝트 수행에서 역할 나누기", "프로젝트를 시작하기 전, 각자 역할을 나누는 방법", "image test 3", "Y"));
                    saveMissionStateDtoList.add(createMissionState(4, allMissionParticipant, "스프링 기본 세팅", "스프링 기본 세팅으로 화면 띄우기", "image test 4", "Y"));
                    saveMissionStateDtoList.add(createMissionState(5, allMissionParticipant, "URI 매핑하기", "Controller 를 활용한 URI 매핑", "image test 5", "Y"));
                    saveMissionStateDtoList.add(createMissionState(6, allMissionParticipant, "MVC 패턴이 무엇인가요", "Spring MVC 패턴에 대한 학습", "image test 6", "N"));
                } else {
                    saveMissionStateDtoList.add(createMissionState(1, allMissionParticipant, "자바 메모리 구조", "자바의 기본적인 메모리 구조에 대한 학습", "image test 1", "N"));
                }

            }
        }


        return saveMissionStateDtoList;
    }






    // RequestMissionStateDto 생성
    private MissionState createMissionState(
            long missionStateWeek,
            MissionParticipants missionParticipants,
            String submittedMissionNm,
            String submittedMissionDesc,
            String submittedMissionImage,
            String approvalYn
    ) {
        MissionState requestMissionStateDto = RequestMissionStateDto.builder()
                .missionStateWeek(missionStateWeek)
                .missionParticipants(missionParticipants)
                .submittedMissionNm(submittedMissionNm)
                .submittedMissionDesc(submittedMissionDesc)
                .submittedMissionImage(submittedMissionImage)
                .submittedMissionDt(LocalDateTime.now())
                .approvalYn(approvalYn)
                .approvalDt(LocalDateTime.now())
                .build()
                .toEntity();

        return requestMissionStateDto;
    }

}
