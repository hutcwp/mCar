package com.hut.cwp.mcar.activitys.info;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.info.intefaces.InfoView;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.WayActivityCarInfoBinding;
import com.hut.cwp.mcar.utils.ProxyLodingProgress;
import com.hut.cwp.mcar.activitys.business.view.LsnumDialog;


public class CarInfoActivity extends BaseActivity implements InfoView {

    public CarInfoPresent present;
    public WayActivityCarInfoBinding Binding;


    @Override
    protected int getLayoutId() {

        return R.layout.way_activity_car_info;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (WayActivityCarInfoBinding) getBinding();

        Binding.lyTitle.btnBack.setOnClickListener(v -> finish());
        Binding.lyTitle.tvTitle.setText(R.string.myCar);

        Binding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.colorAccent);


        Binding.listview.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(CarInfoActivity.this,
                        "用户名 : " + present.myCarInfoList.get(position).getUsername(),
                        Toast.LENGTH_LONG).show());

        Binding.ivDeleteCar.setOnClickListener(v -> {
            showDeleteCar();
        });

        Binding.ivAddCar.setOnClickListener(v -> {
            showAddCar();
        });

        Binding.swipeRefresh.setOnRefreshListener(() -> {

            Binding.swipeRefresh.setRefreshing(true);
            present.refreshData();

        });

    }

    @Override
    protected void loadData() {
        setPresent();
        present.initData();
    }


    @Override
    public void setPresent() {
        if (present == null) {
            present = new CarInfoPresent(CarInfoActivity.this);
        }
    }


    /**
     * 显示加载进度条
     */
    @Override
    public void showProgress() {

        ProxyLodingProgress.show(CarInfoActivity.this);
    }

    /**
     * 关闭加载进度条
     */
    @Override
    public void hideProgress() {

        ProxyLodingProgress.hide();
    }

    /**
     * show添加的界面
     */
    @Override
    public void showAddCar() {

        present.addAble = true;

        View view = LayoutInflater.from(CarInfoActivity.this).inflate(R.layout.dlg_add_car, null);
        Button btn = (Button) view.findViewById(R.id.btn_ok);

        TextView licensePlateInfo = (TextView) view.findViewById(R.id.way_itme_licensePlate);
        TextView engineInfo = (TextView) view.findViewById(R.id.way_itme_engine);
        TextView frameInfo = (TextView) view.findViewById(R.id.way_itme_frame);

        AlertDialog dlg;
        AlertDialog.Builder builder = new AlertDialog.Builder(CarInfoActivity.this);
        builder.setView(view);
        dlg = builder.create();
        dlg.show();

        licensePlateInfo.setOnFocusChangeListener((view1, b) -> {
            //自定义键盘的使用
            LsnumDialog dialog = new LsnumDialog(CarInfoActivity.this);
            dialog.setCompletedListener(result -> licensePlateInfo.setText(result));
            dialog.show(getSupportFragmentManager());
        });

        btn.setOnClickListener(v1 -> {

            String licensePlate = licensePlateInfo.getText().toString().trim();
            String engine = engineInfo.getText().toString().trim();
            String vin = frameInfo.getText().toString().trim();

            if (TextUtils.isEmpty(licensePlate) || TextUtils.isEmpty(engine) || TextUtils.isEmpty(vin)) {
                Toast.makeText(CarInfoActivity.this, "选项内容不能为空！", Toast.LENGTH_SHORT).show();
            } else if (vin.length() != 17) {
                Toast.makeText(CarInfoActivity.this, "车架号需要是17位数！", Toast.LENGTH_SHORT).show();
            } else if (licensePlate.length() != 7) {
                Toast.makeText(CarInfoActivity.this, "发动机号需要是7位数！", Toast.LENGTH_SHORT).show();
            } else {
                present.addCar(licensePlate, engine, vin);
                dlg.dismiss();
            }
        });
    }

    /**
     * show 删除的界面
     */
    @Override
    public void showDeleteCar() {

        present.initAdapter(R.layout.way_item_mycar_deleted);
        present.deleteAble = true;
    }


    /**
     * 设置car的总数
     * @param count 总数
     */
    void setCarCount(int count) {

        String carCount = count + "";
        Binding.tvCarCount.setText(carCount);
    }

    //返回键
    @Override
    public void onBackPressed() {
        if (present.deleteAble) {
            present.deleteAble = false;
            present.initAdapter(R.layout.way_item_mycar);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProxyLodingProgress.destroy();
    }

}

