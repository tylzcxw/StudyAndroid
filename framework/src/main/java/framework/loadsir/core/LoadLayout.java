package framework.loadsir.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import framework.loadsir.LoadSirUtil;
import framework.loadsir.callback.Callback;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public class LoadLayout extends FrameLayout{
    private Map<Class<? extends Callback>,Callback> mCallbacks = new HashMap<>();
    private Context mContext;
    private Callback.OnReloadListener mOnReloadListener;
    public LoadLayout(@NonNull Context context) {
        super(context);
    }
    public LoadLayout(@NonNull Context context,Callback.OnReloadListener onReloadListener){
        this(context);
        mContext = context;
        mOnReloadListener = onReloadListener;
    }
    public void setupCallback(Callback callback){
        Callback cloneCallback = callback.copy();
        cloneCallback.setCallback(null,mContext,mOnReloadListener);
        addCallback(cloneCallback);
    }
    public void addCallback(Callback callback){
        if(!mCallbacks.containsKey(callback.getClass())){
            mCallbacks.put(callback.getClass(),callback);
        }
    }
    public void showCallback(Class<? extends Callback> callback){
        if(!mCallbacks.containsKey(callback)){
            String className = String.format("The Callback (%s) is nonexistent.", callback.getSimpleName());
            throw new IllegalArgumentException(className);
        }
        if(LoadSirUtil.isMainThread()){
            showCallbackView(callback);
        }else{
            postToMainThread(callback);
        }
    }

    private void showCallbackView(Class<? extends Callback> callback) {
        if(getChildCount() > 0){
            removeAllViews();
        }
        for(Class key : mCallbacks.keySet()){
            if(key == callback){
                addView(mCallbacks.get(key).getRootView());
            }
        }
    }

    private void postToMainThread(final Class<? extends Callback> callback) {
        post(new Runnable() {
            @Override
            public void run() {
                showCallbackView(callback);
            }
        });
    }
}
