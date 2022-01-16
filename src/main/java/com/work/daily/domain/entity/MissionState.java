package com.work.daily.domain.entity;

import com.work.daily.domain.pk.MissionStatePK;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MissionStatePK.class)
@Entity
@Table(name = "TB_MISSION_STATE")
public class MissionState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long missionStateSeq;

    @Id
    private long missionStateWeek;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "MISSION_SEQ"),
            @JoinColumn(name = "USER_SEQ"),
            @JoinColumn(name = "USER_ID")
    })
    private MissionParticipants missionParticipants;

    @Column(columnDefinition = "varchar(100) comment '제출미션'")
    private String submittedMission;

    @Column(columnDefinition = "varchar(60) comment '미션제출일'")
    private LocalDateTime submittedMissionDt;

    @Column(columnDefinition = "varchar(10) comment '승인여부'")
    private String approvalYn;

    @Column(columnDefinition = "varchar(60) comment '승인일자'")
    private LocalDateTime approvalDt;


}
