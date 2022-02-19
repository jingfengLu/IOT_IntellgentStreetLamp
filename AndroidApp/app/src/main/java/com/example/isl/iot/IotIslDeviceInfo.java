package com.example.isl.iot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IotIslDeviceInfo extends IotDeviceInfo{
    public  Map<String, IotDeviceProperty> iotIslProMap = new HashMap<>();

    public IotIslDeviceInfo() {
        super("", "");
        mapInit();
    }

    public IotIslDeviceInfo(IotDeviceInfo deviceInfo) {
        super(deviceInfo.getName(), deviceInfo.getStatus());
        setLastTime(deviceInfo.getLastTime());
        setCreatedTime(deviceInfo.getCreatedTime());
        mapInit();
    }

    public String getRuntime() { return Objects.requireNonNull(iotIslProMap.get("runtime")).getData(); }
    public String getPm25() { return Objects.requireNonNull(iotIslProMap.get("pm25")).getData(); }
    public String getHumidity() { return Objects.requireNonNull(iotIslProMap.get("humidity")).getData(); }
    public String getTemp() { return Objects.requireNonNull(iotIslProMap.get("temperature")).getData(); }
    public String getLightSta() { return Objects.requireNonNull(iotIslProMap.get("lightSta")).getData(); }

    private void mapInit() {
        IotDeviceProperty runTime = new IotDeviceProperty("runtime",
            IotDeviceProperty.DataType.Int32, true);
        iotIslProMap.put(runTime.getIdentifier(), runTime);
        IotDeviceProperty pm25 = new IotDeviceProperty("pm25",
            IotDeviceProperty.DataType.Int32, true);
        iotIslProMap.put(pm25.getIdentifier(), pm25);
        IotDeviceProperty humidity = new IotDeviceProperty("humidity",
            IotDeviceProperty.DataType.Float, true);
        iotIslProMap.put(humidity.getIdentifier(), humidity);
        IotDeviceProperty temp = new IotDeviceProperty("temperature",
            IotDeviceProperty.DataType.Float, true);
        iotIslProMap.put(temp.getIdentifier(), temp);
        IotDeviceProperty lightSta = new IotDeviceProperty("lightSta",
            IotDeviceProperty.DataType.Bool, false);
        iotIslProMap.put(lightSta.getIdentifier(), lightSta);
    }
}
