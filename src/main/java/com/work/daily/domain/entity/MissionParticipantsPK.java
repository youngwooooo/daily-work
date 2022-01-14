package com.work.daily.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
public class MissionParticipantsPK implements Serializable {

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
