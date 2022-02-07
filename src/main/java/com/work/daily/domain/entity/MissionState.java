package com.work.daily.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.work.daily.domain.pk.MissionStatePK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MissionStatePK.class)
@Entity
@Table(name = "TB_MISSION_STATE")
public class MissionState {

    @Id
    private long missionStateSeq;

    @Id
    private long missionStateWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "MISSION_SEQ" , referencedColumnName = "missionSeq"),
            @JoinColumn(name = "USER_SEQ", referencedColumnName = "userSeq"),
            @JoinColumn(name = "USER_ID" , referencedColumnName = "userId")
    })
    @JsonBackReference
    private MissionParticipants missionParticipants;

    @Column(columnDefinition = "varchar(60) comment '제출미션명'")
    private String submittedMissionNm;

    @Column(columnDefinition = "varchar(100) comment '제출미션설명'")
    private String submittedMissionDesc;

    @Column(columnDefinition = "varchar(100) comment '제출미션이미지'")
    private String submittedMissionImage;

    @Column(columnDefinition = "varchar(60) comment '미션제출일'")
    private LocalDateTime submittedMissionDt;

    @Column(columnDefinition = "varchar(10) comment '승인여부'")
    private String approvalYn;

    @Column(columnDefinition = "varchar(60) comment '승인일자'")
    private LocalDateTime approvalDt;

    @Column(columnDefinition = "varchar(10) comment '반려여부'")
    private String rejectionYn;

    @Column(columnDefinition = "varchar(60) comment '반려일자'")
    private LocalDateTime rejectionDt;

    @Column(columnDefinition = "varchar(100) comment '반려내용'")
    private String rejectionDesc;

    // 승인여부 N -> Y 수정
    public void modifyMissionStateApprovalYn(String approvalYn, LocalDateTime approvalDt){
        this.approvalYn = approvalYn;
        this.approvalDt = approvalDt;
    }

    // 반려여부 N -> Y, 반려 일자, 반려 내용 수정
    public void modifyMissionStateRejectionInfo(String rejectionYn, LocalDateTime rejectionDt, String rejectionDesc){
        this.rejectionYn = rejectionYn;
        this.rejectionDt = rejectionDt;
        this.rejectionDesc = rejectionDesc;
    }

    // 나의 제출 미션 수정(제목, 내용, 이미지, 반려 정보)
    public void modifyMyMissionState(String submittedMissionNm, String submittedMissionDesc, String submittedMissionImage, String rejectionYn, LocalDateTime rejectionDt, String rejectionDesc){
        this.submittedMissionNm = submittedMissionNm;
        this.submittedMissionDesc = submittedMissionDesc;
        this.submittedMissionImage = submittedMissionImage;
        this.rejectionYn = rejectionYn;
        this.rejectionDt = rejectionDt;
        this.rejectionDesc = rejectionDesc;
    }
}
