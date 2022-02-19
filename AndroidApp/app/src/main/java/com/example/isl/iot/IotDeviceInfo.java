package com.example.isl.iot;

//设备信息基类，包含设备的基本信息
public class IotDeviceInfo {
    private static boolean autoRefresh = false;  //显示的属性是否自动更新
    public static final String iotDeviceStaInactive = "INACTIVE";  //未激活
    public static final String iotDeviceStaOnline   = "ONLINE";    //在线
    public static final String iotDeviceStaOffline  = "OFFLINE";   //离线
    public static final String iotDeviceStaError = "ERROR"; //错误

    private String name;                    //设备名称
    private String createdTime, lastTime;   //设备创建事件和最后在线时间
    private String status;                  //设备状态 1未激活 2在线 3离线

    public IotDeviceInfo(String name, String status) {
        this.name = name;
        this.status = status;
        createdTime = "2020-01-01T00:00:00.000Z";
        lastTime = "2020-01-01T00:00:00.000Z";
    }

    public void setName(String name) {
        if (!name.isEmpty()) {
            this.name = name;
        }
    }

    public void setStatus(String sta) {
        if (!sta.isEmpty()) {
            status = sta;
        }
    }

    /**
     *
     * @param date 日期 yyyy-mm-dd
     * @param time 时间 hh:mm:ss.ms
     */
    public void setCreatedTime(String date, String time) {
        createdTime = date + " " + time;
    }
    public void setCreatedTime(String calendar) {calendar = calendar;}

    /**
     *
     * @param date 日期 yyyy-mm-dd
     * @param time 时间 hh:mm:ss.ms
     */
    public void setLastTime(String date, String time) {
        lastTime = date + " " + time;
    }
    public void setLastTime(String calendar) {lastTime = calendar;}

    public String getName() { return name; }
    public String getStatus() { return status; }
    public String getCreatedTime() { return createdTime; }
    public String getLastTime() { return lastTime; }

    public static boolean isAutoRefresh() { return autoRefresh; }
    public static void setAutoRefresh(boolean autoRefresh) { IotDeviceInfo.autoRefresh = autoRefresh; }
}
