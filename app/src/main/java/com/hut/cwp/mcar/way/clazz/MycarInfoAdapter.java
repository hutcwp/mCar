package com.hut.cwp.mcar.way.clazz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;
import com.hut.cwp.mcar.base.utils.ProxyLodingProgress;
import com.hut.cwp.mcar.way.activitys.InfocarActivity;
import com.hut.cwp.mcar.zero.view.LsnumDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MycarInfoAdapter extends ArrayAdapter<CarInfo> {

    private int resourceId;

    private Context mContext;

    private List<CarInfo> mData = new ArrayList<>();

    private InfocarActivity infoCarActivity;

    private View view = null;
    private TextView licensePlateInfo;
    private TextView engineInfo;
    private TextView frameInfo;

    public MycarInfoAdapter(Context context, int textViewResourceId, List<CarInfo> objects) {
        super(context, textViewResourceId, objects);

        this.infoCarActivity = (InfocarActivity) context;
        this.mContext = context;
        this.resourceId = textViewResourceId;
        this.mData = objects;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        initAddCar(position);
        initDeleteCar(position);

        initListener();


        return view;
    }


    /**
     * 为add添加监听事件
     */
    private void initListener() {
        frameInfo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addCar();
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 初始化initAddCar
     *
     * @param position
     */

    private void initAddCar(int position) {

        if (mData.get(position).getLicensePlate().toString().trim().equals("")) {

            /**
             * 如果是可添加的item
             * 将初始化为EditText
             */

            view = LayoutInflater.from(getContext()).inflate(R.layout.way_item_mycar_add, null);

            licensePlateInfo = (EditText) view.findViewById(R.id.way_itme_licensePlate);
            engineInfo = (EditText) view.findViewById(R.id.way_itme_engine);
            frameInfo = (EditText) view.findViewById(R.id.way_itme_frame);


            engineInfo.setTransformationMethod(new AllCapTransformationMethod());
            frameInfo.setTransformationMethod(new AllCapTransformationMethod());

            licensePlateInfo.setInputType(InputType.TYPE_NULL);
            licensePlateInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    licensePlateInfo.setFocusable(true);
                    LsnumDialog dialog = new LsnumDialog(mContext);
                    dialog.setCompletedListener(new LsnumDialog.ICompleted() {
                        @Override
                        public void afterCompleted(String result) {
                            licensePlateInfo.setText(result);
                        }
                    });

                    dialog.show(infoCarActivity.getSupportFragmentManager());

                }
            });

        } else {
            /**
             * 如果不是可添加的item
             * 将初始化为TextView
             */
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            licensePlateInfo = (TextView) view.findViewById(R.id.way_itme_licensePlate);
            engineInfo = (TextView) view.findViewById(R.id.way_itme_engine);
            frameInfo = (TextView) view.findViewById(R.id.way_itme_frame);

            licensePlateInfo.setText(mData.get(position).getLicensePlate().toString());
            engineInfo.setText(mData.get(position).getEngine().toString());
            frameInfo.setText(mData.get(position).getVin().toString());
        }

    }

    /**
     * 初始化方法AddCar
     *
     * @param position
     */
    private void initDeleteCar(final int position) {

        if (resourceId == R.layout.way_item_mycar_deleted) {
            /**
             * 如果是可删除状态
             *  初始化删除Button
             *  添加点击事件
             */
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
    }

    /**
     * 方法 ：车辆删除
     */

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

    /**
     * 方法 :车辆添加
     */

    private void addCar() {

        String licensePlate = licensePlateInfo.getText().toString().trim();
        String engine = engineInfo.getText().toString().trim();
        String vin = frameInfo.getText().toString().trim();

        if (TextUtils.isEmpty(licensePlate) || TextUtils.isEmpty(engine) || TextUtils.isEmpty(vin)) {
            Toast.makeText(mContext, "某项内容不能为空", Toast.LENGTH_SHORT).show();
        } else if (licensePlate.length() != 7 || vin.length() != 17) {
            Toast.makeText(mContext, "某项内容不合法", Toast.LENGTH_SHORT).show();
        } else {

            ProxyLodingProgress.show(infoCarActivity);
            //加入信息
            CarInfo carInfo = new CarInfo();
            carInfo.setUsername(MyApplication.getUsername());
            carInfo.setLicensePlate(licensePlate);
            carInfo.setEngine(engine);
            carInfo.setVin(vin);

            carInfo.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        infoCarActivity.refreshData();
//                        Intent intent = new Intent(infoCarActivity, InfoCarActivity.class);
//                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(infoCarActivity, "添加失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    ProxyLodingProgress.hide();
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