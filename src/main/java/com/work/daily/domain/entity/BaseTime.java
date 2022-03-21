package com.work.daily.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {

    @CreatedDate
    @Column(updatable = false, columnDefinition = "datetime COMMENT '생성일시'")
    private LocalDateTime insDtm;

    @CreatedBy
    @Column(updatable = false, columnDefinition = "varchar(100) COMMENT '생성자'")
    private String insUser;

    @LastModifiedDate
    @Column(columnDefinition = "datetime COMMENT '수정일시'")
    private LocalDateTime updDtm;

    @LastModifiedBy
    @Column(columnDefinition = "varchar(100) COMMENT '수정자'")
    private String updUser;

}