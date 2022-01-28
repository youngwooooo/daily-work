package com.work.daily.init;

import com.work.daily.access.dto.JoinUserDto;
import com.work.daily.domain.UserRole;
import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.User;
import com.work.daily.domain.repository.MissionRepository;
import com.work.daily.domain.repository.UserRepository;
import com.work.daily.domain.repository.custom.MissionRepositoryCustom;
import com.work.daily.domain.repository.custom.impl.MissionRepositoryCustomImpl;
import com.work.daily.mission.dto.RequestMissionDto;
import com.work.daily.mission.dto.ResponseMissionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Configuration
@Component("DbMissionInsert")
public class DbMissionInsert {
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;

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
}
