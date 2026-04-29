package com.modulersx.config;

import com.modulersx.common.log.AppLoggers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    private static final Logger processLog = LoggerFactory.getLogger(AppLoggers.PROCESS);
    private static final String START_TIME_ATTRIBUTE = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        long duration = calculateDuration(request);
        String exceptionName = ex == null ? "-" : ex.getClass().getSimpleName();

        // 记录每次 HTTP 请求的关键过程信息，方便后续排查接口慢、状态码异常等问题。
        processLog.info(
                "request method={} uri={} status={} durationMs={} exception={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration,
                exceptionName);
    }

    private long calculateDuration(HttpServletRequest request) {
        Object startTime = request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime instanceof Long value) {
            return System.currentTimeMillis() - value;
        }
        return -1;
    }
}
