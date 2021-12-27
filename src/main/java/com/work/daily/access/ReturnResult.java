package com.work.daily.access;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnResult {
    SUCCESS("SUCCESS")
    , FAIL("FAIL")
    , ERROR("ERROR");

    private String value;

}
