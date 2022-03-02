package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardType is a Querydsl query type for BoardType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardType extends EntityPathBase<BoardType> {

    private static final long serialVersionUID = 1872513882L;

    public static final QBoardType boardType = new QBoardType("boardType");

    public final QBaseTime _super = new QBaseTime(this);

    public final StringPath boardTypeCode = createString("boardTypeCode");

    public final StringPath boardTypeNm = createString("boardTypeNm");

    public final NumberPath<Long> boardTypeSeq = createNumber("boardTypeSeq", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDtm = _super.insDtm;

    //inherited
    public final StringPath insUser = _super.insUser;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDtm = _super.updDtm;

    //inherited
    public final StringPath updUser = _super.updUser;

    public QBoardType(String variable) {
        super(BoardType.class, forVariable(variable));
    }

    public QBoardType(Path<? extends BoardType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardType(PathMetadata metadata) {
        super(BoardType.class, metadata);
    }

}

