package com.tylz.myutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

public final class MyUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private MyUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        MyUtils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }
}
