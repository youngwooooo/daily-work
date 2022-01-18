package com.work.daily.mission.dto;

import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.User;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestMissionDto {

    private long missionSeq;

    @NotNull(message = "회원 정보가 존재하지 않습니다.")
    private User user;

    @NotBlank(message = "미션 제목을 입력해주세요.")
    private String missionNm;

    @NotBlank(message = "미션에 대한 설명 또는 소개를 입력해주세요.")
    private String missionDesc;

    @NotNull(message = "미션 시작일을 설정해주세요.")
    private LocalDateTime missionStDt;

    @NotNull(message = "미션 종료일을 설정해주세요.")
    @Future(message = "미션 종료일은 현재 시간 이후여야 합니다.")
    private LocalDateTime missionEndDt;

    @NotBlank(message = "공개 여부를 설정해주세요.")
    private String releaseYn;

    @NotBlank(message = "자동참여 여부를 설정해주세요")
    private String autoAccessYn;

    @NotBlank(message = "방장 여부를 설정해주세요")
    private String masterYn;

    @NotBlank(message = "삭제 여부를 설정해주세요")
    private String delYn;

    @NotBlank(message = "임시 여부를 설정해주세요")
    private String temporaryYn;

    private String missionImage;

    public Mission toEntity(){
        return Mission.builder()
                        .user(user)
                        .missionNm(missionNm)
                        .missionDesc(missionDesc)
                        .missionStDt(missionStDt)
                        .missionEndDt(missionEndDt)
                        .releaseYn(releaseYn)
                        .autoAccessYn(autoAccessYn)
                        .masterYn(masterYn)
                        .delYn(delYn)
                        .temporaryYn(temporaryYn)
                        .missionImage(missionImage)
                        .build();
    }
}
