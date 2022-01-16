package com.work.daily.domain.repository;

import com.work.daily.domain.entity.Board;
import com.work.daily.domain.pk.BoardCommentPk;
import com.work.daily.domain.pk.BoardPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<Board, BoardCommentPk> {
}
