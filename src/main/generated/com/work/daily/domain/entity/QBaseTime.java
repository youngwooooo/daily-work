package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseTime is a Querydsl query type for BaseTime
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseTime extends EntityPathBase<BaseTime> {

    private static final long serialVersionUID = 1715852772L;

    public static final QBaseTime baseTime = new QBaseTime("baseTime");

    public final DateTimePath<java.time.LocalDateTime> insDtm = createDateTime("insDtm", java.time.LocalDateTime.class);

    public final StringPath insUser = createString("insUser");

    public final DateTimePath<java.time.LocalDateTime> updDtm = createDateTime("updDtm", java.time.LocalDateTime.class);

    public final StringPath updUser = createString("updUser");

    public QBaseTime(String variable) {
        super(BaseTime.class, forVariable(variable));
    }

    public QBaseTime(Path<? extends BaseTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseTime(PathMetadata metadata) {
        super(BaseTime.class, metadata);
    }

}

