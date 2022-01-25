package com.work.daily.mission.dto;

import com.work.daily.domain.entity.MissionState;
import com.work.daily.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMissionParticipants {

    private long missionSeq;
    private long userSeq;
    private String userId;
    private User user;
    private LocalDateTime missionJoinDt;
    private String missionJoinYn;
    private LocalDateTime missionJoinApprovalDt;
    private List<MissionState> missionStates;

}
