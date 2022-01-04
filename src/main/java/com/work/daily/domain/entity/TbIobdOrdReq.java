package com.work.daily.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


public class TbIobdOrdReq {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성전략 - 수동
    private String stnNo;

    private String pltNo;
    private String wrkTyp;
    private String toStn;

    @Column(name = "WRK_ORD_DT")
    private String wrkOrdDt;

    @Column(name = "WRK_ORD_NO")
    private String wrkOrdNo;
    private String frghtYn;
    private String procStat;
    private String insDtm;
    private String insUser;
    private String insProc;
    private String insTmnl;
    private String updDtm;
    private String updUser;
    private String updProc;
    private String updTmnl;

    @Builder

    public TbIobdOrdReq(String stnNo, String pltNo, String wrkTyp, String toStn, String wrkOrdDt, String wrkOrdNo, String frghtYn, String procStat, String insDtm, String insUser, String insProc, String insTmnl, String updDtm, String updUser, String updProc, String updTmnl) {
        this.stnNo = stnNo;
        this.pltNo = pltNo;
        this.wrkTyp = wrkTyp;
        this.toStn = toStn;
        this.wrkOrdDt = wrkOrdDt;
        this.wrkOrdNo = wrkOrdNo;
        this.frghtYn = frghtYn;
        this.procStat = procStat;
        this.insDtm = insDtm;
        this.insUser = insUser;
        this.insProc = insProc;
        this.insTmnl = insTmnl;
        this.updDtm = updDtm;
        this.updUser = updUser;
        this.updProc = updProc;
        this.updTmnl = updTmnl;
    }

}
