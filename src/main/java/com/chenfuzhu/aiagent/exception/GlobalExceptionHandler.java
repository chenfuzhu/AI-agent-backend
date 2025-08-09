package com.chenfuzhu.aiagent.exception;

import com.chenfuzhu.aiagent.common.BaseResponse;
import com.chenfuzhu.aiagent.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Hidden
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)  // 确保最高优先级
public class GlobalExceptionHandler {

    /**
     * 处理业务异常 - 返回自定义HTTP状态码
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<?>> handleBusinessException(
            BusinessException e,
            HttpServletRequest request) {

        log.error("业务异常 [{} {}]: 错误码={}, 消息={}",
                request.getMethod(),
                request.getRequestURI(),
                e.getCode(),
                e.getMessage(),
                e);

        return ResponseEntity.status(e.getHttpStatus())
                .body(ResultUtils.error(e.getCode(), e.getMessage()));
    }

    /**
     * 处理参数校验异常 (@Validated 触发的异常)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("参数校验失败: {}", errors);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR);
    }

    /**
     * 处理JSON解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleJsonParseException(HttpMessageNotReadableException e) {
        log.warn("JSON解析错误: {}", e.getMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "JSON格式错误");
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String error = String.format("参数 '%s' 的值 '%s' 类型无效，期望类型: %s",
                e.getName(), e.getValue(), e.getRequiredType().getSimpleName());

        log.warn("参数类型不匹配: {}", error);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), error);
    }

    /**
     * 处理运行时异常 - 返回500状态码
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("系统异常 [{} {}]: {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage(),
                e);

        return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统内部错误");
    }

}
