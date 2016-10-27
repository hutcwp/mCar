package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.base.application.MyApplication;
import com.hut.cwp.mcar.base.utils.DisplayUtil;
import com.hut.cwp.mcar.cwp.clazz.BaiduMapLocal;
import com.hut.cwp.mcar.cwp.clazz.BaiduMapNavi;
import com.hut.cwp.mcar.cwp.clazz.BaiduMapPoiSearch;
import com.hut.cwp.mcar.cwp.view.HListView;
import com.hut.cwp.mcar.way.activitys.InfoCarActivity;
import com.hut.cwp.mcar.way.activitys.LoginActivity;
import com.hut.cwp.mcar.way.activitys.RepasswordActivity;
import com.hut.cwp.mcar.zero.activity.FeedbackActivity;
import com.hut.cwp.mcar.zero.activity.IllegalQueryActivity;
import com.hut.cwp.mcar.zero.activity.IllegalResultActivity;
import com.hut.cwp.mcar.zero.activity.MoveCarActivity;
import com.hut.cwp.mcar.zero.activity.OilCityActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.update.BmobUpdateAgent;


public class BNDemoMainActivity extends Activity {

    //**************工具、全局**************
    private final static String TAG = "BNDemoMainActivity";
    private long activityExitTime;

    private AdapterView.OnItemClickListener autoCompleteTextViewOnItemClickListener;
    //**************************************

    //**************地图********************
    public static List<Activity> activityList = new LinkedList<>();
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    private List<String> listData;
    private ArrayAdapter<String> adapter;

    private BaiduMap mBaiduMap;
    private MapView mMapView = null;

    private Button btn_search;
    private TextView navi, gas, repair, stop;

    private View.OnClickListener onClickListener;

    private SuggestionSearch mSuggestionSearch;
    private AutoCompleteTextView autoCompleteTextView;
    private List<SuggestionResult.SuggestionInfo> suggestionInfosData;
    //*******************************

    //********HlistviewItem(主服务项)************
    private ImageView item_check;
    private ImageView item_notice;
    private ImageView item_ticket;
    private ImageView item_tag;
    private ImageView item_safe;
    private View.OnClickListener itemOnClickListener;
    private View.OnTouchListener itemOnTouchListener;
    //*********************************************

    //**************菜单***************************
    private boolean isMenuOpen;
    private ImageView menu_close;
    private LinearLayout menu_mycar;
    private LinearLayout menu_exit;
    private LinearLayout menu_feedback;
    private LinearLayout menu_about;
    private LinearLayout menu_share;
    private LinearLayout menu_update;
    private LinearLayout menu_user;
    private View.OnClickListener menuOnClickListener;
    //*********************************************

    //******************地图数据*******************
    private boolean isMapShow = false;

    private double endLatitue;
    private double endLongLatitue;

    private BaiduMapLocal baiduMapLocal;
    private BaiduMapNavi baiduMapNavi;
    private BaiduMapPoiSearch poiSearch;

    private HListView hListView;
    private ImageView img_btn_down, img_btn_menu;
    private LinearLayout ll_main_menu;
    private FrameLayout ll_map;

    //全能地图点击事件二级菜单监听
    private View.OnClickListener textItemOnClickListener;
    //路径导航的建议搜索监听
    private OnGetSuggestionResultListener onGetSuggestionResultListener;

    //**********下滑条************
    private ImageButton btn;
    boolean isAsycMoveBtnFinish = true;
    boolean isAsycMoveFinish = true;

    private LinearLayout layout;
    private LinearLayout.LayoutParams lp;

    private int MOVE_WIDTH = 20;
    private int pauseTime = 10;
    private int mTopMargin = 0;
    private int mMaxTopMargin = 400;

    private float slidingDownStartY;
    private float slidingDownCurrentY;
    private float slidingDownSY;

    //****************************

    //***********文章*************

    private TextView article_title;
    private TextView article_one;
    private TextView article_two;
    private TextView article_three;

    private View.OnClickListener article_OnClickListener;
    private List<TextView> mapItem;
    //****************************

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log_i("001");
        activityList.add(this);
        log_i("002");
        setContentView(R.layout.cwp_layout_main);
        log_i("0030");

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        log_i("004");
        mMaxTopMargin = DisplayUtil.dip2px(BNDemoMainActivity.this, 420);

        MOVE_WIDTH = DisplayUtil.dip2px(BNDemoMainActivity.this, 5);


        log_i("005");
        initMap();
        log_i("006");
        initPanel();

