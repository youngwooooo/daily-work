package com.work.daily.domain.repository;

import com.work.daily.domain.entity.BoardComment;
import com.work.daily.domain.repository.custom.BoardCommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, BoardCommentRepositoryCustom {
}
