package com.hut.cwp.mcar.activitys.map;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.activitys.map.adapter.HRecyclerViewAdapter;
import com.hut.cwp.mcar.activitys.map.interfaces.MainView;
import com.hut.cwp.mcar.base.BaseActivity;
import com.hut.cwp.mcar.databinding.CwpActivityMainBinding;

import cn.bmob.v3.update.BmobUpdateAgent;


public class BNMainActivity extends BaseActivity implements MainView {


    /***
     * BNMainActivity的作用，主要是处理UI相关内容
     * 动画效果等等，其他的逻辑交互交给present
     */
    BaiduMap mBaiduMap;
    private MapView mMapView = null;

    public BNMainPresentMain present;
    public CwpActivityMainBinding Binding;

    private boolean isShowingMap = false;
    private final int ANIMATE_DURANTION = 1000;

    @Override
    protected int getLayoutId() {
        return R.layout.cwp_activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Binding = (CwpActivityMainBinding) getBinding();
        BmobUpdateAgent.update(this);//用于自动更新

        initMap();

        initSetListener();
    }

    @Override
    protected void loadData() {

        setPresent();

        //注保证adapter已经初始化过
        Binding.lyMainMap.tvAutoComplete.setAdapter(present.adapter);

    }

    @Override
    public void showMap() {
        float curTranslationX = Binding.lySlidableSlidingdown.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(Binding.lySlidableSlidingdown,
                "translationY",
                curTranslationX,
                Binding.layoutHiddenMap.getHeight());
        animator.setDuration(ANIMATE_DURANTION);
        animator.setInterpolator(new DecelerateInterpolator(2f));
        animator.start();
        isShowingMap = true;
    }

    @Override
    public void showBusiness() {
        float curTranslationX = Binding.lySlidableSlidingdown.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(Binding.lySlidableSlidingdown,
                "translationY",
                curTranslationX,
                curTranslationX - Binding.layoutHiddenMap.getHeight());
        animator.setDuration(ANIMATE_DURANTION);
        animator.setInterpolator(new DecelerateInterpolator(2f));
        animator.start();
        isShowingMap = false;
    }

