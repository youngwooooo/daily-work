package com.work.daily.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.work.daily.domain.pk.MissionParticipantsPK;
import com.work.daily.mission.dto.RequestMissionParticipantsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MissionParticipantsPK.class)
@Entity
@Table(name = "TB_MISSION_PARTICIPANTS")
public class MissionParticipants {

    @Id
    private long missionSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missionSeq", insertable = false, updatable = false)
    @JsonBackReference
    private Mission mission;

    @Id
    private long userSeq;

    @Id
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "userSeq", insertable = false, updatable = false),
            @JoinColumn(name = "userId", insertable = false, updatable = false)
    })
    private User user;

    @Column(columnDefinition = "varchar(60) comment '미션참여일'")
    private LocalDateTime missionJoinDt;

    @Column(columnDefinition = "varchar(60) comment '참여여부'")
    private String missionJoinYn;

    @Column(columnDefinition = "varchar(60) comment '참여승인일'")
    private LocalDateTime missionJoinApprovalDt;

    @OneToMany(mappedBy = "missionParticipants")
    @JsonManagedReference
    private List<MissionState> missionState;

    // 참여여부, 참여승인일 수정
    public void approveParticipants(RequestMissionParticipantsDto requestMissionParticipantsDto){
        this.missionJoinYn = requestMissionParticipantsDto.getMissionJoinYn();
        this.missionJoinApprovalDt = requestMissionParticipantsDto.getMissionJoinApprovalDt();
    }
}
