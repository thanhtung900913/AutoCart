package com.n2t.autocart.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

// JsonInclude.Include.NON_NULL giúp ẩn đi các field bị null (ví dụ: errors)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors // Chỉ dùng khi có lỗi validate form (VD: email sai định dạng)
) {
    // Constructor tiện ích cho các lỗi thông thường
    public ErrorResponse(int status, String error, String message, String path) {
        this(Instant.now(), status, error, message, path, null);
    }
}
