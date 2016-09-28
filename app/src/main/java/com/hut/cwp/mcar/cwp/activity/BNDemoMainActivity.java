package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.hut.cwp.mcar.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BNDemoMainActivity extends Activity {


    public static List<Activity> activityList = new LinkedList<>();

    private static final String APP_FOLDER_NAME = "mCar";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    private String authinfo = null;
    private String mSDCardPath = null;

    //**********************
    private final static String TAG = "MainActivity";

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private BaiduMap mBaiduMap;
    private MapView mMapView = null;

    private Button btn_go;
    private Button btn_search = null;

    private AutoCompleteTextView autoCompleteTextView;

    private double endLatitue;
    private double endLongLatitue;
    private double startLatitue;
    private double startLongLatitue;

    private List<String> listData;
    private List<SuggestionResult.SuggestionInfo> suggestionInfosData;

    private ArrayAdapter<String> adapter;

    private SuggestionSearch mSuggestionSearch;

    private View.OnClickListener onClickListener;
    private OnGetSuggestionResultListener onGetSuggestionResultListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        activityList.add(this);
        setContentView(R.layout.cwp_layout_baidu_map);

        //**********************************************

        initMap();
        initLocation();


        //**********************************************

        // 打开log开关
        BNOuterLogUtil.setLogSwitcher(true);
        if (initDirs()) {
            initNavi();
        }

    }


    //***************************************初始化区域****************************************//

    /**
     * 初始化目录
     */
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 初始化导航
     */
    private void initNavi() {

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                BNDemoMainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(BNDemoMainActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();

            }

            public void initStart() {
                Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }

    //***************************************初始化区域****************************************//


    //***************************************导航算路方法****************************************//

    /**
     * 导航以及路线规划
     */
    private void routeplanToNavi() {

        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;

        sNode = new BNRoutePlanNode(startLongLatitue, startLatitue, null, null, CoordinateType.BD09LL);
        eNode = new BNRoutePlanNode(endLongLatitue, endLatitue, null, null, CoordinateType.BD09LL);

        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }
    //***************************************导航算路方法****************************************//


    //***************************************接口设定****************************************//
    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            showToastMsg("TTSPlayStateListener : 语音播放结束");
        }

        @Override
        public void playStart() {
            showToastMsg("TTSPlayStateListener : 语音播放开始");
        }
    };

    //***************************************接口设定****************************************//


    //***************************************辅助工具****************************************//

    /**
     * 显示Tosat弹窗
     */
    public void showToastMsg(final String msg) {
        BNDemoMainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(BNDemoMainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * *工具设置类
     */
    private void initSetting() {
        // 设置是否双屏显示
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        // 设置导航播报模式
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // 是否开启路况
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

    /**
     * 获得SD卡目录
     */
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    //***************************************辅助工具****************************************//


    public class DemoRoutePlanListener implements RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
			 */
            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(BNDemoMainActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(BNDemoMainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }

    }

    //************************************



    /**
     * 在线建议查询
     */
    private void suggestionSearch(String City, String Key) {

        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(Key)
                .city(City));

    }

    /**
     * 初始化地图所需要的基本数据
     */
    private void initMap() {
        //获取地图控件引用 必须放在.getMap()之前
        mMapView = (MapView) findViewById(R.id.bimapView);
        mBaiduMap = mMapView.getMap();

        mSuggestionSearch = SuggestionSearch.newInstance();

        btn_go = (Button) findViewById(R.id.btn_go);
        btn_search = (Button) findViewById(R.id.btn_search);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        //缩放地图，让地图更加美观
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);

        //初始化listener
        initListener();

        listData = new ArrayList<>();
        suggestionInfosData = new ArrayList<>();


        initListData();
        initAutoCompeleted();

        btn_go.setOnClickListener(onClickListener);
        btn_search.setOnClickListener(onClickListener);

        mSuggestionSearch.setOnGetSuggestionResultListener(onGetSuggestionResultListener);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Toast.makeText(BNDemoMainActivity.this, "OnItemClickListener()选中：" + listData.get(position), Toast.LENGTH_SHORT).show();

                endLatitue = suggestionInfosData.get(position).pt.latitude;
                endLongLatitue = suggestionInfosData.get(position).pt.longitude;

                route_go();
                Log.i(TAG, "*****经纬度*****" + endLatitue + endLongLatitue);
            }
        });
    }

    private void initAutoCompeleted() {

        adapter = new ArrayAdapter(this, R.layout.cwp_item_main_autocomplete, listData);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher_Enum());
    }

    private void initListData() {

//        listData.add("湖南");
//        listData.add("湖南");

    }

    /**
     * *  初始化定位的相关参数
     */
    private void initLocation() {

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 通过传入的经度与纬度，然后将地图画面位置居中
     *
     * @param mLatitue
     * @param mLongLatitue
     */
    private void centerToMyLocation(double mLatitue, double mLongLatitue) {

        LatLng latLng = new LatLng(mLatitue, mLongLatitue);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);

        mBaiduMap.animateMapStatus(msu);
    }


    private void route_go() {

        routeplanToNavi();
    }

    /**
     * 初始化listener
     */
    private void initListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_go:
                        route_go();
                        break;

                    case R.id.btn_search:
                        String key = autoCompleteTextView.getText().toString();
                        suggestionSearch("株洲", key);
                        break;
                }
            }
        };

        onGetSuggestionResultListener = new OnGetSuggestionResultListener() {
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    if (res == null) {
                        Log.i(TAG, "*****res == null*****");
                    } else if (res.getAllSuggestions() == null) {
                        Log.i(TAG, "*****res.getAllSuggestions() == null*****");
                    }
                    //未找到相关结果
                    Log.i(TAG, "*****未找到相关结果*****");
                    return;

                } else if (res.getAllSuggestions().get(0) != null) {
                    Log.i(TAG, "*****111111*****");
                    //获取在线建议检索结果 , 默认截取第一个对象做为导航的参数值

                    Log.i(TAG, "*****110011*****");
                    listData = new ArrayList<>();
                    suggestionInfosData = new ArrayList<>();

                    for (SuggestionResult.SuggestionInfo suggestionInfo : res.getAllSuggestions()) {
                        Log.i(TAG, "*****11for11*****");
                        listData.add(suggestionInfo.key);
                        suggestionInfosData.add(suggestionInfo);

                        Log.i(TAG, "city" + suggestionInfo.city);
                        Log.i(TAG, "key" + suggestionInfo.key);

//                        Log.i(TAG, suggestionInfo.uid);
//                        Log.i(TAG, suggestionInfo.pt.latitude + " " + suggestionInfo.pt.longitude);
                    }

                    initAutoCompeleted();
                    printData();
                    Log.i(TAG, "*****11end11*****");
                }
            }
        };

    }

    /**
     * 用于测试
     */
    private void printData() {

        for (String s : listData) {

            Log.i(TAG, "Data数据 : " + s);
        }

    }

    /**
     * 自定义的定位接口类，继承至BDLocationListener
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //一直处于中心点的状态
            centerToMyLocation(location.getLatitude(), location.getLongitude());

            startLatitue = location.getLatitude();
            startLongLatitue = location.getLongitude();
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }

    }

    /**
     * 以下几个方法均为
     * Android Activity 的生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSuggestionSearch.destroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }


    //TextWatcher接口
    class TextWatcher_Enum implements TextWatcher {
        //文字变化前
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        //文字变化时
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (autoCompleteTextView.getText().toString().length() > 0) {
                suggestionSearch("株洲", autoCompleteTextView.getText().toString());
            }
        }

        //文字变化后
        @Override
        public void afterTextChanged(Editable s) {

        }

    }

}
