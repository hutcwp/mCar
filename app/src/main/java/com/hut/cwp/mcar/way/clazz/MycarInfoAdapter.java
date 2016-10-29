package com.hut.cwp.mcar.way.clazz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.way.activitys.InfoCarActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MyCarInfoAdapter extends ArrayAdapter<CarInfo> {

    private int resourceId;

    private Context mContext;

    private List<CarInfo> mData = new ArrayList<>();

    private InfoCarActivity infoCarActivity;

    public MyCarInfoAdapter(Context context, int textViewResourceId, List<CarInfo> objects) {
        super(context, textViewResourceId, objects);

        this.infoCarActivity = (InfoCarActivity) context;
        this.mContext = context;
        this.resourceId = textViewResourceId;
        this.mData = objects;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView licensePlateInfo = (TextView) view.findViewById(R.id.way_itme_licensePlate);
        TextView engineInfo = (TextView) view.findViewById(R.id.way_itme_engine);
        TextView frameInfo = (TextView) view.findViewById(R.id.way_itme_frame);

        if (resourceId == R.layout.way_item_mycar_deleted) {

            Button btn_delete = (Button) view.findViewById(R.id.way_btn_delete);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), "点击了删除" + position + "@@" + mData.get(position).getObjectId(), Toast.LENGTH_LONG).show();

                    String content = "车牌号为 : " + mData.get(position).getLicensePlate();
                    Dialog dialog = new AlertDialog.Builder(mContext)
                            .setIcon(R.mipmap.logo)
                            .setTitle("是否删除该车？")//设置标题
                            .setMessage(content)//设置提示内容
                            //确定按钮
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deletedCarInfo(mData.get(position));
                                }
                            })
                            //取消按钮
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create();//创建对话框
                    dialog.show();//显示对话框

                }
            });
        }

        licensePlateInfo.setText(mData.get(position).getLicensePlate().toString());
        engineInfo.setText(mData.get(position).getEngine().toString());
        frameInfo.setText(mData.get(position).getVin().toString());

        return view;
    }

    private void deletedCarInfo(CarInfo carInfo) {

        carInfo.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                    infoCarActivity.refreshData();
                } else {

                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}