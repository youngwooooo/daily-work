drop table if exists tb_board_comment CASCADE;
drop table if exists tb_board_file CASCADE;
drop table if exists tb_board_info CASCADE;
drop table if exists tb_board_type CASCADE;
drop table if exists tb_mission_info CASCADE;
drop table if exists tb_mission_participants CASCADE;
drop table if exists tb_mission_state CASCADE;
drop table if exists tb_user_info CASCADE;

drop sequence if exists board_comment_seq_increase;
drop sequence if exists board_file_seq_increase;
drop sequence if exists board_seq_increase;
drop sequence if exists board_type_seq_increase;
drop sequence if exists mission_seq_increase;
drop sequence if exists mission_state_seq_increase;
drop sequence if exists user_seq_increase;

create sequence board_comment_seq_increase start with 1 increment by 1;
create sequence board_file_seq_increase start with 1 increment by 50;
create sequence board_seq_increase start with 1 increment by 1;
create sequence board_type_seq_increase start with 1 increment by 1;
create sequence mission_seq_increase start with 1 increment by 1;
create sequence mission_state_seq_increase start with 1 increment by 50;
create sequence user_seq_increase start with 1 increment by 50;

create table tb_board_comment (
    comment_seq int comment '댓글번호' not null,
    ins_dtm datetime COMMENT '생성일시',
    ins_user varchar(100) COMMENT '생성자',
    upd_dtm datetime COMMENT '수정일시',
    upd_user varchar(100) COMMENT '수정자',
    comment_desc varchar(600) comment '댓글내용',
    del_yn varchar(10) comment '삭제여부',
    parent_comment_seq varchar(60) comment '부모댓글번호',
    board_seq int comment '게시글번호',
    user_seq int comment '회원번호',
    user_id varchar(60) comment '회원ID',
    primary key (comment_seq)
);

create table tb_board_file (
    file_seq int comment '게시글파일번호' not null,
    file_origin_nm varchar(300) comment '게시글파일원본파일명',
    file_server_nm varchar(300) comment '게시글파일서버파일명',
    file_size varchar(100) comment '게시글파일크기',
    file_upload_path varchar(1000) comment '게시글파일경로',
    image_yn varchar(5) comment '이미지여부',
    ins_dtm varchar(30) comment '게시글파일등록일자',
    board_seq int comment '게시글번호',
    primary key (file_seq)
);

create table tb_board_info (
    board_seq int comment '게시글번호' not null,
    ins_dtm datetime COMMENT '생성일시',
    ins_user varchar(100) COMMENT '생성자',
    upd_dtm datetime COMMENT '수정일시',
    upd_user varchar(100) COMMENT '수정자',
    board_desc varchar(300) comment '게시판내용',
    board_nm varchar(60) comment '게시판제목',
    board_type varchar(30) comment '게시글구분',
    del_yn varchar(10) comment '삭제여부',
    temporary_yn varchar(10) comment '임시여부',
    view_count number comment '조회수',
    user_seq int comment '회원번호',
    user_id varchar(60) comment '회원ID',
    primary key (board_seq)
);

create table tb_board_type (
    board_type_seq int comment '게시글분류번호' not null,
    ins_dtm datetime COMMENT '생성일시',
    ins_user varchar(100) COMMENT '생성자',
    upd_dtm datetime COMMENT '수정일시',
    upd_user varchar(100) COMMENT '수정자',
    board_type_code varchar(20) comment '게시글분류코드',
    board_type_nm varchar(20) comment '게시글분류명',
    primary key (board_type_seq)
);

