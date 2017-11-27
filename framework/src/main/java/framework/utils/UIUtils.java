package framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ArrayRes;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import framework.app.BaseApplication;
import framework.ui.widget.SearchView;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/20 16:42
 *  @描述：    TODO
 */
public class UIUtils {
    /**
     * dp-->px
     */
    public static int dp2Px(Context context, int dp) {
        //1.px/dp = density    ==> px和dp倍数关系
        //2.px/(ppi/160) = dp  ==>ppi

        float density = context.getResources().getDisplayMetrics().density; //1.5
        //        int ppi = getResources().getDisplayMetrics().densityDpi;//160 240 320

        int px = (int) (dp * density + .5f);
        return px;
    }

    /**
     * dp-->px
     */
    public static int dp2Px(int dp) {
        //1.px/dp = density    ==> px和dp倍数关系
        //2.px/(ppi/160) = dp  ==>ppi

        float density = BaseApplication.getInstance()
                                       .getResources()
                                       .getDisplayMetrics().density; //1.5
        //        int ppi = getResources().getDisplayMetrics().densityDpi;//160 240 320

        int px = (int) (dp * density + .5f);
        return px;
    }

    /**
     * px-->dp
     */
    public static int px2Dp(Context context, int px) {
        //1.px/dp = density    ==> px和dp倍数关系
        float density = context.getResources().getDisplayMetrics().density; //1.5
        int   dp      = (int) (px / density + .5f);
        return dp;
    }
    /**
     * px-->dp
     */
    public static int px2Dp( int px) {
        //1.px/dp = density    ==> px和dp倍数关系
        float density = BaseApplication.getInstance().getResources().getDisplayMetrics().density; //1.5
        int   dp      = (int) (px / density + .5f);
        return dp;
    }
    public static int getDimension(@DimenRes int resId) {
        return (int) (BaseApplication.getInstance().getResources().getDimension(resId) +0.5f);
    }

    /**
     * 获取target版本信息
     * @return
     */
    public static int getTargetSdkVersion() {
        int version = 0;
        try {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            PackageInfo    packageInfo    = packageManager.getPackageInfo(BaseApplication
                                                                                  .getInstance()
                                                                                         .getPackageName(),
                                                                          0);
            version = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            LogManager.logE("==========get package info error===========");
        }
        return version;
    }

    /**
     * 检查是否有权限
     * @param permission 权限名称 例如 {@link android.Manifest.permission#CALL_PHONE}
     * @return 如果权限开启返回true 如果没有返回false
     */
    public static boolean hasAppPermission(String permission) {
        if (TextUtils.isEmpty(permission)) {
            return true;
        }
        if (getTargetSdkVersion() < Build.VERSION_CODES.M) {
            return PackageManager.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(
                    BaseApplication.getInstance(),
                    permission);
        } else {
            return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                    BaseApplication.getInstance(),
                    permission);
        }
    }

    /**
     * 从context中获取Activity
     * @param context
     * @return
     */
    public static Activity getActivityFromContext(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivityFromContext(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static List<String> getListByRes(@ArrayRes int resId) {
        String[] strings = BaseApplication.getInstance().getResources().getStringArray(resId);
        return Arrays.asList(strings);
    }

    public static String getStringByRes(@StringRes int resId) {
        return BaseApplication.getInstance().getResources().getString(resId);
    }

    public static Handler getMainHandler() {
        return BaseApplication.getMainHandler();
    }
    /****
     * 关闭系统键盘
     *
     * @param focusEt
     */
    public static void hideSystemBoard(SearchView focusEt) {

            if (null == focusEt)
                return;
            Context ctx = focusEt.getContext();
            if (android.os.Build.VERSION.SDK_INT <= 10) {
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(focusEt.getWindowToken(), 0);
            } else {
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(focusEt.getWindowToken(), 0);
                try {
                    Class<EditText> cls = EditText.class;
                    Method          setSoftInputShownOnFocus;
                    setSoftInputShownOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                    setSoftInputShownOnFocus.setAccessible(true);
                    setSoftInputShownOnFocus.invoke(focusEt, false);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                try {
                    Class<EditText> cls = EditText.class;
                    Method setShowSoftInputOnFocus;
                    setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                    setShowSoftInputOnFocus.setAccessible(true);
                    setShowSoftInputOnFocus.invoke(focusEt, false);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
    }
}
