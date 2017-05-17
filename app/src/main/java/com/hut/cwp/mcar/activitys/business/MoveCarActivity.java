package com.hut.cwp.mcar.activitys.business;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.info.bean.CarInfo;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.activitys.business.view.LsnumDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MoveCarActivity extends BaseActivity {
    private TextView mTvPhone, mTvHint;
    private Button mBtCall;
    private FrameLayout mFlLoading;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private String phone;

    @Override
    protected int getLayoutId() {
        return R.layout.z_activity_move_car;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        init();
    }

    @Override
    protected void loadData() {

    }



    public void init() {
        mTvPhone = (TextView) findViewById(R.id.tv_move_car_phone);
        mTvHint = (TextView) findViewById(R.id.tv_move_car_hint);
        mBtCall = (Button) findViewById(R.id.bt_move_car_call);
        mFlLoading = (FrameLayout) findViewById(R.id.rl_move_car_loading);
        mTvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义键盘的使用
                LsnumDialog dialog = new LsnumDialog(MoveCarActivity.this);
                dialog.setCompletedListener(new LsnumDialog.ICompleted() {
                    @Override
                    public void afterCompleted(final String result) {
                        mTvPhone.setText(result);
                        mFlLoading.setVisibility(View.VISIBLE);
                        new Thread(() -> {
                            BmobQuery<CarInfo> query = new BmobQuery<>();
                            query.addWhereEqualTo("licensePlate", result);
                            query.setLimit(1);
                            query.findObjects(new FindListener<CarInfo>() {
                                @Override
                                public void done(final List<CarInfo> object, final BmobException e) {
                                    mHandler.post(() -> {
                                        if (e == null && object.size() != 0) {
                                            phone = object.get(0).getUsername();
                                        } else {
                                            if (e != null)
                                                Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                            mBtCall.setBackgroundResource(R.drawable.z_bcg_move_car_failed);
                                            phone = null;
                                            mTvHint.setText("抱歉，暂无该车主信息，无法通知移车");
                                        }
                                        mFlLoading.setVisibility(View.GONE);
                                    });
                                }
                            });
                        }).start();
                    }
                });
                try {
                    mTvHint.setText("请选择联系车主或返回继续等待");
                    phone = null;
                    mTvPhone.setText("");
                    mBtCall.setBackgroundResource(R.drawable.z_bcg_feedback_success);
                    dialog.show(getSupportFragmentManager());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBtCall.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(phone)) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(MoveCarActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            } else {
                Toast.makeText(MoveCarActivity.this, "无法拨号", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back(View v) {
        finish();
    }
}
