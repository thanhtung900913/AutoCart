package com.n2t.autocart.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // General Errors
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống không xác định"),
    INVALID_KEY(HttpStatus.BAD_REQUEST, "Invalid message key"),

    // Auth & User Errors (1000 - 1999)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "Email này đã được đăng ký"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Bạn chưa đăng nhập"),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập tài nguyên này"),

    // Product & Cart Errors (2000 - 2999)
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "Sản phẩm đã hết hàng trong kho"),

    // Order Errors (3000 - 3999)
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng");

    private final HttpStatus statusCode;
    private final String message;

    ErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}