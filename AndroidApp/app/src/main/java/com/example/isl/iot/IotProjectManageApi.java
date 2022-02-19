package com.example.isl.iot;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.AddDeviceRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.AddDeviceResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.QueryDeviceListRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.QueryDeviceListResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.QueryProductListRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.QueryProductListResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.QueryStatisticsRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.QueryStatisticsResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.RemoveDeviceRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.RemoveDeviceResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.enums.From;
import com.github.cm.heclouds.onenet.studio.api.exception.IotClientException;
import com.github.cm.heclouds.onenet.studio.api.exception.IotServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class IotProjectManageApi extends MyIotClient{
    private final String projectId = IotConstant.projectId;

    public IotProjectManageApi() {
        super();
    }

    /**
     * 同步调用项目概况API
     */
    public void testQueryStatistics() {
        QueryStatisticsRequest request = new QueryStatisticsRequest();
        request.setProjectId(projectId);

        try {
            QueryStatisticsResponse response = client.sendRequest(request);
            System.out.println(JSON.toJSONString(response));
        } catch (IotClientException e) {
            e.printStackTrace();
        } catch (IotServerException e) {
            System.err.println(e.getCode());
            e.printStackTrace();
        }
    }

    /**
     * 异步调用项目概况API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testQueryStatisticsAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryStatisticsRequest request = new QueryStatisticsRequest();
        request.setProjectId(projectId);

        client.sendRequestAsync(request).whenComplete((response, cause) -> {
            if (response != null) {
                System.out.println(JSON.toJSONString(response));
            } else {
                if (cause instanceof IotServerException) {
                    IotServerException serverError = (IotServerException) cause;
                    System.err.println(serverError.getCode());
                }
                cause.printStackTrace();
            }
            latch.countDown();
        });
        latch.await();
    }

    /**
     * 同步调用项目集成产品列表API
     */
    public void testQueryProductList() {
        QueryProductListRequest request = new QueryProductListRequest();
        request.setProjectId(projectId);
        request.setOffset(0);
        request.setLimit(10);

        try {
            QueryProductListResponse response = client.sendRequest(request);
            Log.i("TAG", JSON.toJSONString(response));
        } catch (IotClientException e) {
            e.printStackTrace();
        } catch (IotServerException e) {
            System.err.println(e.getCode());
            e.printStackTrace();
        }
    }

    /**
     * 异步调用项目集成产品列表API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testQueryProductListAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);


        QueryProductListRequest request = new QueryProductListRequest();
        request.setProjectId(projectId);
        request.setOffset(0);
        request.setLimit(10);

        client.sendRequestAsync(request).whenComplete((response, cause) -> {
            if (response != null) {
                String jsonStr = JSON.toJSONString(response);
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                if (jsonArray != null) {
                    iotProductInfos.clear(); //清除列表
                    for (int i =0; i < jsonArray.size(); i++) {
                        JSONObject jsonProduct = jsonArray.getJSONObject(i);
                        IotProductInfo productInfo = new IotProductInfo(jsonProduct.getString("product_id"), jsonProduct.getString("name"));
                        iotProductInfos.add(productInfo);
                    }
                }
                System.out.println(jsonStr);
                setProInfosSta(true);
            } else {
                if (cause instanceof IotServerException) {
                    IotServerException serverError = (IotServerException) cause;
                    System.err.println(serverError.getCode());
                }
                cause.printStackTrace();
            }
            latch.countDown();
        });
        latch.await();


    }

    /**
     * 同步调用项目集成设备列表API
     */
    public void testQueryDeviceList() {
        QueryDeviceListRequest request = new QueryDeviceListRequest();
        request.setProjectId(projectId);
//        request.setProductId("EWQ0uNRuUp");
        request.setDeviceName("api-sdk-device-00");
        request.setFrom(From.CREATED_AUTONOMY);
        request.setOffset(0);
        request.setLimit(10);

        try {
            QueryDeviceListResponse response = client.sendRequest(request);
            System.out.println(JSON.toJSONString(response));
        } catch (IotClientException e) {
            e.printStackTrace();
        } catch (IotServerException e) {
            System.err.println(e.getCode());
            e.printStackTrace();
        }
    }

    /**
     * 异步调用项目集成设备列表API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testQueryDeviceListAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDeviceListRequest request = new QueryDeviceListRequest();
        request.setProjectId(projectId);

        client.sendRequestAsync(request).whenComplete((response, cause) -> {
            if (response != null) {
                String jsonStr = JSON.toJSONString(response);
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                if (jsonArray != null) {
                    int devNum = jsonArray.size();
                    for (int i =0; i < jsonArray.size(); i++) {
                        JSONObject jsonDevice = jsonArray.getJSONObject(i);
                        String productId = jsonDevice.getString("product_id");
                        IotDeviceInfo deviceInfo = new IotDeviceInfo(jsonDevice.getString("device_name"), jsonDevice.getString("status"));
                        String createdTimeStr = jsonDevice.getString("created_time");
                        String lastTimeStr = jsonDevice.getString("last_time");
                        String devStatus = jsonDevice.getString("status");
                        int b, f;
                        if (createdTimeStr != null) {
                            b = createdTimeStr.indexOf("T");
                            f = createdTimeStr.indexOf(".");
                            if (b > 0 && f > b) {
                                String date = createdTimeStr.substring(0, b);
                                String time = createdTimeStr.substring(b+1, f+4);
                                deviceInfo.setCreatedTime(date, time);
                            }
                        }
                        if (lastTimeStr != null) {
                            b = lastTimeStr.indexOf("T");
                            f = lastTimeStr.indexOf(".");
                            if (b > 0 && f > b) {
                                String date = lastTimeStr.substring(0, b);
                                String time = lastTimeStr.substring(b+1, f+4);
                                deviceInfo.setLastTime(date, time);
                            }
                        }
                        deviceInfo.setStatus(devStatus);
                        addDevice(productId, deviceInfo);
                    }
                }
                System.out.println(jsonStr);
                setDevInfosSta(true);
            } else {
                if (cause instanceof IotServerException) {
                    IotServerException serverError = (IotServerException) cause;
                    System.err.println(serverError.getCode());
                }
                cause.printStackTrace();
            }
            latch.countDown();
        });
        latch.await();
    }

    /**
     * 同步调用项目添加设备API
     */
    public void testAddDevice() {
        AddDeviceRequest request = new AddDeviceRequest();
        request.setProjectId(projectId);
        request.setProductId("EWQ0uNRuUp");
        request.addDevice("api-sdk-device-011");

        try {
            AddDeviceResponse response = client.sendRequest(request);
            System.out.println(JSON.toJSONString(response));
        } catch (IotClientException e) {
            e.printStackTrace();
        } catch (IotServerException e) {
            System.err.println(e.getCode());
            e.printStackTrace();
        }
    }

    /**
     * 异步调用项目添加设备API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testAddDeviceAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AddDeviceRequest request = new AddDeviceRequest();
        request.setProjectId(projectId);
        request.setProductId("EWQ0uNRuUp");
        request.addDevice("api-sdk-device-011");

        client.sendRequestAsync(request).whenComplete((response, cause) -> {
            if (cause == null) {
                System.out.println(response.getRequestId());
                response.forEach(errorData -> System.out.println(JSON.toJSONString(errorData)));
            } else {
                if (cause instanceof IotServerException) {
                    IotServerException serverError = (IotServerException) cause;
                    System.err.println(serverError.getCode());
                }
                cause.printStackTrace();
            }
            latch.countDown();
        });
        latch.await();
    }

    /**
     * 同步调用项目移除设备API
     */
    public void testRemoveDevice() {
        RemoveDeviceRequest request = new RemoveDeviceRequest();
        request.setProjectId(projectId);
        request.setProductId("EWQ0uNRuUp");
        request.addDevice("api-sdk-device-011");

        try {
            RemoveDeviceResponse response = client.sendRequest(request);
            System.out.println(JSON.toJSONString(response));
        } catch (IotClientException e) {
            e.printStackTrace();
        } catch (IotServerException e) {
            System.err.println(e.getCode());
            e.printStackTrace();
        }
    }

    /**
     * 异步调用项目移除设备API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testRemoveDeviceAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        RemoveDeviceRequest request = new RemoveDeviceRequest();
        request.setProjectId(projectId);
        request.setProductId("EWQ0uNRuUp");
        request.addDevice("api-sdk-device-011");

        client.sendRequestAsync(request).whenComplete((response, cause) -> {
            if (cause == null) {
                System.out.println(response.getRequestId());
                response.forEach(errorData -> System.out.println(JSON.toJSONString(errorData)));
            } else {
                if (cause instanceof IotServerException) {
                    IotServerException serverError = (IotServerException) cause;
                    System.err.println(serverError.getCode());
                }
                cause.printStackTrace();
            }
            latch.countDown();
        });
        latch.await();
    }


    /**
     * 添加一个设备信息到对应的产品下
     * @param productId  产品id
     * @param deviceInfo 设备信息
     * @return true添加成功 false添加失败
     */
    private boolean addDevice(String productId, IotDeviceInfo deviceInfo) {
        for (int i = 0; i < iotProductInfos.size(); i++) {
            if (iotProductInfos.get(i).getId().equals(productId)) { //找到相同id的产品然后添加设备信息
                iotProductInfos.get(i).addDevice(deviceInfo);
                return true;
            }
        }
        return false;
    }
}
