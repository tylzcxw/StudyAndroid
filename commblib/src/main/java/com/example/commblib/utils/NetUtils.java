package com.example.commblib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by cxw on 2017/7/20.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/20
 * @描述: TODO
 */

public class NetUtils {
    /**
     * 是否3G网络连接
     *
     * @param c
     */
    public static boolean isMobile(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }
    /**
     * 是否WIFI连接
     *
     * @param c
     */
    public static boolean isWIFI(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();// 获取网络的连接情况

        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return true;
        return false;
    }
}
