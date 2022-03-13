package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMission is a Querydsl query type for Mission
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMission extends EntityPathBase<Mission> {

    private static final long serialVersionUID = 210500646L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMission mission = new QMission("mission");

    public final QBaseTime _super = new QBaseTime(this);

    public final StringPath autoAccessYn = createString("autoAccessYn");

    public final StringPath closeYn = createString("closeYn");

    public final StringPath delYn = createString("delYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDtm = _super.insDtm;

    //inherited
    public final StringPath insUser = _super.insUser;

    public final StringPath masterYn = createString("masterYn");

    public final StringPath missionDesc = createString("missionDesc");

    public final DateTimePath<java.time.LocalDateTime> missionEndDt = createDateTime("missionEndDt", java.time.LocalDateTime.class);

    public final StringPath missionImage = createString("missionImage");

    public final StringPath missionNm = createString("missionNm");

    public final ListPath<MissionParticipants, QMissionParticipants> MissionParticipants = this.<MissionParticipants, QMissionParticipants>createList("MissionParticipants", MissionParticipants.class, QMissionParticipants.class, PathInits.DIRECT2);

    public final NumberPath<Long> missionSeq = createNumber("missionSeq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> missionStDt = createDateTime("missionStDt", java.time.LocalDateTime.class);

    public final StringPath releaseYn = createString("releaseYn");

    public final StringPath temporaryYn = createString("temporaryYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDtm = _super.updDtm;

    //inherited
    public final StringPath updUser = _super.updUser;

    public final QUser user;

    public QMission(String variable) {
        this(Mission.class, forVariable(variable), INITS);
    }

    public QMission(Path<? extends Mission> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMission(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMission(PathMetadata metadata, PathInits inits) {
        this(Mission.class, metadata, inits);
    }

    public QMission(Class<? extends Mission> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

