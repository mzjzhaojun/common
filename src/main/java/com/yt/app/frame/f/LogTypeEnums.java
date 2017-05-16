package com.yt.app.frame.f;

public enum LogTypeEnums {

    LOG_TYPE_EXCEPTION(0),
    LOG_TYPE_OPERATION(1);

    private Integer logType;

    LogTypeEnums(Integer logType) {
        this.logType = logType;
    }

    public Integer getValue() {
        return logType;
    }
}
