package com.example.isl.iot;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.*;
import com.github.cm.heclouds.onenet.studio.api.entity.common.QueryDeviceDetailResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.enums.EventType;
import com.github.cm.heclouds.onenet.studio.api.enums.Sort;
import com.github.cm.heclouds.onenet.studio.api.exception.IotClientException;
import com.github.cm.heclouds.onenet.studio.api.exception.IotServerException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class IotDeviceApi extends MyIotClient {
    private final String projectId = IotConstant.projectId;
    final private String productId, deviceName;
    IotDeviceInfo deviceInfo;

    public IotDeviceApi(String productId, IotDeviceInfo deviceInfo) {
        this.productId = productId;
        this.deviceInfo = deviceInfo;
        deviceName = deviceInfo.getName();
    }

    /**
     * 异步调用设备详情API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testQueryDeviceDetailAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDeviceDetailRequest request = new QueryDeviceDetailRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);

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
     * 异步调用设备状态查询API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testQueryDeviceStatusAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDeviceStatusRequest request = new QueryDeviceStatusRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);

        client.sendRequestAsync(request).whenComplete((response, cause) -> {
            if (response != null) {
//                System.out.println(JSON.toJSONString(response));
                deviceStatusParse(JSON.parseObject(JSON.toJSONString(response)));
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
     * 异步调用设备状态历史数据查询API
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void testQueryDeviceStatusHistoryAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDeviceStatusHistoryRequest request = new QueryDeviceStatusHistoryRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        LocalDateTime tenDaysBefore = LocalDateTime.now().minus(Duration.ofDays(10));
        LocalDateTime now = LocalDateTime.now();
        request.setStartTime(Date.from(tenDaysBefore.toInstant(ZoneOffset.of("+8"))));
        request.setEndTime(Date.from(now.toInstant(ZoneOffset.of("+8"))));

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
     * 异步调用设备属性设置API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testSetDevicePropertyAsync(String param, Object value) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        SetDevicePropertyRequest request = new SetDevicePropertyRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        request.addParam(param, value);

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
     * 异步调用设备属性期望设置API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testSetDeviceDesiredPropertyResponseAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        SetDeviceDesiredPropertyRequest request = new SetDeviceDesiredPropertyRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        request.addParam("api-sdk-develop-function-001", 13);

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
     * 异步调用设备属性期望查询API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testQueryDeviceDesiredPropertyAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDeviceDesiredPropertyRequest request = new QueryDeviceDesiredPropertyRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        request.addParam("api-sdk-develop-function-001");

        client.sendRequestAsync(request).whenComplete((response, cause) -> {
            if (response != null) {
                System.out.println("requestId:" + response.getRequestId());
                response.forEach((identify, property) -> {
                    System.out.println("identify:" + identify);
                    System.out.println("property:" + JSON.toJSONString(property));
                });
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
     * 异步调用设备属性期望删除API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testDeleteDeviceDesiredPropertyAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        DeleteDeviceDesiredPropertyRequest request = new DeleteDeviceDesiredPropertyRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        request.addParam("api-sdk-develop-function-001");

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
     * 异步调用设备操作日志查询API
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void testQueryDeviceLogAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDeviceLogRequest request = new QueryDeviceLogRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        LocalDateTime tenDaysBefore = LocalDateTime.now().minus(Duration.ofDays(1));
        LocalDateTime now = LocalDateTime.now();
        request.setStartTime(Date.from(tenDaysBefore.toInstant(ZoneOffset.of("+8"))));
        request.setEndTime(Date.from(now.toInstant(ZoneOffset.of("+8"))));
        request.setOffset(0);
        request.setLimit(10);

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
     * 异步调用设备属性最新数据查询API
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testQueryDevicePropertyAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDevicePropertyRequest request = new QueryDevicePropertyRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);

        client.sendRequestAsync(request).whenComplete((response, cause) -> {
            if (response != null) {
                System.out.println("requestId:" + response.getRequestId());
                response.forEach(deviceProperty -> System.out.println(JSON.toJSONString(deviceProperty)));
                if (deviceInfo instanceof IotIslDeviceInfo)
                    response.forEach(deviceProperty -> islDevicePropertyParse(JSON.parseObject(JSON.toJSONString(deviceProperty))));
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
     * 异步调用设备属性历史数据查询API
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void testQueryDevicePropertyHistoryAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDevicePropertyHistoryRequest request = new QueryDevicePropertyHistoryRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        request.setIdentifier("api-sdk-develop-function-001");
        LocalDateTime tenDaysBefore = LocalDateTime.now().minus(Duration.ofDays(1));
        LocalDateTime now = LocalDateTime.now();
        request.setStartTime(Date.from(tenDaysBefore.toInstant(ZoneOffset.of("+8"))));
        request.setEndTime(Date.from(now.toInstant(ZoneOffset.of("+8"))));
        request.setOffset(0);
        request.setLimit(10);
        request.setSort(Sort.DESC);

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
     * 异步调用设备事件历史数据查询API
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void testQueryDeviceEventHistoryAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        QueryDeviceEventHistoryRequest request = new QueryDeviceEventHistoryRequest();
        request.setProjectId(projectId);
        request.setProductId(productId);
        request.setDeviceName(deviceName);
        request.setIdentifier("api-sdk-develop-event-001");
        request.setEventType(EventType.MESSAGE);
        LocalDateTime tenDaysBefore = LocalDateTime.now().minus(Duration.ofDays(1));
        LocalDateTime now = LocalDateTime.now();
        request.setStartTime(Date.from(tenDaysBefore.toInstant(ZoneOffset.of("+8"))));
        request.setEndTime(Date.from(now.toInstant(ZoneOffset.of("+8"))));
        request.setOffset(0);
        request.setLimit(10);

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
     * 解析json数据获取设备状态
     * @param jsonObject json数据
     */
    private void deviceStatusParse(@NonNull JSONObject jsonObject) {
        String status = jsonObject.getString("status");
        if (status != null && !status.isEmpty()) {
            if (status.equals(IotDeviceInfo.iotDeviceStaInactive)) {
                deviceInfo.setStatus(IotDeviceInfo.iotDeviceStaInactive);
            } else if (status.equals(IotDeviceInfo.iotDeviceStaOffline)) {
                deviceInfo.setStatus(IotDeviceInfo.iotDeviceStaOffline);
            } else if (status.equals(IotDeviceInfo.iotDeviceStaOnline)) {
                deviceInfo.setStatus(IotDeviceInfo.iotDeviceStaOnline);
            } else {
                deviceInfo.setStatus(IotDeviceInfo.iotDeviceStaError);
            }
        }
    }

    /**
     * 解析json数据获得最新的属性数据
     * @param jsonObject json数据
     */
    private void islDevicePropertyParse(JSONObject jsonObject) {
        IotIslDeviceInfo iotIslDeviceInfo = (IotIslDeviceInfo) deviceInfo;
        String identifier = jsonObject.getString("identifier");
        String value = jsonObject.getString("value");
        long lastTime = jsonObject.getLong("time");
        if (identifier != null && !identifier.isEmpty() && !value.isEmpty()) {
            IotDeviceProperty property = iotIslDeviceInfo.iotIslProMap.get(identifier);
            if (property != null) {
                property.setData(value);
                if (lastTime > property.getLastTime())
                    property.setLastTime(lastTime);
            }
        }
    }
}
