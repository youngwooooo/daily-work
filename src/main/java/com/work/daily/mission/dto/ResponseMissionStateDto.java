package com.work.daily.mission.dto;

import com.work.daily.domain.entity.MissionParticipants;
import com.work.daily.domain.entity.MissionState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMissionStateDto {

    private long missionStateSeq;
    private long missionStateWeek;
    private MissionParticipants missionParticipants;
    private String submittedMissionNm;
    private String submittedMissionDesc;
    private String submittedMissionImage;
    private LocalDateTime submittedMissionDt;
    private String approvalYn;
    private LocalDateTime approvalDt;

    public ResponseMissionStateDto(MissionState missionState){
        this.missionStateSeq = missionState.getMissionStateSeq();
        this.missionStateWeek = missionState.getMissionStateWeek();
        this.missionParticipants = missionState.getMissionParticipants();
        this.submittedMissionNm = missionState.getSubmittedMissionNm();
        this.submittedMissionDesc = missionState.getSubmittedMissionDesc();
        this.submittedMissionImage = missionState.getSubmittedMissionImage();
        this.submittedMissionDt = missionState.getSubmittedMissionDt();
        this.approvalYn = missionState.getApprovalYn();
        this.approvalDt = missionState.getApprovalDt();
    }
}
