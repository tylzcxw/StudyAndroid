package framework.app;

import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.BuildConfig;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;

import framework.loadsir.callback.EmptyCallback;
import framework.loadsir.callback.ErrorCallback;
import framework.loadsir.callback.LoadingCallback;
import framework.loadsir.callback.TimeoutCallback;
import framework.loadsir.core.LoadSir;
import framework.umeng.UmengController;
import framework.utils.ContextUtils;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/23 10:34
 *  @描述：    TODO
 */
public class BaseApplication extends MultiDexApplication {
    private static Handler mMainHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mMainHandler = new Handler();
        /**
         * 异常日志捕捉
         */
        CrashHandler.getInstance().initCrashHandler(getApplicationContext());
        ContextUtils.initialize(BaseApplication.getInstance());
        Utils.init(this);
        initLog();
        initCrash();
        initAssets();
        SDKInitializer.initialize(getApplicationContext());
        initLoadSir();
        Stetho.initializeWithDefaults(this);
        /**
         * 友盟分享初始化
         */
        UmengController.getInstance().init(this);
    }


    public static Handler getMainHandler() {
        return mMainHandler;
    }

    private void initLoadSir() {
        LoadSir.beginBuidler()
               .addCallback(new ErrorCallback())
               .addCallback(new EmptyCallback())
               .addCallback(new LoadingCallback())
               .addCallback(new TimeoutCallback())
               .setDefaultCallback(LoadingCallback.class)
               .commit();
    }

    private static BaseApplication mInstance;

    public static BaseApplication getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("IlleagelStateExp:instance is null,application error");
        }
        return mInstance;
    }

    public static void initLog() {
        LogUtils.Builder builder = new LogUtils.Builder().setLogSwitch(BuildConfig.DEBUG)//
                                                         // 设置log总开关，包括输出到控制台和文件，默认开
                                                         .setConsoleSwitch(BuildConfig.DEBUG)//
                                                         // 设置是否输出到控制台开关，默认开
                                                         .setGlobalTag(null)// 设置log全局标签，默认为空
                                                         // 当全局标签不为空时，我们输出的log全部为该tag，
                                                         // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                                                         .setLogHeadSwitch(true)// 设置log头信息开关，默认为开
                                                         .setLog2FileSwitch(false)//
                                                         // 打印log时是否存到文件的开关，默认关
                                                         .setDir("")// 当自定义路径为空时，写入应用的/cache/log/目录中
                                                         .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                                                         .setConsoleFilter(LogUtils.V)// log的控制台过滤器，和logcat过滤器同理，默认Verbose
                                                         .setFileFilter(LogUtils.V);// log文件过滤器，和logcat过滤器同理，默认Verbose
        LogUtils.d(builder.toString());
    }

    private void initCrash() {
        CrashUtils.init();
    }

    private void initAssets() {

    }
}
