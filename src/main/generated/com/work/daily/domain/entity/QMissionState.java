package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissionState is a Querydsl query type for MissionState
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissionState extends EntityPathBase<MissionState> {

    private static final long serialVersionUID = 1563525131L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissionState missionState = new QMissionState("missionState");

    public final DateTimePath<java.time.LocalDateTime> approvalDt = createDateTime("approvalDt", java.time.LocalDateTime.class);

    public final StringPath approvalYn = createString("approvalYn");

    public final QMissionParticipants missionParticipants;

    public final NumberPath<Long> missionStateSeq = createNumber("missionStateSeq", Long.class);

    public final NumberPath<Long> missionStateWeek = createNumber("missionStateWeek", Long.class);

    public final StringPath rejectionDesc = createString("rejectionDesc");

    public final DateTimePath<java.time.LocalDateTime> rejectionDt = createDateTime("rejectionDt", java.time.LocalDateTime.class);

    public final StringPath rejectionYn = createString("rejectionYn");

    public final StringPath submittedMissionDesc = createString("submittedMissionDesc");

    public final DateTimePath<java.time.LocalDateTime> submittedMissionDt = createDateTime("submittedMissionDt", java.time.LocalDateTime.class);

    public final StringPath submittedMissionImage = createString("submittedMissionImage");

    public final StringPath submittedMissionNm = createString("submittedMissionNm");

    public QMissionState(String variable) {
        this(MissionState.class, forVariable(variable), INITS);
    }

    public QMissionState(Path<? extends MissionState> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissionState(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissionState(PathMetadata metadata, PathInits inits) {
        this(MissionState.class, metadata, inits);
    }

    public QMissionState(Class<? extends MissionState> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.missionParticipants = inits.isInitialized("missionParticipants") ? new QMissionParticipants(forProperty("missionParticipants"), inits.get("missionParticipants")) : null;
    }

}

