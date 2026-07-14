package com.binhtv.articleservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationErrorDto {
    private String field;
    private String message;
    private Object rejectedValue;
}
