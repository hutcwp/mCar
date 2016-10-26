package com.hut.cwp.mcar.zero.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.zero.bean.IllegalResultBean;

import java.util.List;

/**
 * Created by Just on 2016/10/15.
 */

public class IllegalResultAdapter extends RecyclerView.Adapter<IllegalResultAdapter.ViewHolder> {
    private List<IllegalResultBean> mData;

    public IllegalResultAdapter(List<IllegalResultBean> data) {
        mData=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.z_item_illegal_result, parent, false);
        return new IllegalResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IllegalResultBean temp=mData.get(position);
        holder.tvDate.setText(temp.getDate());
        holder.tvArea.setText(temp.getArea());
        holder.tvAct.setText(temp.getAct());
        holder.tvCode.setText(TextUtils.isEmpty(temp.getCode())?"无":temp.getCode());
        holder.tvFen.setText(TextUtils.isEmpty(temp.getFen())?"无":temp.getFen());
        holder.tvMoney.setText(TextUtils.isEmpty(temp.getMoney())?"无":temp.getMoney());
        String handled=temp.getHandled();
        if (TextUtils.isEmpty(temp.getHandled())) {
            holder.tvHandled.setText("未知");
        } else {
            holder.tvHandled.setText(handled.equals("1")?"已处理":"未处理");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvDate,tvArea,tvAct,tvCode,tvFen,tvMoney,tvHandled;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate= (TextView) itemView.findViewById(R.id.tv_illegal_result_date);
            tvArea= (TextView) itemView.findViewById(R.id.tv_illegal_result_area);
            tvAct= (TextView) itemView.findViewById(R.id.tv_illegal_result_act);
            tvCode= (TextView) itemView.findViewById(R.id.tv_illegal_result_code);
            tvFen= (TextView) itemView.findViewById(R.id.tv_illegal_result_fen);
            tvMoney= (TextView) itemView.findViewById(R.id.tv_illegal_result_money);
            tvHandled= (TextView) itemView.findViewById(R.id.tv_illegal_result_handled);
        }
    }
}
