package com.work.daily.mission.dto;

import com.work.daily.domain.entity.Mission;
import com.work.daily.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMissionDto {

    private Long missionSeq;
    private User user;
    private String missionNm;
    private String missionDesc;
    private LocalDateTime missionStDt;
    private LocalDateTime missionEndDt;
    private String releaseYn;
    private String autoAccessYn;
    private String masterYn;
    private String delYn;
    private String temporaryYn;
    private String reviewGrade;
    private String missionImage;
    private String closeYn;

    public ResponseMissionDto(Mission mission){
        this.missionSeq = mission.getMissionSeq();
        this.user = mission.getUser();
        this.missionNm = mission.getMissionNm();
        this.missionDesc = mission.getMissionDesc();
        this.missionStDt = mission.getMissionStDt();
        this.missionEndDt = mission.getMissionEndDt();
        this.releaseYn = mission.getReleaseYn();
        this.autoAccessYn = mission.getAutoAccessYn();
        this.masterYn = mission.getMasterYn();
        this.delYn = mission.getDelYn();
        this.temporaryYn = mission.getTemporaryYn();
        this.reviewGrade = mission.getReviewGrade();
        this.missionImage = mission.getMissionImage();
        this.closeYn = mission.getCloseYn();
    }

    public static ResponseMissionDto toPaging(Mission mission){
        return ResponseMissionDto.builder()
                .missionSeq(mission.getMissionSeq())
                .user(mission.getUser())
                .missionNm(mission.getMissionNm())
                .missionDesc(mission.getMissionNm())
                .missionStDt(mission.getMissionStDt())
                .missionEndDt(mission.getMissionEndDt())
                .releaseYn(mission.getReleaseYn())
                .autoAccessYn(mission.getAutoAccessYn())
                .masterYn(mission.getMasterYn())
                .delYn(mission.getDelYn())
                .temporaryYn(mission.getTemporaryYn())
                .reviewGrade(mission.getReviewGrade())
                .missionImage(mission.getMissionImage())
                .closeYn(mission.getCloseYn())
                .build();
    }
}
