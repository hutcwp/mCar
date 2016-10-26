package com.hut.cwp.mcar.zero.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.utils.Local;
import com.hut.cwp.mcar.zero.adapter.PlaceListAdapter;
import com.hut.cwp.mcar.zero.bean.CarInfoBean;
import com.hut.cwp.mcar.zero.bean.CityBean;
import com.hut.cwp.mcar.zero.bean.IllegalResultBean;
import com.hut.cwp.mcar.zero.bean.ProvinceBean;
import com.hut.cwp.mcar.zero.callback.IllegalCityCallBack;
import com.hut.cwp.mcar.zero.callback.IllegalResultCallback;
import com.hut.cwp.mcar.zero.view.ClassOrEngineNoDialog;
import com.hut.cwp.mcar.zero.view.LsnumDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Just on 2016/10/16.
 */

//还需要动态设置手动输入车辆信息时车架号和发动机号的位数

public class IllegalQueryActivity extends AppCompatActivity {
    private Button mBtBack;
    private Button mBtSelectProvince;
    private Button mBtSelectCity;
    private LinearLayout mLlQuery;
    private Button mBtQuery_;
    private TextView mTvQuery_;

    private ProgressBar mPbLoading;
    private LinearLayout mLlAll;

    private BottomSheetBehavior mBehavior;
    private View mBlackView;
    private RecyclerView mRvPlaceList;

    private ListView mLvCarInfo;
    private int mSelectedCarInfo = -1;

    private CarsInfoAdapter mCarsInfoAdapter;
    private List<CarInfoBean> mCarInfoBeanList;

    //用于记录当前的省和市
    //初始化需要在获取地理位置之后
    //如果获取失败，则为空
    private ProvinceBean mCurrentProvince;
    private CityBean mCurrentCity;
    private CarInfoBean mNeedsCarInfo;
    private String tempLsnum;
    private String tempClassno;
    private String tempEngineno;

    //用于判断当前的状态，是正在选择省、市、还是未操作
    private static int SELECT_STATE_NULL = 0;
    private static int SELECT_STATE_PROVINCE = 1;
    private static int SELECT_STATE_CITY = 2;
    private int mCurrentSelectState = SELECT_STATE_NULL;

