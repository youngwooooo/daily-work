package com.work.daily.access;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnResult {
    SUCCESS("SUCCESS")
    , FAIL("FAIL");

    private String value;

}
