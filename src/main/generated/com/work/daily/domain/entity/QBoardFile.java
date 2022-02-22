package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardFile is a Querydsl query type for BoardFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardFile extends EntityPathBase<BoardFile> {

    private static final long serialVersionUID = 1872081308L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardFile boardFile = new QBoardFile("boardFile");

    public final QBoard board;

    public final StringPath fileOriginNm = createString("fileOriginNm");

    public final NumberPath<Long> fileSeq = createNumber("fileSeq", Long.class);

    public final StringPath fileServerNm = createString("fileServerNm");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final StringPath fileUploadPath = createString("fileUploadPath");

    public final StringPath imageYn = createString("imageYn");

    public final DateTimePath<java.time.LocalDateTime> insDtm = createDateTime("insDtm", java.time.LocalDateTime.class);

    public QBoardFile(String variable) {
        this(BoardFile.class, forVariable(variable), INITS);
    }

    public QBoardFile(Path<? extends BoardFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardFile(PathMetadata metadata, PathInits inits) {
        this(BoardFile.class, metadata, inits);
    }

    public QBoardFile(Class<? extends BoardFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

