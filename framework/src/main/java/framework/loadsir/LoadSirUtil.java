package framework.loadsir;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import framework.loadsir.callback.Callback;
import framework.loadsir.core.LoadService;
import framework.loadsir.core.TargetContext;
import framework.utils.UIUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public class LoadSirUtil {
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static TargetContext getTargetContext(Object target) {
        ViewGroup contentParent;
        Context   context;
        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        } else if (target instanceof View) {
            View view = (View) target;
            contentParent = (ViewGroup) view.getParent();
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("The target must be within Activity, Fragment, " +
                                                       "View.");
        }
        int  childIndex = 0;
        int  childCount = contentParent == null ? 0 : contentParent.getChildCount();
        View oldContent;
        if (target instanceof View) {
            oldContent = (View) target;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    childIndex = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent != null ? contentParent.getChildAt(0) : null;
        }
        if (oldContent == null) {
            String errorMsg = String.format("enexpected error when register " + "LoadSir in %s",
                                            target.getClass().getSimpleName());
            throw new IllegalArgumentException(errorMsg);
        }
        if(contentParent != null){
            contentParent.removeView(oldContent);
        }
        return new TargetContext(context,contentParent,oldContent,childIndex);
    }
    public static final int DELAY_TIME = 1000;
    public static void postCallbackDelayed(final LoadService loadService, final Class<? extends Callback> clazz){
        UIUtils.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadService.showCallback(clazz);
            }
        }, DELAY_TIME);
    }
    public static void postSuccessDelayed(final LoadService loadService) {
        postSuccessDelayed(loadService, DELAY_TIME);
    }

    public static void postSuccessDelayed(final LoadService loadService, long delay) {
        UIUtils.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadService.showSuccess();
            }
        }, delay);
    }
}
