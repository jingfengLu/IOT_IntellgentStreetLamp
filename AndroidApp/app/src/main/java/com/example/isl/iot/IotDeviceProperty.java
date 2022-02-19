package com.example.isl.iot;

import java.lang.reflect.Type;

public class IotDeviceProperty {
    enum DataType {
        Int32, Int64,
        Float, Double,
        Enum, Bool, Str, Struct, Bitmap, Date, Array
    }
    private String data;
    private final String identifier;
    private final DataType dataType;
    private final boolean readOnly;
    //2020-01-01 0:0:0.000  unix的ms时间戳
    private long lastTime = 1577808000000L;

    public IotDeviceProperty(String identifier, DataType dataType, boolean readOnly) {
        this.identifier = identifier;
        this.dataType = dataType;
        this.readOnly = readOnly;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getData() {
        return data;
    }

    public DataType getDataType() {
        return dataType;
    }

    public long getLastTime() {
        return lastTime;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
