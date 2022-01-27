package com.work.daily.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.work.daily.mission.dto.RequestMissionDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@SequenceGenerator(
        name = "MISSION_SEQ_INCREASE",
        sequenceName = "MISSION_SEQ_INCREASE", // 시퀸스 명
        initialValue = 1, // 초기 값
        allocationSize = 1 // 미리 할당 받을 시퀸스 수
)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_MISSION_INFO")
public class Mission extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MISSION_SEQ_INCREASE")
    @Column(columnDefinition = "varchar(20) comment '미션번호'")
    private Long missionSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "USER_SEQ"),
            @JoinColumn(name = "USER_ID")
    })
    private User user;

    @Column(columnDefinition = "varchar(60) comment '미션명'")
    private String missionNm;

    @Column(columnDefinition = "varchar(1000) comment '미션설명'")
    private String missionDesc;

    @Column(columnDefinition = "varchar(60) comment '미션시작일'")
    private LocalDateTime missionStDt;

    @Column(columnDefinition = "varchar(60) comment '미션종료일'")
    private LocalDateTime missionEndDt;

    @Column(columnDefinition = "varchar(10) comment '공개여부'")
    private String releaseYn;

    @Column(columnDefinition = "varchar(10) comment '자동참여여부'")
    private String autoAccessYn;

    @Column(columnDefinition = "varchar(10) comment '방장여부'")
    private String masterYn;

    @Column(columnDefinition = "varchar(10) comment '삭제여부'")
    private String delYn;

    @Column(columnDefinition = "varchar(10) comment '임시여부'")
    private String temporaryYn;

    @Column(columnDefinition = "varchar(60) comment '미션만족도'")
    private String reviewGrade;

    @Column(columnDefinition = "varchar(200) comment '미션이미지'")
    private String missionImage;

    @OneToMany(mappedBy = "mission")
    @JsonManagedReference
    private List<MissionParticipants> MissionParticipants;

    // 미션 대표 이미지 추가 및 수정
    public void modifyMissionImage(String missionImagePath){
        this.missionImage = missionImagePath;
    }
    // 미션 수정
    public void modifyMission(RequestMissionDto requestMissionDto){
        this.missionNm = requestMissionDto.getMissionNm();
        this.missionDesc = requestMissionDto.getMissionDesc();
        this.missionStDt = requestMissionDto.getMissionStDt();
        this.missionEndDt = requestMissionDto.getMissionEndDt();
        this.releaseYn = requestMissionDto.getReleaseYn();
        this.autoAccessYn = requestMissionDto.getAutoAccessYn();
        this.temporaryYn = requestMissionDto.getTemporaryYn();
        this.missionImage = requestMissionDto.getMissionImage();
    }
    // 삭제여부 수정
    public void deleteMission(String delYn){
        this.delYn = delYn;
    }

}
