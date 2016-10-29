package com.hut.cwp.mcar.way.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;
import com.hut.cwp.mcar.base.utils.ProxyLodingProgress;
import com.hut.cwp.mcar.way.clazz.CarInfo;
import com.hut.cwp.mcar.zero.view.LsnumDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class AddCarActivity extends AppCompatActivity {

    private TextView licensePlateEdit;
    private TextView engineEdit;
    private TextView vinEdit;

    private ImageButton intcar;
    private ImageButton returnon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_activity_addcar);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        licensePlateEdit = (EditText) findViewById(R.id.licensePlate_intcar_Text);
        engineEdit = (EditText) findViewById(R.id.engine_intcar_Text);
        vinEdit = (EditText) findViewById(R.id.frame_intcar_Text);

        intcar = (ImageButton) findViewById(R.id.bu_intcar_finish);
        returnon = (ImageButton) findViewById(R.id.bu_return9);

        engineEdit.setTransformationMethod(new AllCapTransformationMethod());
        vinEdit.setTransformationMethod(new AllCapTransformationMethod());


        licensePlateEdit.setInputType(InputType.TYPE_NULL);
        licensePlateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                licensePlateEdit.setFocusable(true);
                LsnumDialog dialog = new LsnumDialog(AddCarActivity.this);
                dialog.setCompletedListener(new LsnumDialog.ICompleted() {
                    @Override
                    public void afterCompleted(String result) {
                        licensePlateEdit.setText(result);
                    }
                });

                dialog.show(getSupportFragmentManager());

            }
        });

        intcar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String licensePlate = licensePlateEdit.getText().toString().trim();
                String engine = engineEdit.getText().toString().trim();
                String vin = vinEdit.getText().toString().trim();

                if (TextUtils.isEmpty(licensePlate) || TextUtils.isEmpty(engine) || TextUtils.isEmpty(vin)) {
                    Toast.makeText(AddCarActivity.this, "某项内容不能为空", Toast.LENGTH_SHORT).show();
                } else if (licensePlate.length() != 7 || vin.length() != 17) {
                    Toast.makeText(AddCarActivity.this, "某项内容不合法", Toast.LENGTH_SHORT).show();
                } else {

                    ProxyLodingProgress.show(AddCarActivity.this);
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
                                Toast.makeText(AddCarActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                Intent intent = new Intent(AddCarActivity.this, InfoCarActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AddCarActivity.this, "添加失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            ProxyLodingProgress.hide();
                        }
                    });
                }
            }
        });


        returnon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(AddCarActivity.this, InfoCarActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ProxyLodingProgress.destroy();
    }
}
