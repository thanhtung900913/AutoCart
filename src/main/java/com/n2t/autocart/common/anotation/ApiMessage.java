package com.n2t.autocart.common.anotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)     // annotation dùng cho method
@Retention(RetentionPolicy.RUNTIME) // tồn tại ở runtime
@Documented
public @interface ApiMessage {
    public String value();
}