    private static String URL_FOR_PLACE = "http://api.avatardata.cn/Wz/CityList?key=708be218b7f94d329a7f1a4ce8b1995b";
    private static String URL_FOR_ILLEGAL_PART = "http://api.avatardata.cn/Wz/Lookup?key=708be218b7f94d329a7f1a4ce8b1995b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_illegal_query);
        getSupportActionBar().hide();
        init();
    }

    private void setQueryState(boolean canQuery) {
        if (canQuery) {
            mBtQuery_.setBackgroundResource(R.drawable.z_il_qy_query);
            mTvQuery_.setTextColor(getResources().getColor(R.color.z_colorCanQuery));
            mLlAll.setClickable(true);
        } else {
            mBtQuery_.setBackgroundResource(R.drawable.z_il_qy_unquery);
            mTvQuery_.setTextColor(getResources().getColor(R.color.z_colorUnQuery));
            mLlAll.setClickable(false);
        }
    }

    private void init() {
        mPbLoading = (ProgressBar) findViewById(R.id.pb_il_qy_loading);
        mLlAll = (LinearLayout) findViewById(R.id.ll_il_qy_all);
        mLlAll.setVisibility(View.GONE);
        mPbLoading.setVisibility(View.VISIBLE);

        //加载数据
        OkHttpUtils.get().url(URL_FOR_PLACE).build().execute(new IllegalCityCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(IllegalQueryActivity.this, "获取服务数据失败！\n可能网络出错或服务器正在维护！\n请稍后再试~", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e2) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }

            @Override
            public void onResponse(final List<ProvinceBean> response, int id) {
                if (response.size() != 0) {
                    mBtBack = (Button) findViewById(R.id.bt_il_qy_back);
                    mBtSelectProvince = (Button) findViewById(R.id.bt_il_qy_select_province);
                    mBtSelectCity = (Button) findViewById(R.id.bt_il_qy_select_city);
                    mLlQuery = (LinearLayout) findViewById(R.id.ll_il_qy_query);
                    mBtQuery_ = (Button) findViewById(R.id.bt_il_qy_query_bt);
                    mTvQuery_ = (TextView) findViewById(R.id.tv_il_qy_query_tv);

                    mBlackView = findViewById(R.id.v_il_qy_black_view);
                    mRvPlaceList = (RecyclerView) findViewById(R.id.rv_il_qy_place_list);
                    mRvPlaceList.setLayoutManager(new LinearLayoutManager(IllegalQueryActivity.this));
                    mRvPlaceList.setItemAnimator(new DefaultItemAnimator());

                    mBehavior = BottomSheetBehavior.from(mRvPlaceList);
                    mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                                mBlackView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            // React to dragging events
                            mBlackView.setVisibility(View.VISIBLE);
                            ViewCompat.setAlpha(mBlackView, slideOffset);
                        }
                    });

                    mBtBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });//设置返回键
                    //设置省份选择按钮的事件
                    mBtSelectProvince.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PlaceListAdapter<ProvinceBean> provinceAdapter = new PlaceListAdapter<>(response, PlaceListAdapter.GRADE_PROVINCE, IllegalQueryActivity.this);
                            mRvPlaceList.setAdapter(provinceAdapter);
                            mCurrentSelectState = SELECT_STATE_PROVINCE;
                            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            provinceAdapter.setItemClickListener(new PlaceListAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(int pos) {
                                    mCurrentProvince = response.get(pos);
                                    Toast.makeText(IllegalQueryActivity.this, "您选择了" + mCurrentProvince.getProvince(), Toast.LENGTH_SHORT).show();
                                    mCurrentSelectState = SELECT_STATE_CITY;
                                    mBtSelectProvince.setText(mCurrentProvince.getProvince());
                                    try {
                                        mBtSelectCity.callOnClick();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    //设置城市选择按钮的事件
                    mBtSelectCity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mCurrentProvince != null) {
                                PlaceListAdapter<CityBean> cityAdapter = new PlaceListAdapter<>(mCurrentProvince.getCities(), PlaceListAdapter.GRADE_CITY, IllegalQueryActivity.this);
                                cityAdapter.setItemClickListener(new PlaceListAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(int pos) {
                                        mCurrentCity = mCurrentProvince.getCities().get(pos);
                                        try {
                                            Log.d("测试+++++++++","classa="+mCurrentCity.getClassa());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(IllegalQueryActivity.this, "您选择了" + mCurrentCity.getCity_name(), Toast.LENGTH_SHORT).show();
                                        mCurrentSelectState = SELECT_STATE_NULL;
                                        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                        mBtSelectCity.setText(mCurrentCity.getCity_name());
                                        setDynamicManualCarInfo();
                                    }
                                });
                                mRvPlaceList.setAdapter(cityAdapter);
                                mCurrentSelectState = SELECT_STATE_CITY;
                                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            } else {
                                Toast.makeText(IllegalQueryActivity.this, "请先选择省级地区！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //设置用于在选择地区时产生阴暗效果的View的点击事件
                    mBlackView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCurrentSelectState = SELECT_STATE_NULL;
                            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });
                    //设置查询区域的点击事件(需要通过OkHttp获取数据之后直接传到显示结果的界面)
                    mLlQuery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //显示正在查询的ProcessBar+++++++++++++++++

                            String engnine = ("1".equals(mCurrentCity.getEngine())) ?
                                    ("0".equals(mCurrentCity.getEngineno()) ? mNeedsCarInfo.getEngineno() : mNeedsCarInfo.getEngineno().substring(0, Integer.parseInt(mCurrentCity.getEngineno()))) :
                                    null;

                            String classno = ("1".equals(mCurrentCity.getClassa())) ? ("0".equals(mCurrentCity.getClassno()) ? mNeedsCarInfo.getClassno() : mNeedsCarInfo.getClassno().substring(0, Integer.parseInt(mCurrentCity.getClassno()))) : null;
                            String url = getUrlForIllegal(mCurrentCity.getCity_code(),
                                    mNeedsCarInfo.getLsnum(), classno, engnine);

                            OkHttpUtils.get().url(url).build().execute(new IllegalResultCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    //关闭正在查询的ProcessBar+++++++++++++++++
                                    Toast.makeText(IllegalQueryActivity.this, "查询失败！\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(List<IllegalResultBean> response, int id) {
                                    if (response.size() != 0) {
                                        Intent intent = new Intent(IllegalQueryActivity.this, IllegalQueryActivity.class);
                                        intent.putExtra("lsnum", mNeedsCarInfo.getLsnum());
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelableArrayList("result", (ArrayList<? extends Parcelable>) response);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(IllegalQueryActivity.this, "所查地区未有违章记录!", Toast.LENGTH_SHORT).show();
                                    }
                                    //关闭正在查询的ProcessBar+++++++++++++++++
                                }
                            });
                        }
                    });

                    mLvCarInfo = (ListView) findViewById(R.id.lv_il_qy_car_info);

                    //TODO 应该需要开启一个子线程
                    mCarInfoBeanList = getUserCarInfo();
                    mCarsInfoAdapter = new CarsInfoAdapter();
                    mLvCarInfo.setAdapter(mCarsInfoAdapter);
                    mLvCarInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mSelectedCarInfo = position;
                            mCarsInfoAdapter.notifyDataSetChanged();
                        }
                    });

                    setQueryState(false);

                    String currentProvince = getCurrentProvince();
                    String currentCity = getCurrentCity();
                    for (ProvinceBean tempProvince : response) {
                        if (tempProvince.getProvince().equals(currentProvince)) {
                            mCurrentProvince = tempProvince;
                            mBtSelectProvince.setText(currentProvince);
                            List<CityBean> tempCities = tempProvince.getCities();
                            for (CityBean tempCity : tempCities)
                                if (tempCity.getCity_name().equals(currentCity)) {
                                    mCurrentCity = tempCity;
                                    setDynamicManualCarInfo();
                                    mBtSelectCity.setText(currentCity);
                                    break;
                                }
                            break;
                        }
                    }

                    //在最后把用于加载的提示隐藏，在进入界面时也需要显示
                    mLlAll.setVisibility(View.VISIBLE);
                    mPbLoading.setVisibility(View.GONE);
                    Toast.makeText(IllegalQueryActivity.this, "加载服务数据完成！", Toast.LENGTH_SHORT).show();

                    if (mCurrentProvince == null) {
                        Toast.makeText(IllegalQueryActivity.this, "当前地区不支持查询违章服务!", Toast.LENGTH_SHORT).show();
                    } else if (mCurrentCity == null) {
                        Toast.makeText(IllegalQueryActivity.this, "当前城市不支持查询违章服务!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(IllegalQueryActivity.this, "获取服务数据失败!请稍后再试", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mCurrentSelectState == SELECT_STATE_NULL) {
            finish();
        } else {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mCurrentSelectState = SELECT_STATE_NULL;
        }
    }

    private String getUrlForIllegal(String city, @Nullable String lsnum, @Nullable String classno, @Nullable String engineno) {
        String url = URL_FOR_ILLEGAL_PART + "&city" + city;
        if (!TextUtils.isEmpty(lsnum)) url += "&lsnum=" + lsnum;
        if (!TextUtils.isEmpty(classno)) url += "&classno==" + classno;
        if (!TextUtils.isEmpty(engineno)) url += "&engineno" + engineno;
        return url;
    }

    private String getCurrentProvince() {
        return Local.getProvince();
    }

    private String getCurrentCity() {
        return Local.getCity();
    }

    private List<CarInfoBean> getUserCarInfo() {
        //TODO 需要真实的情况

        List<CarInfoBean> list = new ArrayList<CarInfoBean>();
        CarInfoBean tempBean = new CarInfoBean();
        tempBean.setLsnum("湘A383838");
        tempBean.setClassno("123456");
        tempBean.setEngineno("123456");
        list.add(tempBean);
        CarInfoBean tempBean1 = new CarInfoBean();
        tempBean1.setLsnum("湘A88888");
        tempBean1.setClassno("123456");
        tempBean1.setEngineno("123456");
        list.add(tempBean1);
        CarInfoBean tempBean2 = new CarInfoBean();
        tempBean2.setLsnum("点击输入车牌号");
        tempBean2.setClassno("点击输入车架号");
        tempBean2.setEngineno("点击输入发动机号");
        list.add(tempBean2);
        return list;
    }

    /**
     * 动态设置手动输入车架号和发动机号时的位数，此时存有汽车的信息的的List必需存在
     */
    //还需要调整 1- 0是不是表示不需要该数据  2- 动态设置BottomDialog的输入位数
    private void setDynamicManualCarInfo() {
        CarInfoBean tempCarInfoBean = mCarInfoBeanList.get(mCarInfoBeanList.size() - 1);
        if ("0".equals(mCurrentCity.getClassa())) {//0表示不需要车架号
            tempCarInfoBean.setClassno("不需要车架号");
        } else if ("1".equals(mCurrentCity.getClassa())) {
            tempCarInfoBean.setClassno("点击输入" + ("0".equals(mCurrentCity.getClassno())?"全部":"后"+mCurrentCity.getClassno()+"位") + "车架号");
        }
        if ("0".equals(mCurrentCity.getEngine())) {
            tempCarInfoBean.setEngineno("不需要发动机号");
        } else if ("1".equals(mCurrentCity.getEngine())) {
            tempCarInfoBean.setEngineno("点击输入" + ("0".equals(mCurrentCity.getEngineno())?"全部":"后"+mCurrentCity.getClassno()+"位") + "发动机号");
        }
        Log.d("测试++++++",mCarInfoBeanList.get(mCarInfoBeanList.size() - 1).getClassno());
        mCarsInfoAdapter.notifyDataSetChanged();
    }

    public class CarsInfoAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public CarsInfoAdapter() {
            mInflater = LayoutInflater.from(IllegalQueryActivity.this);
        }

        @Override
        public int getCount() {
            return mCarInfoBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCarInfoBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.z_item_car_info, parent, false);

                holder.tvLsnum = (TextView) convertView.findViewById(R.id.tv_il_qy_car_info_lsnum);
                holder.tvEngineno = (TextView) convertView.findViewById(R.id.tv_il_qy_car_info_engineno);
                holder.tvClassno = (TextView) convertView.findViewById(R.id.tv_il_qy_car_info_classno);
                holder.tvLsnum.setText("");
                holder.tvEngineno.setText("");
                holder.tvClassno.setText("");
                holder.rbSelect = (RadioButton) convertView.findViewById(R.id.rb_il_qy_car_info_radio_box);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CarInfoBean temp = mCarInfoBeanList.get(position);
            holder.tvLsnum.setText(temp.getLsnum());
            holder.tvClassno.setText(temp.getEngineno());
            holder.tvEngineno.setText(temp.getClassno());
            if (position == mCarInfoBeanList.size() - 1) {
                if (!TextUtils.isEmpty(tempLsnum)) holder.tvLsnum.setText(tempLsnum);
                if (!TextUtils.isEmpty(tempClassno)) holder.tvClassno.setText(tempClassno);
                if (!TextUtils.isEmpty(tempEngineno)) holder.tvEngineno.setText(tempEngineno);
            }
            if (position == mCarInfoBeanList.size() - 1) {
                CarInfoBean tempCarInfoBean = mCarInfoBeanList.get(position);
                holder.tvLsnum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LsnumDialog dialog = new LsnumDialog(IllegalQueryActivity.this);
                        dialog.setCompletedListener(new LsnumDialog.ICompleted() {
                            @Override
                            public void afterCompleted(String result) {
                                tempLsnum = result;
                                notifyDataSetChanged();
                            }
                        });
                        dialog.show(getSupportFragmentManager());
                    }
                });
                if (!tempCarInfoBean.getClassno().equals("不需要车架号")) {
                    holder.tvClassno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClassOrEngineNoDialog dialog = new ClassOrEngineNoDialog(IllegalQueryActivity.this, ClassOrEngineNoDialog.TAG_FOR_CLASS);
                            dialog.setCompletedListener(new ClassOrEngineNoDialog.ICompleted() {
                                @Override
                                public void afterCompleted(String result) {
                                    tempClassno = result;
                                    notifyDataSetChanged();
                                }
                            });
                            dialog.show(getSupportFragmentManager());
                        }
                    });
                }
                if (!tempCarInfoBean.getEngineno().equals("不需要发动机号")) {
                    holder.tvEngineno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClassOrEngineNoDialog dialog = new ClassOrEngineNoDialog(IllegalQueryActivity.this, ClassOrEngineNoDialog.TAG_FOR_ENGINE);
                            dialog.setCompletedListener(new ClassOrEngineNoDialog.ICompleted() {
                                @Override
                                public void afterCompleted(String result) {
                                    tempEngineno = result;
                                    notifyDataSetChanged();
                                }
                            });
                            dialog.show(getSupportFragmentManager());
                        }
                    });
                }
            }
            if (mSelectedCarInfo == position) {
                holder.rbSelect.setChecked(true);
                holder.rbSelect.setBackgroundResource(R.drawable.z_il_qy_radio_box_selected);
                if (position != mCarInfoBeanList.size() - 1) {
                    mNeedsCarInfo = mCarInfoBeanList.get(position);
                    setQueryState(true);
                } else {
                    if (!TextUtils.isEmpty(tempLsnum) && !TextUtils.isEmpty(tempClassno) && !TextUtils.isEmpty(tempEngineno)) {
                        CarInfoBean bean = new CarInfoBean();
                        bean.setLsnum(tempLsnum);
                        bean.setClassno(tempClassno);
                        bean.setEngineno(tempEngineno);
                        mNeedsCarInfo = bean;
                        setQueryState(true);
                    } else {
                        mNeedsCarInfo = null;
                        setQueryState(false);
                    }
                }

            } else {
                holder.rbSelect.setChecked(false);
                holder.rbSelect.setBackgroundResource(R.drawable.z_il_qy_radio_box_unselect);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tvLsnum, tvEngineno, tvClassno;
            RadioButton rbSelect;
        }
    }
}