        log_i("007");
        BmobUpdateAgent.update(this);//用于自动更新

        log_i("008");
    }

    /**
     * 初始化地图所需要的基本数据
     */
    private void initMap() {
        //获取地图控件引用 必须放在.getMap()之前
        mMapView = (MapView) findViewById(R.id.bimapView);
        mBaiduMap = mMapView.getMap();
        mSuggestionSearch = SuggestionSearch.newInstance();

        baiduMapLocal = new BaiduMapLocal(BNDemoMainActivity.this, mBaiduMap);
        baiduMapNavi = new BaiduMapNavi(BNDemoMainActivity.this);
        poiSearch = new BaiduMapPoiSearch(mBaiduMap, BNDemoMainActivity.this);

        hListView = (HListView) findViewById(R.id.hListView);

        listData = new ArrayList<>();
        suggestionInfosData = new ArrayList<>();
        mapItem = new ArrayList<>();


        //缩放地图，让地图更加美观
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);

        initFindView();

        initListener();

        initSetListener();

        initAutoCompeleted();
        baiduMapLocal.initLocation();


        mapItem.add(navi);
        mapItem.add(gas);
        mapItem.add(repair);
        mapItem.add(stop);

        // 打开log开关
        BNOuterLogUtil.setLogSwitcher(true);
        if (baiduMapNavi.initDirs()) {
            baiduMapNavi.initNavi();
        }
    }

    /**
     * 客户端调用方法
     * 在线建议查询
     * 实时导航
     */
    private void suggestionSearch(String City, String Key) {

        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(Key)
                .city(City));

    }

    public void route_go(LatLng target) {

        if (target != null) {
            endLatitue = target.latitude;
            endLongLatitue = target.longitude;

            routeplanToNavi();

        } else {
            Toast.makeText(BNDemoMainActivity.this, "暂时为找到该点的具体位置", Toast.LENGTH_SHORT).show();
        }

    }

    public void route_go() {

        routeplanToNavi();

    }


    /**
     * 一系列初始化findViewById的方法
     */
    private void initFindView() {

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);


        ll_main_menu = (LinearLayout) findViewById(R.id.layout_main_menu);
        ll_map = (FrameLayout) findViewById(R.id.ll_map);

        btn_search = (Button) findViewById(R.id.btn_search);
        img_btn_down = (ImageView) findViewById(R.id.down);
        img_btn_menu = (ImageView) findViewById(R.id.menu);

        article_title = (TextView) findViewById(R.id.article_title);
        article_one = (TextView) findViewById(R.id.article_one);
        article_two = (TextView) findViewById(R.id.article_two);
        article_three = (TextView) findViewById(R.id.article_three);

        navi = (TextView) findViewById(R.id.cwp_layout_main_b_navigation);
        gas = (TextView) findViewById(R.id.cwp_layout_main_b_gas);
        repair = (TextView) findViewById(R.id.cwp_layout_main_b_repair);
        stop = (TextView) findViewById(R.id.cwp_layout_main_b_stop);

        item_safe = (ImageView) findViewById(R.id.img_safe);
        item_check = (ImageView) findViewById(R.id.img_check);
        item_notice = (ImageView) findViewById(R.id.img_notice);
        item_tag = (ImageView) findViewById(R.id.img_tag);
        item_ticket = (ImageView) findViewById(R.id.img_ticket);

        menu_close = (ImageView) findViewById(R.id.menu_close);
        menu_mycar = (LinearLayout) findViewById(R.id.menu_mycar);
        menu_about = (LinearLayout) findViewById(R.id.menu_about);
        menu_share = (LinearLayout) findViewById(R.id.menu_share);
        menu_feedback = (LinearLayout) findViewById(R.id.menu_feedback);
        menu_exit = (LinearLayout) findViewById(R.id.menu_exit);
        menu_user = (LinearLayout) findViewById(R.id.menu_user);
        menu_update = (LinearLayout) findViewById(R.id.menu_update);
    }

    /**
     * 一系列初始化findViewById的方法
     */
    private void initListener() {
        menuOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.menu_close:
                        menuClose();
                        break;
                    case R.id.menu_mycar:
                        if (MyApplication.getLandState()==MyApplication.HAD_LANDED) {
                            startActivity(new Intent(BNDemoMainActivity.this, InfoCarActivity.class));
                        } else if (MyApplication.getLandState()==MyApplication.NO_LAND){
                            Intent intent=new Intent(BNDemoMainActivity.this, LoginActivity.class);
                            intent.putExtra("TAG","FROM_BNDemoMainActivity");
                            startActivity(intent);
                        }
                        break;

                    case R.id.menu_user:
                        if (MyApplication.getLandState()==MyApplication.HAD_LANDED) {
                            startActivity(new Intent(BNDemoMainActivity.this, RepasswordActivity.class));
                        } else if (MyApplication.getLandState()==MyApplication.NO_LAND){
                            Intent intent=new Intent(BNDemoMainActivity.this, LoginActivity.class);
                            intent.putExtra("TAG","FROM_BNDemoMainActivity");
                            startActivity(intent);
                        }
                        break;

                    case R.id.menu_update:
                        BmobUpdateAgent.update(BNDemoMainActivity.this);//用于自动更新
                        break;

                    case R.id.menu_about:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_About.class));
                        break;
                    case R.id.menu_share:
                        onClickShare(menu_share);
                        break;
                    case R.id.menu_feedback:
                        startActivity(new Intent(BNDemoMainActivity.this, FeedbackActivity.class));
                        break;
                    case R.id.menu_exit://退出当前账号
                        if (MyApplication.getLandState()==MyApplication.HAD_LANDED) {
                            MyApplication.setLandState(MyApplication.NO_LAND);
                            MyApplication.setUsername("");
                            menuClose();
                            Toast.makeText(BNDemoMainActivity.this, "已经退出当前账号", Toast.LENGTH_SHORT).show();
                            Log.d("测试","已经退出当前账号");
                        } else if(MyApplication.getLandState()==MyApplication.NO_LAND) {
                            Toast.makeText(BNDemoMainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                }

            }
        };

        textItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cwp_layout_main_b_navigation:

                        textItemOnClick(R.id.cwp_layout_main_b_navigation);
                        mBaiduMap.clear();
                        break;

                    case R.id.cwp_layout_main_b_repair:

                        textItemOnClick(R.id.cwp_layout_main_b_repair);
                        poiSearch.boundSearch(baiduMapLocal, "修车店");
                        break;

                    case R.id.cwp_layout_main_b_gas:

                        textItemOnClick(R.id.cwp_layout_main_b_gas);
                        poiSearch.boundSearch(baiduMapLocal, "加油站");
                        break;

                    case R.id.cwp_layout_main_b_stop:

                        textItemOnClick(R.id.cwp_layout_main_b_stop);
                        poiSearch.boundSearch(baiduMapLocal, "停车场");
                        break;

                    default:
                        break;
                }
            }
        };


        itemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_check:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_news.class));
                        break;

                    case R.id.img_notice:
                        startActivity(new Intent(BNDemoMainActivity.this, MoveCarActivity.class));
                        break;

                    case R.id.img_safe:
                        startActivity(new Intent(BNDemoMainActivity.this, IllegalResultActivity.class));
                        break;

                    case R.id.img_tag:
                        startActivity(new Intent(BNDemoMainActivity.this, OilCityActivity.class));
                        break;

                    case R.id.img_ticket:
                        startActivity(new Intent(BNDemoMainActivity.this, IllegalQueryActivity.class));
                        break;

                    default:
                }

            }
        };


        article_OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.article_title:

                        startActivity(new Intent(BNDemoMainActivity.this, Activity_news.class));
                        break;

                    case R.id.article_one:

                        startActivity(new Intent(BNDemoMainActivity.this, Activity_news.class));
                        break;

                    case R.id.article_two:

                        startActivity(new Intent(BNDemoMainActivity.this, Activity_news.class));
                        break;

                    case R.id.article_three:

                        startActivity(new Intent(BNDemoMainActivity.this, Activity_news.class));
                        break;

                    default:
                        break;

                }

            }
        };


        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.menu:
                        menuOpen();
                        break;

                    case R.id.down:

                        if (isMapShow) {

                            img_btn_down.setImageResource(R.drawable.cwp_main_img_down);
                            if (isAsycMoveBtnFinish && isAsycMoveFinish) {
                                new AsynMoveBtn().execute(new Integer[]{-MOVE_WIDTH});// 正数收缩,向上
                                isMapShow = false;
                            }
                        } else {

                            img_btn_down.setImageResource(R.drawable.cwp_main_img_up);
                            if (isAsycMoveBtnFinish && isAsycMoveFinish) {
                                new AsynMoveBtn().execute(new Integer[]{MOVE_WIDTH});// 正数展开,向下
                                isMapShow = true;
                            }
                        }
                        break;

                    case R.id.btn_search:

                        if (autoCompleteTextView.getText().toString().trim().equals("")) {

                            suggestionSearch("株洲", autoCompleteTextView.getHint().toString().trim());
                        } else {

                            suggestionSearch("株洲", autoCompleteTextView.getText().toString().trim());
                        }
                        break;
                }
            }
        };

        itemOnTouchListener = new View.OnTouchListener() {

            float startX;
            float currentX;
            float sX;
            int duration = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        startX = event.getRawX();
                        sX = startX;

                        Log.e("TestForHListView", "ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:

                        currentX = event.getRawX();

                        float dx = currentX - startX;

                        duration -= dx;
                        hListView.scroll(duration);

                        startX = currentX;

                        Log.e("TestForHListView", "ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:

                        if (Math.abs(currentX - sX) > 8) {
                            if (duration > 150) {

                                int s = (int) (hListView.getScreenWidth() * (45.0 / 108.0));

                                hListView.scroll(s);
                                duration = s;
                            } else {
                                hListView.scroll(0);
                                duration = 0;
                            }

                            return true;
                        }
                        break;
                }
                return false;
            }
        };


        autoCompleteTextViewOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
