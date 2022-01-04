package com.work.daily.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {

    @CreatedDate
    @Column(updatable = false, columnDefinition = "TEXT COMMENT '생성일시'")
    private LocalDateTime insDtm;

    @Column(columnDefinition = "TEXT COMMENT '생성자'")
    private String insUser;

    @LastModifiedDate
    @Column(columnDefinition = "TEXT COMMENT '수정일시'")
    private String updDtm;

    @Column(columnDefinition = "TEXT COMMENT '수정자'")
    private String updUser;

}