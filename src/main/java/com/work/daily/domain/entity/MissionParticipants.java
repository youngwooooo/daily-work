package com.work.daily.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
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

    @Id
    private User user;

    @Column(columnDefinition = "varchar(60) comment '미션참여일'")
    private LocalDateTime missionJoinDt;

    @Column(columnDefinition = "varchar(60) comment '참여여부'")
    private String missionJoinYn;

    @Column(columnDefinition = "varchar(60) comment '참여승인일'")
    private LocalDateTime missionJoinApprovalDt;
}

@EqualsAndHashCode
class MissionParticipantsPK implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MISSION_SEQ")
    private Mission missionSeq;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "USER_SEQ"),
            @JoinColumn(name = "USER_ID")
    })
    private User user;

}
