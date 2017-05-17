package com.hut.cwp.mcar.utils;

import android.app.Activity;

/**
 * Created by Adminis on 2016/10/29.
 */

public class ProxyLodingProgress {

    private static LoadingProgressBar loadingProgressBar;

    public static void show(Activity activity) {
        if (loadingProgressBar == null) {
            loadingProgressBar = new LoadingProgressBar(activity);

        } else {
            loadingProgressBar.show();
        }
    }

    public static void hide() {

        if (loadingProgressBar != null) {
            loadingProgressBar.hide();
        }
    }

    public static void destroy() {
        if (loadingProgressBar != null)
            loadingProgressBar = null;
    }


}
