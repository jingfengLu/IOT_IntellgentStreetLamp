package com.example.isl.iot;

import java.util.ArrayList;
import java.util.List;

public class IotProductInfo {
    private List<IotDeviceInfo> deviceList = new ArrayList<IotDeviceInfo>();
    private String productId;
    private String productName;

    public IotProductInfo(String id, String name) {
        productId = id;
        productName = name;
    }

    public IotProductInfo(String id, String name, IotDeviceInfo deviceInfo) {
        productId = id;
        productName = name;
        deviceList.add(deviceInfo);
    }

    public void addDevice(IotDeviceInfo deviceInfo) {
        deviceList.add(deviceInfo);
    }
    public boolean delDevice(int index) {
        if (index < 0 || index >= deviceList.size())
            return false;
        deviceList.remove(index);
        return true;
    }
    public boolean delDevice(String name) {
        int position = -1;
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getName().equals(name)) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            deviceList.remove(position);
            return true;
        }
        return false;
    }

    public void setProductId(String id) {productId = id;}
    public void setProductName(String name) {productName = name;}

    public String getId() {return productId;}
    public String getName() {return productName;}
    public IotDeviceInfo getDeviceInfo(int index) {return deviceList.get(index);}
    public int getDeviceSize() {return deviceList.size();}
    public List<IotDeviceInfo> getDeviceList() {return deviceList;}
    public String getDeviceName(int index) {return deviceList.get(index).getName();}
}
