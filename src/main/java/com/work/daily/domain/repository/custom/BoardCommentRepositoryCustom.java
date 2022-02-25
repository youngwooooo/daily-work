package com.work.daily.domain.repository.custom;

import com.work.daily.domain.entity.BoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCommentRepositoryCustom {
    Page<BoardComment> findAllParentBoardComment(long boardSeq, Pageable pageable);
    List<BoardComment> findAllChildBoardComment(long boardSeq);
}
