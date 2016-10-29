package com.hut.cwp.mcar.cwp.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;

import com.hut.cwp.mcar.R;
import com.hut.cwp.mcar.cwp.view.ProgressWebView;

public class WebViewContentActivity extends Activity {

    private ProgressWebView webview;
    private String URL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cwp_activity_webview_content);

        // ~~~ 获取参数
        URL = getIntent().getStringExtra("URL");

        if(URL==null){
            URL = "https://www.baidu.com/";
        }
        // ~~~ 绑定控件
        webview = (ProgressWebView) findViewById(R.id.webview);

        // ~~~ 设置数据

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        //加载需要显示的网页
        webview.loadUrl(URL);
    }

}
