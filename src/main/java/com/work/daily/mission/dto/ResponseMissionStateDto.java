package com.work.daily.mission.dto;

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
    private long missionSeq;
    private long userSeq;
    private String userId;
    private String submittedMissionNm;
    private String submittedMissionDesc;
    private String submittedMissionImage;
    private LocalDateTime submittedMissionDt;
    private String approvalYn;
    private LocalDateTime approvalDt;
    private String rejectionYn;
    private LocalDateTime rejectionDt;
    private String rejectionDesc;

    public ResponseMissionStateDto(MissionState missionState){
        this.missionStateSeq = missionState.getMissionStateSeq();
        this.missionStateWeek = missionState.getMissionStateWeek();
        this.missionSeq = missionState.getMissionParticipants().getMissionSeq();
        this.userSeq = missionState.getMissionParticipants().getUserSeq();
        this.userId = missionState.getMissionParticipants().getUserId();
        this.submittedMissionNm = missionState.getSubmittedMissionNm();
        this.submittedMissionDesc = missionState.getSubmittedMissionDesc();
        this.submittedMissionImage = missionState.getSubmittedMissionImage();
        this.submittedMissionDt = missionState.getSubmittedMissionDt();
        this.approvalYn = missionState.getApprovalYn();
        this.approvalDt = missionState.getApprovalDt();
        this.rejectionYn = missionState.getRejectionYn();
        this.rejectionDt = missionState.getRejectionDt();
        this.rejectionDesc = missionState.getRejectionDesc();
    }
}
