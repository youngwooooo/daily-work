package com.work.daily.mission.dto;

import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMissionParticipantsDto {

    private long missionSeq;
    private User user;
    private LocalDateTime missionJoinDt;
    private String missionJoinYn;
    private LocalDateTime missionJoinApprovalDt;

    public ResponseMissionParticipantsDto(MissionParticipants missionParticipants){
        this.missionSeq = missionParticipants.getMissionSeq();
        this.user = missionParticipants.getUser();
        this.missionJoinDt = missionParticipants.getMissionJoinDt();
        this.missionJoinYn = missionParticipants.getMissionJoinYn();
        this.missionJoinApprovalDt = missionParticipants.getMissionJoinApprovalDt();
    }

}