create table tb_mission_info (
    mission_seq int comment '미션번호' not null,
    ins_dtm datetime COMMENT '생성일시',
    ins_user varchar(100) COMMENT '생성자',
    upd_dtm datetime COMMENT '수정일시',
    upd_user varchar(100) COMMENT '수정자',
    auto_access_yn varchar(10) comment '자동참여여부',
    close_yn varchar(10) comment '마감여부',
    del_yn varchar(10) comment '삭제여부',
    master_yn varchar(10) comment '방장여부',
    mission_desc varchar(1000) comment '미션설명',
    mission_end_dt datetime comment '미션종료일',
    mission_image varchar(1000) comment '미션이미지',
    mission_nm varchar(60) comment '미션명',
    mission_st_dt datetime comment '미션시작일',
    release_yn varchar(10) comment '공개여부',
    temporary_yn varchar(10) comment '임시여부',
    user_seq int comment '회원번호',
    user_id varchar(60) comment '회원ID',
    primary key (mission_seq)
);

create table tb_mission_participants (
    user_seq bigint not null,
    user_id varchar(255) not null,
    mission_seq bigint not null,
    mission_join_approval_dt varchar(60) comment '참여승인일',
    mission_join_dt varchar(60) comment '미션참여일',
    mission_join_yn varchar(60) comment '참여여부',
    primary key (user_seq, user_id, mission_seq)
);

create table tb_mission_state (
    mission_state_week int comment '미션주차' not null,
    mission_state_seq int comment '미션현황번호' not null,
    approval_dt varchar(60) comment '승인일자',
    approval_yn varchar(10) comment '승인여부',
    rejection_desc varchar(100) comment '반려내용',
    rejection_dt varchar(60) comment '반려일자',
    rejection_yn varchar(10) comment '반려여부',
    submitted_mission_desc varchar(100) comment '제출미션설명',
    submitted_mission_dt varchar(60) comment '미션제출일',
    submitted_mission_image varchar(1000) comment '제출미션이미지',
    submitted_mission_nm varchar(60) comment '제출미션명',
    user_seq bigint,
    user_id varchar(255),
    mission_seq bigint,
    primary key (mission_state_week, mission_state_seq)
);

create table tb_user_info (
    user_seq int comment '회원번호' not null,
    user_id varchar(60) comment '회원ID' not null,
    ins_dtm datetime COMMENT '생성일시',
    ins_user varchar(100) COMMENT '생성자',
    upd_dtm datetime COMMENT '수정일시',
    upd_user varchar(100) COMMENT '수정자',
    profile_image varchar(500) comment '프로필사진',
    provider varchar(10) comment '소셜타입',
    provider_id varchar(255) comment '소셜ID',
    role varchar(10) comment '권한',
    user_email varchar(100) comment '이메일',
    user_nm varchar(20) comment '이름',
    user_pw varchar(300) comment '비밀번호',
    primary key (user_seq, user_id)
);

alter table tb_board_comment
    add constraint FKske8fu3twy6gtdp2i1xc9p8cc
    foreign key (board_seq)
    references tb_board_info;

alter table tb_board_comment
    add constraint FKk0sd1930e7muygyfgw0rw11xg
    foreign key (user_seq, user_id)
    references tb_user_info;

alter table tb_board_file
    add constraint FKtormygr34t1iivwn6svloxbt4
    foreign key (board_seq)
    references tb_board_info;

alter table tb_board_info
    add constraint FKnvfq4xpb5xdssdu2gc1a310hs
    foreign key (user_seq, user_id)
    references tb_user_info;

alter table tb_mission_info
    add constraint FKi6s3o24a9mvm5eil9d87j7rc1
    foreign key (user_seq, user_id)
    references tb_user_info;

alter table tb_mission_participants
    add constraint FKk8pdaephcjihysd0j05vwe04w
    foreign key (mission_seq)
    references tb_mission_info;

alter table tb_mission_participants
    add constraint FK5ryjrigluwsrb895ioa7kidtx
    foreign key (user_seq, user_id)
    references tb_user_info;

alter table tb_mission_state
    add constraint FKp0dhs6r20g3p9u8u075y264vx
    foreign key (user_seq, user_id, mission_seq)
    references tb_mission_participants
    on delete cascade;