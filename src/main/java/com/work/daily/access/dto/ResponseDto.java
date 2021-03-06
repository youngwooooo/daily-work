package com.work.daily.access.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseDto {

    private int status;
    private String message;
    private Object data;
}
