package com.hut.cwp.mcar.activitys.map.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.map.BNMainActivity;

/**
 * Created by hutcwp on 2017/5/17.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class HRecyclerViewAdapter extends RecyclerView.Adapter<HRecyclerViewAdapter.ViewHolder> {

    private BNMainActivity activity;



//    private String[] titles = {
//            content.getString(R.string.notice_move_car),
//            content.getString(R.string.gas_price),
//            content.getString(R.string.illegal_recod),
//            content.getString(R.string.car_insurance),
//            content.getString(R.string.car_check)
//    };

    private String[] titles = {
            "通知移车","今日油价","违章记录","车辆保险","车辆体检"
    };

    private int[] imgs = {
            R.drawable.cwp_main_item_img_notice,
            R.drawable.cwp_main_item_img_gastag,
            R.drawable.cwp_main_item_img_illegal,
            R.drawable.cwp_main_item_img_insurance,
            R.drawable.cwp_main_item_img_check,
    };

    public HRecyclerViewAdapter(BNMainActivity activity) {

        this.activity = activity;
    }

    @Override
    public HRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_h_listview, parent, false);
        return new HRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.img.setBackgroundResource(imgs[position]);
        holder.title.setText(titles[position]);
        switch(titles[position]){
            case "通知移车":
                holder.img.setOnClickListener(view -> activity.present.moveCar());
                break;
            case "今日油价":
                holder.img.setOnClickListener(view -> activity.present.gasPrice());
                break;
            case "违章记录":
                holder.img.setOnClickListener(view -> activity.present.ivIllegalRecord());
                break;
            case "车辆保险":
                holder.img.setOnClickListener(view -> activity.present.insurance());
                break;
            case "车辆体检":
                holder.img.setOnClickListener(view -> activity.present.checkCar());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }



}
