package com.chenfuzhu.aiagent.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter


public enum ErrorCode {
    SUCCESS(20000, "成功", HttpStatus.OK),
    PARAMS_ERROR(40000, "参数错误", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(40100, "未授权", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(40300, "禁止访问", HttpStatus.FORBIDDEN),
    NOT_FOUND(40400, "资源不存在", HttpStatus.NOT_FOUND),
    SYSTEM_ERROR(50000, "系统错误", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

