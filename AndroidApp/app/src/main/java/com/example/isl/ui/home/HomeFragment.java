package com.example.isl.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.isl.ISLDeviceActivity;
import com.example.isl.MainActivity;
import com.example.isl.databinding.FragmentHomeBinding;
import com.example.isl.iot.IotDeviceInfo;
import com.example.isl.iot.IotProductInfo;
import com.example.isl.iot.MyIotClient;
import com.example.isl.iotList.IotListAdapter;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private IotListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final ExpandableListView expandableListView = binding.expandList;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        adapter = ((MainActivity)getActivity()).adapter;

        expandableListView.setAdapter(adapter);
        //展开某个分组时，并关闭其他分组。注意这里设置的是 ExpandListener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //遍历 group 的数组（或集合），判断当前被点击的位置与哪个组索引一致，不一致就合并起来。
                int len = adapter.getGroupCount();
                for (int i = 0; i < len; i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i); //收起某个指定的组
                    }
                }
            }
        });

        //点击某个分组时，跳转到指定Activity
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (expandableListView.isGroupExpanded(groupPosition))
                    expandableListView.collapseGroup(groupPosition);
                else
                    expandableListView.expandGroup(groupPosition);
                return true;    //拦截点击事件，不再处理展开或者收起
            }
        });

        //某个分组中的子View被点击时的事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                if (!MyIotClient.getIotProductInfos().get(groupPosition).getDeviceInfo(childPosition).
                        getStatus().equals(IotDeviceInfo.iotDeviceStaInactive)) { //未激活的设备不支持查看详情
                    Intent intent = new Intent((MainActivity)getActivity(), ISLDeviceActivity.class);
                    intent.putExtra("groupPosition", groupPosition);
                    intent.putExtra("childPosition", childPosition);
                    startActivity(intent);
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}