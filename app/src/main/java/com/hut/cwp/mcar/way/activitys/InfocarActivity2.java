package com.hut.cwp.mcar.way.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.way.clazz.CarInfo;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class InfocarActivity2 extends Activity {

    private EditText licensePlateEdit;

    private EditText engineEdit;

    private EditText frameEdit;

    private ImageButton intcar;

    private ImageButton returnon;

    //private String data ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_layout_intcar);

        licensePlateEdit = (EditText) findViewById(R.id.licensePlate_intcar_Text);
        engineEdit = (EditText) findViewById(R.id.engine_intcar_Text);
        frameEdit = (EditText) findViewById(R.id.frame_intcar_Text);
        intcar = (ImageButton) findViewById(R.id.bu_intcar_finish);
        returnon =  (ImageButton) findViewById(R.id.bu_return9);

        intcar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String licensePlate = licensePlateEdit.getText().toString().trim();
                String engine = engineEdit.getText().toString().trim();
                String frame = frameEdit.getText().toString().trim();

                if (TextUtils.isEmpty(licensePlate)||TextUtils.isEmpty(engine)||TextUtils.isEmpty(frame)) {
                    Toast.makeText(InfocarActivity2.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(licensePlate.length()!=7||engine.length()<=7||frame.length()!=17){
                    Toast.makeText(InfocarActivity2.this, "内容输入不合法", Toast.LENGTH_SHORT).show();
                }
                else {

                    //加入信息
                    CarInfo carInfo = new CarInfo();
                    BmobUser bu2 = BmobUser.getCurrentUser();

                    carInfo.setUsernameObject(bu2.getUsername());
                    //carInfo.setUsernameObjectId(bu2.getObjectId());
                    carInfo.setLicensePlate(licensePlate);
                    carInfo.setEngine(engine);
                    carInfo.setFrame(frame);

                    carInfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){
                                //toast("添加数据成功，返回objectId为："+objectId);
                                //data = objectId ;
                                Toast.makeText(InfocarActivity2.this, "添加数据成功，返回objectId为："+objectId, Toast.LENGTH_SHORT).show();
                                //String theObjectId = objectId ;
                                //String theObjectId = data ;
                                Intent intent = new Intent(InfocarActivity2.this,InfocarActivity.class);
                                //intent.putExtra("oid",theObjectId);
                                startActivity(intent);
                                finish();
                            }else{
                                //toast("创建数据失败：" + e.getMessage());
                                Toast.makeText(InfocarActivity2.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //

                }


            }
        });


        returnon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //String theObjectId = data ;
                Intent intent = new Intent(InfocarActivity2.this,InfocarActivity.class);
                //intent.putExtra("oid",theObjectId);
                startActivity(intent);
                finish();

            }
        });




    }
}