//                Toast.makeText(BNDemoMainActivity.this, "前往：" + listData.get(position), Toast.LENGTH_SHORT).show();

                showDialog(suggestionInfosData.get(position));


                Log.i(TAG, "*****经纬度*****" + endLatitue + endLongLatitue);
            }
        };

        onGetSuggestionResultListener = new OnGetSuggestionResultListener() {
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    if (res == null) {
                    } else if (res.getAllSuggestions() == null) {
                    }
                    //未找到相关结果
                    Log.i(TAG, "*****未找到相关结果*****");
                    return;

                } else if (res.getAllSuggestions().get(0) != null) {
                    //获取在线建议检索结果 , 默认截取第一个对象做为导航的参数值
                    Log.i(TAG, "*****110011*****");
                    listData = new ArrayList<>();
                    suggestionInfosData = new ArrayList<>();
                    for (SuggestionResult.SuggestionInfo suggestionInfo : res.getAllSuggestions()) {
                        //suggestionInfo.key 经纬度
                        listData.add(suggestionInfo.key);
                        suggestionInfosData.add(suggestionInfo);

                        Log.i(TAG, "city" + suggestionInfo.city);
                        Log.i(TAG, "key" + suggestionInfo.key);
//                        Log.i(TAG, suggestionInfo.uid);
//                        Log.i(TAG, suggestionInfo.pt.latitude + " " + suggestionInfo.pt.longitude);
                    }
//                    Toast.makeText(BNDemoMainActivity.this, "list" + listData.size(), Toast.LENGTH_SHORT).show();
                    initAutoCompeleted();
                    autoCompleteTextView.showDropDown();
                    printData();
                }
            }
        };
    }

    private void textItemOnClick(int id) {

        for (int i = 0; i < mapItem.size(); i++) {

            if (mapItem.get(i).getId() == id) {
                mapItem.get(i).setTextColor(Color.parseColor("#7bb5ed"));
            } else {
                mapItem.get(i).setTextColor(Color.parseColor("#6ad865"));
            }
        }

    }

    private void menuOpen() {
        if (ll_main_menu.getVisibility() == View.GONE) {

            ll_main_menu.setVisibility(View.VISIBLE);

            ll_main_menu.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

        }
    }

    private void menuClose() {
        if (ll_main_menu.getVisibility() == View.VISIBLE) {

            ll_main_menu.setVisibility(View.GONE);

        }
    }

    private void showDialog(final SuggestionResult.SuggestionInfo suggestionInfo) {

        String content =
                "名称:" + "\t\t\t\t" + suggestionInfo.district + "\n"
                        + "地址详情:" + "" + suggestionInfo.key + "\n";

        Dialog dialog = new AlertDialog.Builder(BNDemoMainActivity.this)
                .setTitle("出发去该地")//设置标题
                .setMessage(content)//设置提示内容
                //确定按钮
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endLatitue = suggestionInfo.pt.latitude;
                        endLongLatitue = suggestionInfo.pt.longitude;

                        route_go();
                        //                        finish();
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

    /**
     * 为控件设置Listener
     */
    private void initSetListener() {

        btn_search.setOnClickListener(onClickListener);

        img_btn_menu.setOnClickListener(onClickListener);
        img_btn_down.setOnClickListener(onClickListener);

        article_title.setOnClickListener(article_OnClickListener);
        article_one.setOnClickListener(article_OnClickListener);
        article_two.setOnClickListener(article_OnClickListener);
        article_three.setOnClickListener(article_OnClickListener);

        navi.setOnClickListener(textItemOnClickListener);
        repair.setOnClickListener(textItemOnClickListener);
        gas.setOnClickListener(textItemOnClickListener);
        stop.setOnClickListener(textItemOnClickListener);

        item_ticket.setOnClickListener(itemOnClickListener);
        item_notice.setOnClickListener(itemOnClickListener);
        item_safe.setOnClickListener(itemOnClickListener);
        item_tag.setOnClickListener(itemOnClickListener);
        item_check.setOnClickListener(itemOnClickListener);

        item_ticket.setOnTouchListener(itemOnTouchListener);
        item_notice.setOnTouchListener(itemOnTouchListener);
        item_safe.setOnTouchListener(itemOnTouchListener);
        item_tag.setOnTouchListener(itemOnTouchListener);
        item_check.setOnTouchListener(itemOnTouchListener);

        menu_close.setOnClickListener(menuOnClickListener);
        menu_mycar.setOnClickListener(menuOnClickListener);
        menu_user.setOnClickListener(menuOnClickListener);
        menu_update.setOnClickListener(menuOnClickListener);
        menu_share.setOnClickListener(menuOnClickListener);
        menu_feedback.setOnClickListener(menuOnClickListener);
        menu_exit.setOnClickListener(menuOnClickListener);
        menu_about.setOnClickListener(menuOnClickListener);

        mSuggestionSearch.setOnGetSuggestionResultListener(onGetSuggestionResultListener);
        autoCompleteTextView.setOnItemClickListener(autoCompleteTextViewOnItemClickListener);

        autoCompleteTextView.addTextChangedListener(new TextWatcher_Enum());
    }

    private void initAutoCompeleted() {

        adapter = new ArrayAdapter(this, R.layout.cwp_item_main_autocomplete, listData);
        autoCompleteTextView.setAdapter(adapter);

    }

    /**
     * 具体实现方法
     * 导航以及路线规划
     */
    private void routeplanToNavi() {

        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;

        sNode = new BNRoutePlanNode(baiduMapLocal.getmCurrentLongLatitue(), baiduMapLocal.getmCurentLatitue(), null, null, CoordinateType.BD09LL);
        eNode = new BNRoutePlanNode(endLongLatitue, endLatitue, null, null, CoordinateType.BD09LL);

        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    /**
     * 用于测试 的Log方法
     */
    private void printData() {

        for (String s : listData) {

            Log.i(TAG, "Data数据 : " + s);
        }

    }

    private void log_i(String error) {

        Log.i("test", error);
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
//        if (!mLocationClient.isStarted()) {
//            mLocationClient.start();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭定位
        mBaiduMap.setMyLocationEnabled(false);
//        mLocationClient.stop();
    }

    /**
     * 按两次back 退出程序两次间隔时间节点
     * 设置为2000 ms
     */
    @Override
    public void onBackPressed() {
        if (ll_main_menu.getVisibility() == View.VISIBLE) {

            ll_main_menu.setVisibility(View.GONE);
            isMenuOpen = false;
            return;
        } else if ((System.currentTimeMillis() - activityExitTime) > 2000) {

            Toast.makeText(BNDemoMainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            activityExitTime = System.currentTimeMillis();

            Log.i("JJJ", "activityExitTime: " + activityExitTime + "currenttimr" + System.currentTimeMillis());
            return;

        }
        this.finish();
    }

    /**
     * *  方法
     */
    public void initPanel() {

        btn = (ImageButton) findViewById(R.id.btn_dropdown);

        layout = (LinearLayout) findViewById(R.id.layout_slidable_slidingdown);

        lp = (LinearLayout.LayoutParams) layout.getLayoutParams();

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("MOVE", "***************************");
                        slidingDownStartY = event.getRawY();
                        slidingDownSY = slidingDownStartY;
                        Log.e("MOVE", "**slidingDownStartY**" + slidingDownStartY);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        slidingDownCurrentY = event.getRawY();
                        Log.e("MOVE", "**slidingDownCurrentY**" + slidingDownCurrentY + "**slidingDownStartY**" + slidingDownStartY);
                        float dy = slidingDownCurrentY - slidingDownStartY;

                        scroll(dy);

                        slidingDownStartY = slidingDownCurrentY;

                        Log.e("MOVE", "**dy**" + dy + "**slidingDownCurrentY**" + slidingDownCurrentY + "**slidingDownStartY**" + slidingDownStartY);
                        break;
                    case MotionEvent.ACTION_UP:

                        if (Math.abs(slidingDownCurrentY - slidingDownSY) > 8) {
                            if (mTopMargin < mMaxTopMargin && mTopMargin > (mMaxTopMargin / 2)) {
                                //下滑
                                new AsynMove().execute(new Integer[]{MOVE_WIDTH});// 正数展开,向下
                                isMapShow = true;

                            } else if ((mTopMargin > 0 && mTopMargin <= (mMaxTopMargin / 2))) {
                                //上滑
                                new AsynMove().execute(new Integer[]{-MOVE_WIDTH});// 正数展开,向下
                                isMapShow = false;

                            }
                            return true;
                        }

                        break;
                }
                return false;

            }
        });

    }

    public void onClickShare(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "要分享的内容!!!");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享到"));

    }

    public void scroll(float positionOffset) {

        if (mTopMargin < mMaxTopMargin && positionOffset > 0) {
            Log.e("scroll", "Down" + mTopMargin);
            if ((mTopMargin + positionOffset) <= mMaxTopMargin) {
                layoutScroll(positionOffset);
            }
        } else if (mTopMargin > 0 && positionOffset < 0) {
            Log.e("scroll", "Up" + mTopMargin);
            if ((mTopMargin + positionOffset) > 0) {
                layoutScroll(positionOffset);
            }
        }
    }

    private void layoutScroll(float offset) {
        mTopMargin += offset;
        lp.topMargin = mTopMargin;
        layout.setLayoutParams(lp);
    }

    /**
     * 内部接口类
     */
    //TextWatcher接口
    private class TextWatcher_Enum implements TextWatcher {
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

    private class DemoRoutePlanListener implements RoutePlanListener {

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

    private class AsynMove extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int times = 0;
            isAsycMoveFinish = false;
            if (mTopMargin < mMaxTopMargin / 2) {

                if (mTopMargin % Math.abs(params[0]) == 0)// 整除
                    times = mTopMargin / Math.abs(params[0]);
                else
                    // 有余数
                    times = mTopMargin / Math.abs(params[0]) + 1;
                Log.e("doInBackground", "Up" + times);
            } else if (mTopMargin >= mMaxTopMargin / 2) {
                if ((mMaxTopMargin - mTopMargin) % Math.abs(params[0]) == 0)// 整除
                    times = mTopMargin / Math.abs(params[0]);
                else
                    // 有余数
                    times = (mMaxTopMargin - mTopMargin) / Math.abs(params[0]) + 1;
                Log.e("doInBackground", "Down" + times);
            }

            for (int i = 0; i < times; i++) {
                publishProgress(params);
                try {
                    Thread.sleep(Math.abs(pauseTime));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (isMapShow) {
                mTopMargin = mMaxTopMargin;

            } else {
                mTopMargin = 0;
            }

            isAsycMoveFinish = true;
            lp.topMargin = mTopMargin;
            layout.setLayoutParams(lp);

        }

        @Override
        protected void onProgressUpdate(Integer... params) {

            layoutScroll(params[0]);
        }
    }

    private class AsynMoveBtn extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int times = 0;
            isAsycMoveBtnFinish = false;
            if (params[0] > 0) {

                if ((mMaxTopMargin - mTopMargin) % Math.abs(params[0]) == 0)// 整除
                    times = (mMaxTopMargin - mTopMargin) / Math.abs(params[0]);
                else
                    // 有余数
                    times = (mMaxTopMargin - mTopMargin) / Math.abs(params[0]) + 1;
                Log.e("doAsynMoveBtn", "Down" + times);
                Log.e("doAsynMoveBtn", "Down" + times);

            } else if (params[0] < 0) {
                if (mTopMargin % Math.abs(params[0]) == 0)// 整除
                    times = mTopMargin / Math.abs(params[0]);
                else
                    // 有余数
                    times = mTopMargin / Math.abs(params[0]) + 1;
                Log.e("doAsynMoveBtn", "Up" + times);
            }

            for (int i = 0; i < times; i++) {
                publishProgress(params);
                try {
                    Thread.sleep(Math.abs(pauseTime));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (isMapShow) {
                mTopMargin = mMaxTopMargin;

            } else {
                mTopMargin = 0;
            }

            lp.topMargin = mTopMargin;
            layout.setLayoutParams(lp);
            isAsycMoveBtnFinish = true;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            layoutScroll(params[0]);
        }
    }


}

