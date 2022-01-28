package com.work.daily.init;

import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.repository.MissionParticipantsRepository;
import com.work.daily.domain.repository.MissionRepository;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.mission.dto.RequestMissionDto;
import com.work.daily.mission.dto.RequestMissionParticipantsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    }




    // 미션 , 미션 마스터 등록
    private List<Mission> createMissionMasterDto() {
        List<Mission> saveMissionDtoList = new ArrayList<>();

        saveMissionDtoList.add(
                RequestMissionDto.builder()
                    .missionNm("한상우의 코딩특강")
                    .missionDesc("선후배가 함께하는 코딩 특강으로 코딩 know잼이 되세요!")
                    .missionStDt(LocalDateTime.now())
                    .missionEndDt(LocalDateTime.MAX)
                    .releaseYn("Y")
                    .autoAccessYn("N")
                    .masterYn("Y")
                    .user(userRepository.findByUserId("sangWooHan").get())
                    .delYn("N")
                    .temporaryYn("N")
                    .build()
                    .toEntity()
        );

        saveMissionDtoList.add(
                RequestMissionDto.builder()
                        .missionNm("이준범의 한걸음씩 나가는 코딩교실")
                        .missionDesc("나도 모르는데 너도 모르니? 그러니까 같이 공부하자!")
                        .missionStDt(LocalDateTime.now())
                        .missionEndDt(LocalDateTime.MAX)
                        .releaseYn("Y")
                        .autoAccessYn("N")
                        .masterYn("Y")
                        .user(userRepository.findByUserId("junBeomLee").get())
                        .delYn("N")
                        .temporaryYn("N")
                        .build()
                        .toEntity()
        );

        saveMissionDtoList.add(
                RequestMissionDto.builder()
                        .missionNm("이영우의 코딩스터디")
                        .missionDesc("웹 개발자로 취업하기 위해 발버둥을 쳐보자!!")
                        .missionStDt(LocalDateTime.now())
                        .missionEndDt(LocalDateTime.MAX)
                        .releaseYn("Y")
                        .autoAccessYn("Y")
                        .masterYn("Y")
                        .user(userRepository.findByUserId("youngWooLee").get())
                        .delYn("N")
                        .temporaryYn("N")
                        .build()
                        .toEntity()
        );

        return saveMissionDtoList;
    }

    // 미션 참여자 등록
    private List<Mission> createMissionParticipantsDto() {

        List<MissionParticipants> saveMissionDtoList = new ArrayList<>();
        List<MissionParticipants> all = missionParticipantsRepository.findAll()
                .stream()
                .filter(missionParticipants -> missionParticipants.getMission().getMissionNm().contains("google"))
                .collect(Collectors.toList());

        RequestMissionParticipantsDto.builder()
                .missionSeq(1)
                .userSeq(1)
                .userId("d")
                .missionJoinDt(LocalDateTime.now())
                .missionJoinYn("Y")
                .missionJoinApprovalDt(LocalDateTime.now())
                .build()
                .toEntity();

        return null;
    }



}
