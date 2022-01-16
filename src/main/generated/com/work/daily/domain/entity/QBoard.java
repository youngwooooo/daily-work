package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -1529329664L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final QBaseTime _super = new QBaseTime(this);

    public final StringPath boardDesc = createString("boardDesc");

    public final StringPath boardNm = createString("boardNm");

    public final NumberPath<Long> boardSeq = createNumber("boardSeq", Long.class);

    public final StringPath boardType = createString("boardType");

    public final StringPath delYn = createString("delYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDtm = _super.insDtm;

    //inherited
    public final StringPath insUser = _super.insUser;

    public final StringPath temporaryYn = createString("temporaryYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDtm = _super.updDtm;

    //inherited
    public final StringPath updUser = _super.updUser;

    public final QUser user;

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

