package com.n2t.autocart.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseWrapper<T> {
    private Instant timestamp;
    private Boolean success;
    private String message;
    private int statusCode;
    private T data;
}
