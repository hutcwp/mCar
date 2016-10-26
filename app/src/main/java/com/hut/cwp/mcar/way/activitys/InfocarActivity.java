package com.hut.cwp.mcar.way.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.way.clazz.CarInfo;
import com.hut.cwp.mcar.way.clazz.MycarInfo;
import com.hut.cwp.mcar.way.clazz.MycarInfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class InfocarActivity extends Activity {

    private ImageView addCar;

    private ImageView deleteCar;

    private ImageView asd;

    private TextView count;

    private ListView listView;

    private List<Map<String,Object>> datalist;

    private MycarInfoAdapter adapter;

    private MycarInfoAdapter adapter2;

    private boolean deletein = false;

    private List<MycarInfo> mycarInfoList = new ArrayList<>();
    private List<CarInfo> mData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycar);


//        adapter = new MycarInfoAdapter(InfocarActivity.this, R.layout.way_my_car_itme, mData);
//        listView = (ListView) findViewById(R.id.mycar_list_view);
//        listView.setAdapter(adapter);
//
        addCar = (ImageView) findViewById(R.id.add_intcar);
        deleteCar = (ImageView) findViewById(R.id.delete_intcar);
//        count = (TextView) findViewById(R.id.count_mycar);
//
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Toast.makeText(InfocarActivity.this, "" + mData.get(position).getObjectId(), Toast.LENGTH_LONG).show();
//
//            }
//        });
//
        addCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InfocarActivity.this, InfocarActivity2.class);
                startActivity(intent);
                //finish();

            }
        });

        deleteCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                if (deletein) {
//                    listView.setAdapter(adapter);
//                    deletein = false;
//                } else {
//                    adapter2 = new MycarInfoAdapter(InfocarActivity.this, R.layout.way_my_car_itme2, mData);
//                    listView.setAdapter(adapter2);
//                    Toast.makeText(InfocarActivity.this, "点击右上角图标即可删除", Toast.LENGTH_SHORT).show();
//                    deletein = true;
//                }


//                initMycarInfos();

            }
//
//            private void initMycarInfos() {
//
//                BmobQuery<CarInfo> query = new BmobQuery<CarInfo>();
//
//                BmobUser bu1 = BmobUser.getCurrentUser();
//
//                query.addWhereEqualTo("usernameObject", bu1.getUsername());
//
//                query.setLimit(20);
//
//                query.findObjects(new FindListener<CarInfo>() {
//                    @Override
//                    public void done(final List<CarInfo> object, BmobException e) {
//                        if (e == null) {
//
//                            mData.addAll(object);
//                            adapter.notifyDataSetChanged();
//
//                            count.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    count.setText("" + object.size());
//                                }
//                            });
//
//
//                        } else {
//                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                        }
//
//                    }
//                });
//
//
//            }
//
        });
    }

}

