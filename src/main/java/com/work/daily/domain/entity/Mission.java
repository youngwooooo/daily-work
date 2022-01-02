//package com.work.daily.domain.entity;
//
//import lombok.*;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Getter
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "TB_MISSION")
//public class Mission {
//
//    @Id
//    private String missionSeq;
//
//    @MapsId("userSeq") // User.userSeq 매핑
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "USER_SEQ")
//    })
//    private User user1;
//
//    @MapsId("userId") // User.userId 매핑
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "USER_ID")
//    })
//    private User user2;
//
//    private String missionNm;
//    private String missionDesc;
//    private String missionStDt;
//    private String missionEndDt;
//    private String releaseYn;
//    private String autoAccessYn;
//    private String masterYn;    /* 방장여부 */
//    private String delYn;       /* 삭제여부 */
//
//    @Column(columnDefinition = "varchar(10) not null comment '임시여부'")
//    private String temporaryYn; /* 임시여부 */
//
//    @Column(columnDefinition = "varchar(10) not null comment '미션만족도'")
//    private String reviewGrade; /* 미션만족도 */
//
//    private String insDtm;
//    private String insUser;
//    private String updDtm;
//    private String updUser;
//
//}
