package com.modulersx.exception;

import com.modulersx.common.log.AppLoggers;
import com.modulersx.common.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger businessErrorLog = LogManager.getLogger(AppLoggers.BUSINESS_ERROR);
    private static final Logger systemErrorLog = LogManager.getLogger(AppLoggers.SYSTEM_ERROR);

    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBizException(BizException ex) {
        // 主动抛出的业务异常单独进入 businessErr.log，避免和系统异常混在一起。
        businessErrorLog.warn("Business exception code={} message={}", ex.getCode(), ex.getMessage());
        return ApiResponse.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "invalid request";
        businessErrorLog.warn("Request validation failed message={}", message);
        return ApiResponse.fail(400, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException ex) {
        businessErrorLog.warn("Request validation failed message={}", ex.getMessage());
        return ApiResponse.fail(400, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ApiResponse<Void> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        businessErrorLog.warn("Upload request missing file part");
        return ApiResponse.fail(400, "file cannot be empty");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResponse<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        businessErrorLog.warn("Upload file size exceeded");
        return ApiResponse.fail(400, "file size cannot exceed 5MB");
    }

    @ExceptionHandler(MultipartException.class)
    public ApiResponse<Void> handleMultipartException(MultipartException ex) {
        businessErrorLog.warn("Invalid multipart request message={}", ex.getMessage());
        return ApiResponse.fail(400, "file cannot be empty");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        businessErrorLog.warn("Request body is not readable message={}", ex.getMessage());
        return ApiResponse.fail(400, "invalid request body");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        businessErrorLog.warn("Request method not supported method={}", ex.getMethod());
        return ApiResponse.fail(405, "request method not supported");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<Void> handleNoResourceFoundException(NoResourceFoundException ex) {
        businessErrorLog.warn("Resource not found path={}", ex.getResourcePath());
        return ApiResponse.fail(404, "resource not found");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex) {
        // 未预期异常进入 error.log，并保留完整堆栈，优先用于排查程序 bug 或环境问题。
        systemErrorLog.error("Unhandled exception", ex);
        return ApiResponse.fail(500, "server error");
    }
}
