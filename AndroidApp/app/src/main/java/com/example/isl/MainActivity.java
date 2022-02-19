package com.example.isl;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.example.isl.iot.IotProjectManageApi;
import com.example.isl.iot.MyIotClient;
import com.example.isl.iotList.IotListAdapter;
import com.example.isl.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.isl.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private IotProjectManageApi iotProjectManageApi = new IotProjectManageApi();

    public IotListAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new IotListAdapter(MyIotClient.getIotProductInfos(),
                MainActivity.this);
        requestPermissions(new String[]{"android.permission.INTERNET"}, 1);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        MyIotClient.createClient();

        if (!MyIotClient.getProInfosSta()) { //未初始化产品列表
            try {
                iotProjectManageApi.testQueryProductListAsync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!MyIotClient.getDevInfosSta()) {
            try {
                iotProjectManageApi.testQueryDeviceListAsync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建右上角菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                updateIotProductInfos();
                adapter.notifyDataSetChanged();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public IotProjectManageApi getIotProjectManage() {return iotProjectManageApi;}


    private void updateIotProductInfos() {
        try {
            iotProjectManageApi.testQueryProductListAsync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            iotProjectManageApi.testQueryDeviceListAsync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}