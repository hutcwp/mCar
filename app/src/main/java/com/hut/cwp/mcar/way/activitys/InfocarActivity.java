package com.hut.cwp.mcar.way.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;
import com.hut.cwp.mcar.base.utils.ProxyLodingProgress;
import com.hut.cwp.mcar.way.clazz.CarInfo;
import com.hut.cwp.mcar.way.clazz.MyCarInfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class InfoCarActivity extends AppCompatActivity {

    private ImageView addCar;
    private ImageView deleteCar;
    private ImageView btn_back;
    private TextView text_content;

    private TextView count;

    private ListView listView;

    private List<Map<String, Object>> datalist;

    private MyCarInfoAdapter adapter;

    private boolean deletedable = false;

    private boolean addedable = true;

    private List<CarInfo> myCarInfoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way_activity_infocar);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initFindViewById();

        initListData();


        initSetListener();

    }


    private void initFindViewById() {

        text_content = (TextView) findViewById(R.id.menu_title_text_content);
        btn_back = (ImageView) findViewById(R.id.menu_title_btn_back);


        addCar = (ImageView) findViewById(R.id.add_intcar);
        deleteCar = (ImageView) findViewById(R.id.delete_intcar);
        count = (TextView) findViewById(R.id.count_mycar);

        listView = (ListView) findViewById(R.id.mycar_list_view);

        ProxyLodingProgress.show(InfoCarActivity.this);
    }

    private void initListData() {

        deletedable = false;
        addedable = true;

        myCarInfoList = new ArrayList<>();
        BmobQuery<CarInfo> query = new BmobQuery<>();

        //查询playerName叫“13874939742”的数据
        query.addWhereEqualTo("username", MyApplication.getUsername());

        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);

        //执行查询方法
        query.findObjects(new FindListener<CarInfo>() {
            @Override
            public void done(List<CarInfo> object, BmobException e) {
                if (e == null) {
                    Toast.makeText(InfoCarActivity.this, "查询成功：共" + object.size() + "条数据。", Toast.LENGTH_SHORT).show();
                    for (CarInfo carInfo : object) {

                        myCarInfoList.add(carInfo);
                    }
                    initAdapter(R.layout.way_item_mycar);
                    count.setText(myCarInfoList.size() + "");
                    ProxyLodingProgress.hide();

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }


    private void initAdapter(int item_res_id) {

        adapter = new MyCarInfoAdapter(InfoCarActivity.this, item_res_id, myCarInfoList);
        listView.setAdapter(adapter);
    }

    private void initSetListener() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_content.setText("我的私家车");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(InfoCarActivity.this, "用户名 : " + myCarInfoList.get(position).getUsername(), Toast.LENGTH_LONG).show();

            }
        });


        addCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (addedable) {
                    myCarInfoList.add(new CarInfo());
                    initAdapter(R.layout.way_item_mycar);
                    addedable = false;
                }

//                Intent intent = new Intent(InfoCarActivity.this, AddCarActivity.class);
//                startActivity(intent);
//                finish();

            }
        });

        deleteCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                deletedable = true;
                initAdapter(R.layout.way_item_mycar_deleted);

            }

        });
    }


    public void refreshData() {

        initListData();
    }

    @Override
    public void onBackPressed() {

        if (deletedable) {
            deletedable = false;

            initAdapter(R.layout.way_item_mycar);
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

