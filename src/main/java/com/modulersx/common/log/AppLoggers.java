package com.modulersx.common.log;

public final class AppLoggers {

    // 这里统一定义日志路由名称，业务代码只需要选择日志类型，不需要关心具体写入哪个文件。
    public static final String BUSINESS_ERROR = "BUSINESS_ERROR";
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    public static final String PROCESS = "PROCESS";

    private AppLoggers() {
    }
}
