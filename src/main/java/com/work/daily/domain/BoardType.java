package com.work.daily.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardType {
    QA("QA") // 질문
    , NORMAL("NORMAL") // 일반
    , PR("PR"); // 홍보

    private String value;
}
