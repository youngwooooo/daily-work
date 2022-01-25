package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissionParticipants is a Querydsl query type for MissionParticipants
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissionParticipants extends EntityPathBase<MissionParticipants> {

    private static final long serialVersionUID = -1984938266L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissionParticipants missionParticipants = new QMissionParticipants("missionParticipants");

    public final QMission mission;

    public final DateTimePath<java.time.LocalDateTime> missionJoinApprovalDt = createDateTime("missionJoinApprovalDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> missionJoinDt = createDateTime("missionJoinDt", java.time.LocalDateTime.class);

    public final StringPath missionJoinYn = createString("missionJoinYn");

    public final NumberPath<Long> missionSeq = createNumber("missionSeq", Long.class);

    public final ListPath<MissionState, QMissionState> missionStates = this.<MissionState, QMissionState>createList("missionStates", MissionState.class, QMissionState.class, PathInits.DIRECT2);

    public final QUser user;

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QMissionParticipants(String variable) {
        this(MissionParticipants.class, forVariable(variable), INITS);
    }

    public QMissionParticipants(Path<? extends MissionParticipants> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissionParticipants(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissionParticipants(PathMetadata metadata, PathInits inits) {
        this(MissionParticipants.class, metadata, inits);
    }

    public QMissionParticipants(Class<? extends MissionParticipants> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mission = inits.isInitialized("mission") ? new QMission(forProperty("mission"), inits.get("mission")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

