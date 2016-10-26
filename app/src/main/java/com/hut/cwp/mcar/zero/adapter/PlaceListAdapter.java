package com.hut.cwp.mcar.zero.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.zero.bean.CityBean;
import com.hut.cwp.mcar.zero.bean.ProvinceBean;

import java.util.List;

/**
 * Created by Just on 2016/10/14.
 */

public class PlaceListAdapter<T> extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder>{
    private List<T> mData;
    private int mPlaceGrade;//1-选择的省，2-选择的市
    private Context mContext;

    public static int GRADE_PROVINCE=1;
    public static int GRADE_CITY=2;

    private ItemClickListener mItemClickListener;

    public void setItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int pos);
    }

    public PlaceListAdapter(List<T> data, int placeGrade, Context context) {
        super();
        mData=data;
        mPlaceGrade=placeGrade;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.z_item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String str="";
        switch (mPlaceGrade) {
            case 1:
                str=((ProvinceBean)(mData.get(position))).getProvince();
                break;
            case 2:
                str=((CityBean)(mData.get(position))).getCity_name();
                break;
            default:
                Log.d("Test","The member variable-mPlaceGrade of PlaceListAdapter is error!");
        }
        holder.tv.setText(str);
        Log.d("测试","+++++++"+str);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tv;

        public ViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_illegal_query_item_city);
        }
    }
}
