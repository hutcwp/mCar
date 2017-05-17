package com.hut.cwp.mcar.activitys.info.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.method.ReplacementTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.info.CarInfoActivity;
import com.hut.cwp.mcar.activitys.info.bean.CarInfo;

import java.util.ArrayList;
import java.util.List;

public class MyCarInfoAdapter extends ArrayAdapter<CarInfo> {

    private int resourceId;
    private Context mContext;
    private List<CarInfo> mData = new ArrayList<>();
    private CarInfoActivity carInfoActivity;
    private View view = null;
    private boolean isShowingDialog = false;

    public MyCarInfoAdapter(Context context, int textViewResourceId, List<CarInfo> datas) {
        super(context, textViewResourceId, datas);
        this.carInfoActivity = (CarInfoActivity) context;
        this.mContext = context;
        this.resourceId = textViewResourceId;
        this.mData = datas;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        initView(position);
        return view;
    }

    /**
     * 初始化删除方法
     *
     * @param position
     */
    private void initView(final int position) {

        view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView licensePlateInfo = (TextView) view.findViewById(R.id.way_itme_licensePlate);
        TextView engineInfo = (TextView) view.findViewById(R.id.way_itme_engine);
        TextView frameInfo = (TextView) view.findViewById(R.id.way_itme_frame);

        licensePlateInfo.setText(mData.get(position).getLicensePlate().trim());
        engineInfo.setText(mData.get(position).getEngine().trim());
        frameInfo.setText(mData.get(position).getVin().trim());

        if (resourceId == R.layout.way_item_mycar_deleted) {
            /**
             * 如果是可删除状态
             *  初始化删除Button
             *  添加点击事件
             **/

            Button btn_delete = (Button) view.findViewById(R.id.way_btn_delete);

            btn_delete.setOnClickListener(v -> {
                if (!isShowingDialog) {
                    String content = "车牌号为 : " + mData.get(position).getLicensePlate();
                    Dialog dialog = new AlertDialog.Builder(mContext)
                            .setIcon(R.mipmap.logo)
                            .setTitle("是否删除该车？")//设置标题
                            .setMessage(content)//设置提示内容
                            //确定按钮
                            .setPositiveButton("确定", (dialog1, which) -> {
                                carInfoActivity.present.deleteCar(mData.get(position));
                                isShowingDialog = false;
                            })
                            //取消按钮
                            .setNegativeButton("取消", (dialog12, which) -> {
                                isShowingDialog = false;
                            })
                            .create();//创建对话框
                    dialog.setCanceledOnTouchOutside(false);
                    isShowingDialog = true;
                    dialog.show();//显示对话框
                }
            });
        }

    }


    /**
     * 自定义键盘用到的方法
     */

    private class AllCapTransformationMethod extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }

    }

}