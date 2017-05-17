package com.hut.cwp.mcar.activitys.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.business.bean.CarInfoBean;

import java.util.List;

/**
 * Created by hutcwp on 2017/5/14.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class CarInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<CarInfoBean> datas;

    public CarInfoAdapter(Context mContext, List<CarInfoBean> datas) {

        this.mContext = mContext;
        this.datas = datas;

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.z_item_car_info, parent, false);
            holder.tvLsnum = (TextView) convertView.findViewById(R.id.tv_il_qy_car_info_lsnum);
            holder.tvEngineno = (TextView) convertView.findViewById(R.id.tv_il_qy_car_info_engineno);
            holder.tvClassno = (TextView) convertView.findViewById(R.id.tv_il_qy_car_info_classno);
            holder.tvLsnum.setText("");
            holder.tvEngineno.setText("");
            holder.tvClassno.setText("");
            holder.rbSelect = (Button) convertView.findViewById(R.id.rb_il_qy_car_info_radio_box);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CarInfoBean data = datas.get(position);
        holder.tvLsnum.setText(data.getChepaino());
        holder.tvClassno.setText(data.getEngineno());
        holder.tvEngineno.setText(data.getChejiano());
        if (data.isChecked()) {
            holder.rbSelect.setBackgroundResource(R.drawable.z_il_qy_radio_box_selected);
        }else
        {
            holder.rbSelect.setBackgroundResource(R.drawable.z_il_qy_radio_box_unselect);
        }
        return convertView;
    }


    /**
     * 更新选择状态
     */
    public void update(){
        notifyDataSetChanged();
    }

    /**
     * ViewHolder
     */
    private class ViewHolder {
        TextView tvLsnum, tvEngineno, tvClassno;
        Button rbSelect;

    }


}
