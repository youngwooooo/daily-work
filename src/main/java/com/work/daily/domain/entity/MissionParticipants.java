package com.work.daily.domain.entity;

import com.work.daily.domain.pk.MissionParticipantsPK;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MissionParticipantsPK.class)
@Entity
@Table(name = "TB_MISSION_PARTICIPANTS")
public class MissionParticipants {

    @Id
    private long missionSeq;

    @ManyToOne
    @JoinColumn(name = "missionSeq", insertable = false, updatable = false)
    private Mission mission;

    @Id
    private long userSeq;

    @Id
    private String userId;

    @ManyToOne
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
}
