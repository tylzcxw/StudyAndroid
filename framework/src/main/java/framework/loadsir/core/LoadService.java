package framework.loadsir.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import framework.loadsir.callback.Callback;
import framework.loadsir.callback.SuccessCallback;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public class LoadService<T> {
    private LoadLayout   mLoadLayout;
    private Convertor<T> mConvertor;

    LoadService(Convertor<T> convertor,
                TargetContext targetContext,
                Callback.OnReloadListener onReloadListener,
                LoadSir.Builder builder) {
        mConvertor = convertor;
        Context context    = targetContext.getContext();
        View    oldContent = targetContext.getOldContent();
        mLoadLayout = new LoadLayout(context, onReloadListener);
        mLoadLayout.addCallback(new SuccessCallback(oldContent, context, onReloadListener));
        ViewGroup parentView = targetContext.getParentView();
        if (parentView != null) {
            parentView.addView(mLoadLayout,
                               targetContext.getChildIndex(),
                               oldContent.getLayoutParams());
        }
        initCallback(builder);
    }

    private void initCallback(LoadSir.Builder builder) {
        List<Callback>            callbacks       = builder.getCallbacks();
        Class<? extends Callback> defaultCallback = builder.getDefaultCallback();
        if (callbacks != null && callbacks.size() > 0) {
            for (Callback callback : callbacks) {
                mLoadLayout.setupCallback(callback);
            }

        }
        if (defaultCallback != null) {
            mLoadLayout.showCallback(defaultCallback);
        }
    }

    public void showSuccess() {
        mLoadLayout.showCallback(SuccessCallback.class);
    }

    public void showCallback(Class<? extends Callback> callback) {
        mLoadLayout.showCallback(callback);
    }

    public void showWithConvertor(T t) {
        if (mConvertor == null) {
            throw new IllegalArgumentException("You haven't set the Convertor.");
        }
        mLoadLayout.showCallback(mConvertor.map(t));
    }

    public LoadLayout getLoadLayout() {
        return mLoadLayout;
    }
}
