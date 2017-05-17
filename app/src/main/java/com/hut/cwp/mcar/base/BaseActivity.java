package com.hut.cwp.mcar.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hut.cwp.mcar.utils.ProxyLodingProgress;

/**
 * Created by hutcwp on 2017/5/9.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public abstract  class BaseActivity extends AppCompatActivity {


    protected ViewDataBinding binding;

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void loadData();

    //使用databinding
    public ViewDataBinding getBinding(){

        return binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,getLayoutId());
        initUI();
        initTheme();
        initViews(savedInstanceState);
        loadData();

    }

    /**
     * 初始化状态栏
     */
    private void initUI(){

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    /**
     * 设置主题
     */
    private void initTheme() {


    }


    /**
     * 显示加载进度条
     */

    public void showProgress() {

        ProxyLodingProgress.show(BaseActivity.this);
    }

    /**
     * 关闭加载进度条
     */

    public void hideProgress() {

        ProxyLodingProgress.hide();
    }
}
