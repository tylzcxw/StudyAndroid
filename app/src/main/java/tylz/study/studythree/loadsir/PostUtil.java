package tylz.study.studythree.loadsir;

import android.os.Handler;

import framework.loadsir.callback.Callback;
import framework.loadsir.core.LoadService;
import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/28
 *  @描述：    TODO
 */
public class PostUtil {
    public static final int DELAY_TIME = 1000;
    public static void postCallbackDelayed(final LoadService loadService, final Class<? extends Callback> clazz){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.debug("postCallbackDelayed");
                loadService.showCallback(clazz);
            }
        },DELAY_TIME);
    }
    public static void postSuccessDelayed(final LoadService loadService) {
        postSuccessDelayed(loadService, DELAY_TIME);
    }

    public static void postSuccessDelayed(final LoadService loadService, long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadService.showSuccess();
            }
        }, delay);
    }
}
