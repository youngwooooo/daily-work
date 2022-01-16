package com.work.daily.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1988425871L;

    public static final QUser user = new QUser("user");

    public final QBaseTime _super = new QBaseTime(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDtm = _super.insDtm;

    //inherited
    public final StringPath insUser = _super.insUser;

    public final StringPath profileImage = createString("profileImage");

    public final StringPath provider = createString("provider");

    public final StringPath providerId = createString("providerId");

    public final EnumPath<com.work.daily.domain.UserRole> role = createEnum("role", com.work.daily.domain.UserRole.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDtm = _super.updDtm;

    //inherited
    public final StringPath updUser = _super.updUser;

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userId = createString("userId");

    public final StringPath userNm = createString("userNm");

    public final StringPath userPw = createString("userPw");

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

