package com.hut.cwp.mcar.cwp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.hut.cwp.mcar.cwp.clazz.BaiduMapLocal;
import com.hut.cwp.mcar.cwp.clazz.BaiduMapNavi;
import com.hut.cwp.mcar.cwp.clazz.BaiduMapPoiSearch;
import com.hut.cwp.mcar.cwp.view.HListView;
import com.hut.cwp.mcar.zero.view.OilCityActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class BNDemoMainActivity extends Activity {

    public static List<Activity> activityList = new LinkedList<>();

    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private final static String TAG = "MainActivity_a";

    private long exitTime;

    private BaiduMap mBaiduMap;
    private MapView mMapView = null;

    private AutoCompleteTextView autoCompleteTextView;
    private List<String> listData;
    private ArrayAdapter<String> adapter;
    private SuggestionSearch mSuggestionSearch;
    private List<SuggestionResult.SuggestionInfo> suggestionInfosData;

    //**************Item*****************
    private ImageView item_check;
    private ImageView item_notice;
    private ImageView item_ticket;
    private ImageView item_tag;
    private ImageView item_safe;

    //**********************

    //**************菜单*****************
    private boolean isMenuOpen;
    private ImageView menu_close;
    private LinearLayout menu_mycar;
    private LinearLayout menu_exit;
    private LinearLayout menu_feedback;
    private LinearLayout menu_about;
    private LinearLayout menu_share;
    private LinearLayout menu_update;
    private LinearLayout menu_user;

    //**********************

    private double endLatitue;
    private double endLongLatitue;

    private Button btn_go;
    private Button btn_search;

    private TextView navi, gas, repair, stop;

    private ImageView img_btn_down, img_btn_menu;
    private LinearLayout ll_main_menu;
    private FrameLayout ll_map;


    private View.OnClickListener menuOnClickListener;
    private View.OnClickListener itemOnClickListener;
    private View.OnClickListener onClickListener;
    private View.OnClickListener textItemOnClickListener;
    private View.OnTouchListener itemOnTouchListener;

    private OnGetSuggestionResultListener onGetSuggestionResultListener;
    private AdapterView.OnItemClickListener autoCompleteTextViewOnItemClickListener;

    private BaiduMapLocal baiduMapLocal;
    private BaiduMapNavi baiduMapNavi;
    private BaiduMapPoiSearch poiSearch;

    private HListView hListView;


    //**********下滑条************
    private ImageButton btn;
    private TextView text;

    private LinearLayout layout;
    private LinearLayout layout_a;
    private LinearLayout.LayoutParams lp;

    private int MOVE_WIDTH = 20;

    private int mTopMargin = 0;
    private int mMaxTopMargin = 400;

    private float startY;
    private float currentY;
    private float sY;

    boolean isMapShow = false;

    //****************************

    //***********文章*************

    TextView article_title;
    TextView article_one;
    TextView article_two;
    TextView article_three;

    View.OnClickListener article_OnClickListener;
    //****************************

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(this);

        log_i("001");
        setContentView(R.layout.cwp_layout_main);
        log_i("002");

       //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        mMaxTopMargin = dipTopx(BNDemoMainActivity.this, 400);
        MOVE_WIDTH = dipTopx(BNDemoMainActivity.this, 10);

        initMap();

        initPanel();

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
        //缩放地图，让地图更加美观
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        log_i("003");
        initFindView();
        log_i("004");
        initListener();
        log_i("005");
        initSetListener();
        log_i("006");
        initAutoCompeleted();
        baiduMapLocal.initLocation();

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


    public int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 一系列初始化的方法
     */
    private void initFindView() {

        btn_go = (Button) findViewById(R.id.btn_go);
        btn_search = (Button) findViewById(R.id.btn_search);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        ll_main_menu = (LinearLayout) findViewById(R.id.layout_main_menu);
        ll_map = (FrameLayout) findViewById(R.id.ll_map);


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

    private void initListener() {
        menuOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.menu_close:
                        if (ll_main_menu.getVisibility() == View.VISIBLE) {

                            ll_main_menu.setVisibility(View.GONE);
                            isMenuOpen = false;
                        }
                        break;
                    case R.id.menu_mycar:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_myCar.class));
                        break;

                    case R.id.menu_user:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_User.class));
                        break;

                    case R.id.menu_update:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_Update.class));
                        break;

                    case R.id.menu_about:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_About.class));
                        break;

                    case R.id.menu_share:
                        onClickShare(menu_share);
