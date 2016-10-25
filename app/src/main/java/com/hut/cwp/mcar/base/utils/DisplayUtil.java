package com.hut.cwp.mcar.base.utils;

import android.content.Context;

/**
 * Created by Adminis on 2016/10/19.
 */

public class DisplayUtil {

    public static int px2dip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);


    }

}
