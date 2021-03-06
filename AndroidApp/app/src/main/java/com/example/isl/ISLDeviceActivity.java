package com.example.isl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isl.databinding.ActivityIsldeviceBinding;
import com.example.isl.iot.IotDeviceApi;
import com.example.isl.iot.IotDeviceInfo;
import com.example.isl.iot.IotIslDeviceInfo;
import com.example.isl.iot.IotProductInfo;
import com.example.isl.iot.MyIotClient;

import java.sql.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ISLDeviceActivity extends AppCompatActivity {
    ActivityIsldeviceBinding binding;
    private TextView tvDevGroup, tvHumiPm, tvDevSta, tvDevLastTime, tvDevLightSta;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private ImageView imageDevSta;
    private Button btnTurnOn, btnTurnOff;
    IotDeviceApi iotDeviceApi;
    IotProductInfo productInfo;
    IotDeviceInfo deviceInfo;
    IotIslDeviceInfo iotIslDeviceInfo;

    private Handler mRepeatHandler;
    private Runnable mRepeatRunnable;
    private final static int UPDATE_INTERVAL = 5000;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isldevice);

        int groupPosition, childPosition;
        groupPosition = getIntent().getIntExtra("groupPosition", 0);
        childPosition = getIntent().getIntExtra("childPosition", 0);
        productInfo = MyIotClient.getProductInfo(groupPosition);
        deviceInfo = MyIotClient.getDeviceInfo(groupPosition, childPosition);
        iotIslDeviceInfo = new IotIslDeviceInfo(deviceInfo);
        iotDeviceApi = new IotDeviceApi(productInfo.getId(), iotIslDeviceInfo);

        binding = ActivityIsldeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvDevGroup = binding.tvDeviceGroup;
        tvHumiPm = binding.tvDeviceHumiPm;
        tvDevSta = binding.tvDeviceStatus;
        tvDevLastTime = binding.tvDeviceLastTime;
        imageDevSta = binding.ivDeviceStatus;
        tvDevLightSta = binding.tvDeviceLightSta;
        btnTurnOn = binding.btnTurnOn;
        btnTurnOff = binding.btnTurnOff;

        tvDevGroup.setText(productInfo.getName() + "->" + deviceInfo.getName());

        refreshInfo(); //????????????????????????

        btnTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????????????????
                try {
                    iotDeviceApi.testSetDevicePropertyAsync("lightSta", false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btnTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????????????????
                try {
                    iotDeviceApi.testSetDevicePropertyAsync("lightSta", true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mRepeatHandler = new Handler();
        mRepeatRunnable = new Runnable() {
            @Override
            public void run() {
                //Do something awesome
                if (IotDeviceInfo.isAutoRefresh())
                    refreshInfo(); //????????????????????????
                mRepeatHandler.postDelayed(mRepeatRunnable, UPDATE_INTERVAL);
            };
        };
        mRepeatHandler.postDelayed(mRepeatRunnable, UPDATE_INTERVAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mRepeatHandler.removeCallbacks(mRepeatRunnable); //???activity?????????????????????
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_auto_refresh:
                if (item.isChecked()) {
                    item.setChecked(false);
                    IotDeviceInfo.setAutoRefresh(false);
                } else {
                    item.setChecked(true);
                    IotDeviceInfo.setAutoRefresh(true);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * ?????????????????????
     * @param menu ??????
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_device_menu, menu);
        menu.findItem(R.id.action_auto_refresh).setChecked(IotDeviceInfo.isAutoRefresh());
        return true;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void refreshInfo() {
        try {
            iotDeviceApi.testQueryDeviceStatusAsync(); //??????????????????
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tvDevSta.setText(deviceInfo.getStatus());
        switch (deviceInfo.getStatus()) {
            case IotDeviceInfo.iotDeviceStaInactive:
                tvDevSta.setText("?????????");
                imageDevSta.setImageResource(R.drawable.ic_inactive);
                break;
            case IotDeviceInfo.iotDeviceStaOffline:
                tvDevSta.setText("?????????");
                imageDevSta.setImageResource(R.drawable.ic_offline);
                break;
            case IotDeviceInfo.iotDeviceStaOnline:
                tvDevSta.setText("??????");
                imageDevSta.setImageResource(R.drawable.ic_online);
                break;
            case IotDeviceInfo.iotDeviceStaError:
                tvDevSta.setText("??????");
                imageDevSta.setImageResource(R.drawable.ic_offline);
                break;
        }

        try {
            iotDeviceApi.testQueryDevicePropertyAsync(); //?????????????????????
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tvHumiPm.setText("?????????" + iotIslDeviceInfo.getTemp() + "???  " +
                "?????????" + iotIslDeviceInfo.getHumidity() + "%   " +
                "pm2.5:" + iotIslDeviceInfo.getPm25() + "ug/m3");
        if (Boolean.parseBoolean(iotIslDeviceInfo.getLightSta())) { //???????????????
            tvDevLightSta.setText("???????????????");
        } else {
            tvDevLightSta.setText("???????????????");
        }
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).
                format(new Date(Objects.requireNonNull(iotIslDeviceInfo.iotIslProMap.get("humidity")).getLastTime()));
        tvDevLastTime.setText("???????????????" + date);
    }
}