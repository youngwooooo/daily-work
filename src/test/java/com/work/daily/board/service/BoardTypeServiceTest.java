package com.work.daily.board.service;

import com.work.daily.board.dto.BoardTypeDto;
import com.work.daily.domain.entity.BoardType;
import com.work.daily.domain.repository.BoardTypeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BoardTypeService :: 단위 테스트")
public class BoardTypeServiceTest {

    @Mock
    private BoardTypeRepository boardTypeRepository;

    @InjectMocks
    private BoardTypeService boardTypeService;

    @Test
    @Order(1)
    @DisplayName("전체 게시글 타입(카테고리) 조회")
    public void findAllBoardType() {
        // given
        List<BoardType> list = new ArrayList<>();

        BoardType boardType1 = BoardType.builder().boardTypeSeq(1).boardTypeNm("일반").boardTypeCode("NORMAL").build();
        BoardType boardType2 = BoardType.builder().boardTypeSeq(2).boardTypeNm("질문").boardTypeCode("QA").build();
        BoardType boardType3 = BoardType.builder().boardTypeSeq(3).boardTypeNm("홍보").boardTypeCode("PR").build();

        list.add(boardType1);
        list.add(boardType2);
        list.add(boardType3);

        given(boardTypeRepository.findAll()).willReturn(list);

        // when
        List<BoardTypeDto> findAllBoardType = boardTypeService.findAllBoardType();

        // then
        assertThat(list.size()).isEqualTo(findAllBoardType.size());
        for(int i=0; i<findAllBoardType.size(); i++){
            assertThat(findAllBoardType.get(i).getBoardTypeSeq()).isEqualTo(list.get(i).getBoardTypeSeq());
            assertThat(findAllBoardType.get(i).getBoardTypeNm()).isEqualTo(list.get(i).getBoardTypeNm());
            assertThat(findAllBoardType.get(i).getBoardTypeCode()).isEqualTo(list.get(i).getBoardTypeCode());
        }
    }
}