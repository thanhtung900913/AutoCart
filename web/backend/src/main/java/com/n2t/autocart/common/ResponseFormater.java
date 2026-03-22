package com.n2t.autocart.common;

import com.n2t.autocart.common.anotation.ApiMessage;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ResponseFormater implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpStatus status = HttpStatus.valueOf(
                ((ServletServerHttpResponse) response)
                        .getServletResponse()
                        .getStatus()
        );
        if(body instanceof String){
            return body;
        }
        ResponseWrapper res = new ResponseWrapper<>();

        res.setSuccess(status.is2xxSuccessful());
        ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
        res.setMessage(
                apiMessage != null && apiMessage.value() != null && !apiMessage.value().isBlank()
                        ? apiMessage.value()
                        : "Call API Success"
        );
        res.setTimestamp(Instant.now());
        res.setData(body);
        return res;
    }
}
