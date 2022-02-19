package com.example.isl.iot;

import com.github.cm.heclouds.onenet.studio.api.IotClient;
import com.github.cm.heclouds.onenet.studio.api.IotProfile;
import com.github.cm.heclouds.onenet.studio.api.auth.SignatureMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyIotClient {

    static List<IotProductInfo> iotProductInfos = new ArrayList<IotProductInfo>(); //共同使用一个公共的设备列表
    static IotClient client;
    static private boolean initSta = false;
    static private boolean proInfosSta = false;
    static private boolean devInfosSta = false;

    static public void createClient() {
        if (!initSta) {
            IotProfile profile = new IotProfile();

            profile.userId(IotConstant.userId).accessKey(IotConstant.accessKey).signatureMethod(SignatureMethod.SHA256);
            client = IotClient.create(profile);
            initSta = true;
        }
    }

    /**
     * 关闭{@link IotClient}并释放资源
     * @throws IOException 如果有IO 错误
     */
    public void destroyClient() throws IOException {
        client.close();
    }


    static public boolean getInitSta() {return initSta;}
    static public boolean getProInfosSta() {return proInfosSta;}
    static public boolean getDevInfosSta() {return devInfosSta;}
    static public void setProInfosSta(boolean sta) {proInfosSta = sta;}
    static public void setDevInfosSta(boolean sta) {devInfosSta = sta;}
    static public IotProductInfo getProductInfo(int index) {return iotProductInfos.get(index);}
    static public String getProductName(int index) {return iotProductInfos.get(index).getName();}
    static public String getProductId(int index) {return iotProductInfos.get(index).getId();}
    static public IotDeviceInfo getDeviceInfo(int proIndex, int devIndex) {return iotProductInfos.get(proIndex).getDeviceInfo(devIndex);}
    static public int getProductCount() {return iotProductInfos.size();}
    static public int getDeviceCount(int productIdx) {return iotProductInfos.get(productIdx).getDeviceSize();}
    static public List<IotProductInfo> getIotProductInfos() {return iotProductInfos;}
    static public void clearProductIndos() {
        for (int i = 0; i < iotProductInfos.size(); i++) {
            for (int j = 0; j < iotProductInfos.get(i).getDeviceSize(); j++) {
                iotProductInfos.get(i).delDevice(j);
            }
            iotProductInfos.remove(i);
        }
    }
}
