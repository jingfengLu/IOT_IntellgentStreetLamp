package com.example.isl.iotList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isl.MainActivity;
import com.example.isl.R;
import com.example.isl.iot.IotDeviceInfo;
import com.example.isl.iot.IotProductInfo;
import com.example.isl.iot.MyIotClient;

import org.w3c.dom.Text;

import java.util.List;


public class IotListAdapter extends BaseExpandableListAdapter {
    private List<IotProductInfo> iotProductInfos;
    private Context context;

    public IotListAdapter(List<IotProductInfo> iotProductInfos, Context context) {
        super();
        this.iotProductInfos = iotProductInfos;
        this.context = context;
    }

    /**
     * 获取分组的个数
     * @return 分组个数
     */
    @Override
    public int getGroupCount() {
        return iotProductInfos.size();
    }

    /**
     * 获取子选项个数
     * @param groupPosition 分组位置
     * @return 子选项个数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return iotProductInfos.get(groupPosition).getDeviceSize();
    }

    /**
     * 获取分组数据
     * @param groupPosition 分组位置
     * @return 分组数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return iotProductInfos.get(groupPosition);
    }

    /**
     * 获取分组中指定的子选项数据
     * @param groupPosition 分组位置
     * @param childPosition 子选项位置
     * @return 子选项数据
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return iotProductInfos.get(groupPosition).getDeviceInfo(childPosition);
    }

    /**
     * 获取指定分组的ID, 这个ID必须是唯一的
     * @param groupPosition 分组位置
     * @return 分组ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //

    /**
     * 获取子选项的ID, 这个ID必须是唯一的
     * @param groupPosition 分组位置
     * @param childPosition 子选项位置
     * @return 子选项id
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定分组的视图
     */
    @SuppressLint("DefaultLocale")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.textProductName = (TextView) convertView.findViewById(R.id.text_proName);
            groupViewHolder.textProductId = (TextView) convertView.findViewById(R.id.text_proId);
            groupViewHolder.textDeviceCount = (TextView) convertView.findViewById(R.id.text_devCount);
            convertView.setTag(groupViewHolder); //保存groupViewHolder
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag(); //将保存的groupViewHolder取出来
        }
        groupViewHolder.textProductName.setText(iotProductInfos.get(groupPosition).getName());
        groupViewHolder.textProductId.setText(String.format("id:%s", iotProductInfos.get(groupPosition).getId()));
        groupViewHolder.textDeviceCount.setText(String.format("设备数量%d台", iotProductInfos.get(groupPosition).getDeviceSize()));
        return convertView;
    }

    //        获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.textDevName = (TextView) convertView.findViewById(R.id.text_devName);
            childViewHolder.textDevSta = (TextView) convertView.findViewById(R.id.text_devSta);
            childViewHolder.imageDevSta = (ImageView) convertView.findViewById(R.id.image_devSta);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.textDevName.setText(iotProductInfos.get(groupPosition).getDeviceName(childPosition));
        switch (iotProductInfos.get(groupPosition).getDeviceInfo(childPosition).getStatus()) {
            case IotDeviceInfo.iotDeviceStaInactive:
                childViewHolder.textDevSta.setText("未激活");
                childViewHolder.imageDevSta.setImageResource(R.drawable.ic_inactive);
                break;
            case IotDeviceInfo.iotDeviceStaOffline:
                childViewHolder.textDevSta.setText("不在线");
                childViewHolder.imageDevSta.setImageResource(R.drawable.ic_offline);
                break;
            case IotDeviceInfo.iotDeviceStaOnline:
                childViewHolder.textDevSta.setText("在线");
                childViewHolder.imageDevSta.setImageResource(R.drawable.ic_online);
                break;
            case IotDeviceInfo.iotDeviceStaError:
                childViewHolder.textDevSta.setText("错误");
                childViewHolder.imageDevSta.setImageResource(R.drawable.ic_offline);
                break;
        }
        return convertView;
    }

    //        指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView textProductName, textProductId, textDeviceCount;
    }
    static class ChildViewHolder {
        TextView textDevName, textDevSta;
        ImageView imageDevSta;
    }
}
