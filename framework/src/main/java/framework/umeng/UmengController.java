package framework.umeng;

import android.app.Application;

import com.tylz.common.utils.LogManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * @author cxw
 * @date 2018/3/28
 * @des 分享相关控制器
 */

public class UmengController {
    private UMShareAPI mUMShareAPI;

    private static UmengController mInstance;

    public static UmengController getInstance() {
        if(mInstance == null){
            mInstance = new UmengController();
        }
        return mInstance;
    }

    /**
     * 初始化
     * @param application
     */
    public void init(Application application){
        Config.DEBUG = LogManager.IS_DEBUG;
        mUMShareAPI = UMShareAPI.get(application);
        UMConfigure.init(application, Key.UMENG_APPKEY, Key.UMENG_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, Key.UMENG_SECRET);
        shareInit(application);
    }
    /**
     * 分享初始化
     * @param application
     */
    private void shareInit(Application application){
        //微信
        PlatformConfig.setWeixin(Key.WEIXIN_ID,Key.WEIBO_KEY);
        //新浪微博
        PlatformConfig.setSinaWeibo(Key.WEIBO_KEY,Key.WEIBO_SECRET,Key.WEIBO_REDIRECTURL);
        //QQ和QQ空间
        PlatformConfig.setQQZone(Key.QQ_ID,Key.QQ_KEY);
        //对应平台没有安装的时候跳转转到应用商店下载 其中qq 微信会跳转到下载界面进行下载
        Config.isJumptoAppStore = true;

    }
}
