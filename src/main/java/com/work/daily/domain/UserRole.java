package com.work.daily.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("USER")
    , ADMIN("ADMIN");

    private String value;
}
