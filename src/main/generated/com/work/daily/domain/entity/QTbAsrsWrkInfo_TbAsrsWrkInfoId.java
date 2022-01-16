package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTbAsrsWrkInfo_TbAsrsWrkInfoId is a Querydsl query type for TbAsrsWrkInfoId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTbAsrsWrkInfo_TbAsrsWrkInfoId extends BeanPath<TbAsrsWrkInfo.TbAsrsWrkInfoId> {

    private static final long serialVersionUID = 262113025L;

    public static final QTbAsrsWrkInfo_TbAsrsWrkInfoId tbAsrsWrkInfoId = new QTbAsrsWrkInfo_TbAsrsWrkInfoId("tbAsrsWrkInfoId");

    public final StringPath wrkOrdDt = createString("wrkOrdDt");

    public final NumberPath<Long> wrkOrdNo = createNumber("wrkOrdNo", Long.class);

    public QTbAsrsWrkInfo_TbAsrsWrkInfoId(String variable) {
        super(TbAsrsWrkInfo.TbAsrsWrkInfoId.class, forVariable(variable));
    }

    public QTbAsrsWrkInfo_TbAsrsWrkInfoId(Path<? extends TbAsrsWrkInfo.TbAsrsWrkInfoId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbAsrsWrkInfo_TbAsrsWrkInfoId(PathMetadata metadata) {
        super(TbAsrsWrkInfo.TbAsrsWrkInfoId.class, metadata);
    }

}

