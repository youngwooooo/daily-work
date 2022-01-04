package com.work.daily.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


public class TbAsrsWrkInfo {

    @Id
    private String wrkOrdDt;

    @Id
    private String wrkOrdNo;

    @MapsId("wrkOrdDt") // TbIobdOrdReq.wrkOrdDt 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "WRK_ORD_DT")
    })
    private TbIobdOrdReq tbIobdOrdReq1;

    @MapsId("wrkOrdNo") // TbIobdOrdReq.wrkOrdNo 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "WRK_ORD_NO")
    })
    private TbIobdOrdReq tbIobdOrdReq2;

    private String ecsWrkOrdNo;
    private String prio;
    private String wrkTyp;
    private String fromStn;
    private String toStn;
    private String fromScNo;
    private String fromZone;
    private String fromLoc;
    private String toScNo;
    private String toZone;
    private String toLoc;
    private String pltNo;
    private String wrkStat;
    private String delReq;
    private String errCd;

    private String insDtm;
    private String insUser;
    private String insProc;
    private String insTmnl;
    private String updDtm;
    private String updUser;
    private String updProc;
    private String updTmnl;

    @Builder
    public TbAsrsWrkInfo(String ecsWrkOrdNo, String prio, String wrkTyp, String fromStn, String toStn, String fromScNo, String fromZone, String fromLoc, String toScNo, String toZone, String toLoc, String pltNo, String wrkStat, String delReq, String errCd, String insDtm, String insUser, String insProc, String insTmnl, String updDtm, String updUser, String updProc, String updTmnl) {
        this.ecsWrkOrdNo = ecsWrkOrdNo;
        this.prio = prio;
        this.wrkTyp = wrkTyp;
        this.fromStn = fromStn;
        this.toStn = toStn;
        this.fromScNo = fromScNo;
        this.fromZone = fromZone;
        this.fromLoc = fromLoc;
        this.toScNo = toScNo;
        this.toZone = toZone;
        this.toLoc = toLoc;
        this.pltNo = pltNo;
        this.wrkStat = wrkStat;
        this.delReq = delReq;
        this.errCd = errCd;
        this.insDtm = insDtm;
        this.insUser = insUser;
        this.insProc = insProc;
        this.insTmnl = insTmnl;
        this.updDtm = updDtm;
        this.updUser = updUser;
        this.updProc = updProc;
        this.updTmnl = updTmnl;
    }

    @Embeddable
    public class TbAsrsWrkInfoId implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "WRK_ORD_DT")
        private String wrkOrdDt;

        @Column(name="WRK_ORD_NO")
        private long wrkOrdNo;
    }
}
