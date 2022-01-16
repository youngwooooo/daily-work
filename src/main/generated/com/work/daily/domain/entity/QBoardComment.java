package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardComment is a Querydsl query type for BoardComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardComment extends EntityPathBase<BoardComment> {

    private static final long serialVersionUID = -1465565633L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardComment boardComment = new QBoardComment("boardComment");

    public final QBaseTime _super = new QBaseTime(this);

    public final QBoard board;

    public final StringPath commentDesc = createString("commentDesc");

    public final NumberPath<Long> commentSeq = createNumber("commentSeq", Long.class);

    public final StringPath delYn = createString("delYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDtm = _super.insDtm;

    //inherited
    public final StringPath insUser = _super.insUser;

    public final NumberPath<Long> parentCommentSeq = createNumber("parentCommentSeq", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDtm = _super.updDtm;

    //inherited
    public final StringPath updUser = _super.updUser;

    public final QUser user;

    public QBoardComment(String variable) {
        this(BoardComment.class, forVariable(variable), INITS);
    }

    public QBoardComment(Path<? extends BoardComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardComment(PathMetadata metadata, PathInits inits) {
        this(BoardComment.class, metadata, inits);
    }

    public QBoardComment(Class<? extends BoardComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