//                        shareText(menu_share);

                        break;
                    case R.id.menu_feedback:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_feedBack.class));

                        break;
                    case R.id.menu_exit:

                        BNDemoMainActivity.this.finish();

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
                        navi.setTextColor(Color.RED);
                        gas.setTextColor(Color.parseColor("#6ad865"));
                        repair.setTextColor(Color.parseColor("#6ad865"));
                        stop.setTextColor(Color.parseColor("#6ad865"));

                        break;

                    case R.id.cwp_layout_main_b_repair:

                        repair.setTextColor(Color.RED);
                        gas.setTextColor(Color.parseColor("#6ad865"));
                        stop.setTextColor(Color.parseColor("#6ad865"));
                        navi.setTextColor(Color.parseColor("#6ad865"));
                        poiSearch.boundSearch(baiduMapLocal, "修车店");
                        break;

                    case R.id.cwp_layout_main_b_gas:

                        gas.setTextColor(Color.RED);
                        stop.setTextColor(Color.parseColor("#6ad865"));
                        repair.setTextColor(Color.parseColor("#6ad865"));
                        navi.setTextColor(Color.parseColor("#6ad865"));

                        poiSearch.boundSearch(baiduMapLocal, "加油站");
                        break;

                    case R.id.cwp_layout_main_b_stop:
                        stop.setTextColor(Color.RED);
                        gas.setTextColor(Color.parseColor("#6ad865"));
                        repair.setTextColor(Color.parseColor("#6ad865"));
                        navi.setTextColor(Color.parseColor("#6ad865"));
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
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_news.class));
                        break;

                    case R.id.img_safe:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_news.class));
                        break;

                    case R.id.img_tag:
                        startActivity(new Intent(BNDemoMainActivity.this, OilCityActivity.class));
                        break;

                    case R.id.img_ticket:
                        startActivity(new Intent(BNDemoMainActivity.this, Activity_news.class));
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
                        if (ll_main_menu.getVisibility() == View.GONE) {

                            isMenuOpen = true;

                            ll_main_menu.setVisibility(View.VISIBLE);

                            ll_main_menu.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return true;
                                }
                            });

                        }
                        break;

                    case R.id.down:

                        if (isMapShow) {

                            new AsynMoveBtn().execute(new Integer[]{-MOVE_WIDTH});// 正数收缩,向上
                            isMapShow = false;
                        } else {

                            new AsynMoveBtn().execute(new Integer[]{MOVE_WIDTH});// 正数展开,向下
                            isMapShow = true;
                        }

                        break;

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
                                int s = hListView.getScreenWidth() / 4 + 80;
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
                Toast.makeText(BNDemoMainActivity.this, "OnItemClickListener()选中：" + listData.get(position), Toast.LENGTH_SHORT).show();

                endLatitue = suggestionInfosData.get(position).pt.latitude;
                endLongLatitue = suggestionInfosData.get(position).pt.longitude;
                route_go();
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

                    initAutoCompeleted();
                    printData();
                }
            }
        };
    }

    private void initSetListener() {

        btn_go.setOnClickListener(onClickListener);
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

    }

    private void initAutoCompeleted() {

        adapter = new ArrayAdapter(this, R.layout.cwp_item_main_autocomplete, listData);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher_Enum());
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
        } else if ((System.currentTimeMillis() - exitTime) > 2000) {

            Toast.makeText(BNDemoMainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();

            Log.i("JJJ", "exitTime: " + exitTime + "currenttimr" + System.currentTimeMillis());
            return;

        }
        this.finish();
    }

    /**
     * *  方法
     */
    public void initPanel() {

        btn = (ImageButton) findViewById(R.id.btn);
        text = (TextView) findViewById(R.id.text);

        layout_a = (LinearLayout) findViewById(R.id.one);
        layout = (LinearLayout) findViewById(R.id.two_layout);

        lp = (LinearLayout.LayoutParams) layout.getLayoutParams();

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BNDemoMainActivity.this, "OnClick", Toast.LENGTH_SHORT).show();
            }
        });

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("MOVE", "***************************");
                        startY = event.getRawY();
                        sY = startY;
                        Log.e("MOVE", "**startY**" + startY);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        currentY = event.getRawY();
                        Log.e("MOVE", "**currentY**" + currentY + "**startY**" + startY);
                        float dy = currentY - startY;

                        scroll(dy);

                        startY = currentY;

                        Log.e("MOVE", "**dy**" + dy + "**currentY**" + currentY + "**startY**" + startY);
                        break;
                    case MotionEvent.ACTION_UP:

                        if (Math.abs(currentY - sY) > 8) {

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


    //分享文字
    public void shareText(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "要分享的内容");
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "标题：分享到"));
    }

    public void scroll(float positionOffset) {

        if (mTopMargin < mMaxTopMargin && positionOffset > 0) {
            Log.e("scroll", "Down" + mTopMargin);

            layoutScroll(positionOffset);
        } else if (mTopMargin > 0 && positionOffset < 0) {
            Log.e("scroll", "Up" + mTopMargin);

            layoutScroll(positionOffset);
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
                    Thread.sleep(Math.abs(24));
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
                    Thread.sleep(Math.abs(24));
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

        }

        @Override
        protected void onProgressUpdate(Integer... params) {

            layoutScroll(params[0]);

        }
    }


}