    @Override
    public void showMenu() {

        Binding.lyMenu.lyMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeMenu() {

        Binding.lyMenu.lyMenu.setVisibility(View.GONE);
    }

    /**
     * 设置present
     */
    void setPresent() {
        if (present == null) {
            present = new BNMainPresentMain(BNMainActivity.this);
        }
    }

    /**
     * 初始化地图所需要的基本数据
     */
    private void initMap() {
        //获取地图控件引用 必须放在.getMap()之前
        mMapView = (MapView) findViewById(R.id.bimapView);
        mBaiduMap = mMapView.getMap();

        //缩放地图，让地图更加美观
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 为控件设置Listener
     */
    private void initSetListener() {

        Binding.lyHeader.lyDown.setOnClickListener(view -> {
            if (!isShowingMap) {
                showMap();
            } else {
                showBusiness();
            }
        });

        //返回true，实现在点击菜单半透明灰色背景时，屏蔽底层UI交互的效果
        Binding.lyMenu.lyMenu.setOnTouchListener((v, event) -> true);
        /**
         * 文章评论
         */
        Binding.lyNews.tvArticleTitle.setOnClickListener(view ->
                present.startActivityWithUrl("http://m.autohome.com.cn/news/201610/894807.html#pvareaid=2028166"));
        Binding.lyNews.tvArticleOne.setOnClickListener(view ->
                present.startActivityWithUrl("http://m.autohome.com.cn/news/201610/894799.html#pvareaid=2028166"));
        Binding.lyNews.tvArticleTwo.setOnClickListener(view ->
                present.startActivityWithUrl("http://m.autohome.com.cn/news/201610/894791.html#pvareaid=2028166"));
        Binding.lyNews.tvArticleThree.setOnClickListener(view ->
                present.startActivityWithUrl("http://m.autohome.com.cn/news/201610/894789.html#pvareaid=2028166"));
        /**
         * 地图模块
         */
        Binding.lyMainMap.itemNavigation.setOnClickListener(view ->
                mBaiduMap.clear());
        Binding.lyMainMap.itemGas.setOnClickListener(view ->
                present.searchPeriphery("加油站"));
        Binding.lyMainMap.itemPark.setOnClickListener(view ->
                present.searchPeriphery("停车场"));
        Binding.lyMainMap.itemRepair.setOnClickListener(view ->
                present.searchPeriphery("修车店"));

        Binding.lyMainMap.tvAutoComplete.addTextChangedListener(new TextWatcher_Enum());

        Binding.lyMainMap.tvAutoComplete.setOnItemClickListener((parent, view, position, id) ->
                showDestinationDialog(present.suggestionInfoList.get(position)));


        Binding.lyMainMap.btnSearch.setOnClickListener(view -> {
            if (Binding.lyMainMap.tvAutoComplete.getText().toString().trim().equals("")) {
                present.searchDestination("株洲", Binding.lyMainMap.tvAutoComplete.getHint().toString().trim());
            } else {
                present.searchDestination("株洲", Binding.lyMainMap.tvAutoComplete.getText().toString().trim());
            }
        });
        /**
         * 业务模块
         */
//        Binding.lyHListview.ivIllegalRecord.setOnClickListener(view ->
//                present.ivIllegalRecord());
//        Binding.lyHListview.ivGasPrice.setOnClickListener(view ->
//                present.gasPrice());
//        Binding.lyHListview.ivInsurance.setOnClickListener(view ->
//                present.insurance());
//        Binding.lyHListview.ivCheck.setOnClickListener(view ->
//                present.checkCar());
//        Binding.lyHListview.ivMoveNotice.setOnClickListener(view ->
//                present.moveCar());



        /**
         * 菜单模块
         */
        Binding.lyHeader.ivMenu.setOnClickListener(view ->
                showMenu());
        Binding.lyMenu.ivClose.setOnClickListener(view ->
                closeMenu());
        Binding.lyMenu.lyAbout.setOnClickListener(view ->
                present.about());
        Binding.lyMenu.lyFeedback.setOnClickListener(view ->
                present.feedback());
        Binding.lyMenu.lyLoginOut.setOnClickListener(view ->
                present.loginOut());
        Binding.lyMenu.lyMycar.setOnClickListener(view ->
                present.myCar());
        Binding.lyMenu.lyShare.setOnClickListener(view ->
                present.Share());
        Binding.lyMenu.lyUpdate.setOnClickListener(view ->
                present.update());
        Binding.lyMenu.lyUser.setOnClickListener(view ->
                present.userInfo());


        LinearLayoutManager manager = new LinearLayoutManager(BNMainActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        Binding.hRecyclerView.setLayoutManager(manager);
        Binding.hRecyclerView.setAdapter(new HRecyclerViewAdapter(BNMainActivity.this));


//        Binding.hRecyclerView.getChildAt(0).setOnClickListener( view ->
//                present.ivIllegalRecord());
//        Binding.hRecyclerView.getChildAt(1).setOnClickListener( view ->
//                present.gasPrice());
//        Binding.hRecyclerView.getChildAt(2).setOnClickListener( view ->
//                present.insurance());
//        Binding.hRecyclerView.getChildAt(3).setOnClickListener( view ->
//                present.checkCar());
//        Binding.hRecyclerView.getChildAt(4).setOnClickListener( view ->
//                present.moveCar());
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
        present.mSuggestionSearch.destroy();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭定位
        mBaiduMap.setMyLocationEnabled(false);
    }


    /**
     * 内部接口类，用于监听自动输入框的输入事件
     */
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
            if (Binding.lyMainMap.tvAutoComplete.getText().toString().length() > 0) {
                present.searchDestination("株洲", Binding.lyMainMap.tvAutoComplete.getText().toString());
            }
        }

        //文字变化后
        @Override
        public void afterTextChanged(Editable s) {
        }

    }


    /**
     * @param suggestionInfo 以对话框的形式展示搜索结果
     */
    void showDestinationDialog(final SuggestionResult.SuggestionInfo suggestionInfo) {

        String content =
                "名称:" + "\t\t\t\t" + suggestionInfo.district + "\n"
                        + "地址详情:" + "" + suggestionInfo.key + "\n";
        Dialog dialog = new AlertDialog.Builder(BNMainActivity.this)
                .setTitle(R.string.go_to_here)//设置标题
                .setMessage(content)//设置提示内容
                //确定按钮
                .setPositiveButton(R.string.ok, (dialog1, which) -> {
                    present.endLatitue = suggestionInfo.pt.latitude;
                    present.endLongLatitue = suggestionInfo.pt.longitude;

                    present.goTo(null);
                })
                //取消按钮
                .setNegativeButton(R.string.cancle, (dialog12, which) -> {
                })
                .create();//创建对话框
        dialog.show();//显示对话框
    }


}

