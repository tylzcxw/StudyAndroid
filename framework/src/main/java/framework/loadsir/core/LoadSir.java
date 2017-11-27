package framework.loadsir.core;

import java.util.ArrayList;
import java.util.List;

import framework.loadsir.LoadSirUtil;
import framework.loadsir.callback.Callback;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public class LoadSir {
    private static volatile LoadSir mLoadSir;
    private                 Builder mBuilder;

    public static LoadSir getDefault() {
        if (mLoadSir == null) {
            synchronized (LoadSir.class) {
                if (mLoadSir == null) {
                    mLoadSir = new LoadSir();
                }
            }
        }
        return mLoadSir;
    }

    private LoadSir() {
        this.mBuilder = new Builder();
    }

    private LoadSir(Builder builder) {
        mBuilder = builder;
    }

    private void setBuilder(Builder builder) {
        mBuilder = builder;
    }

    public LoadService register(Object target, Callback.OnReloadListener onReloadListener) {
        return register(target, onReloadListener, null);
    }

    public <T> LoadService register(Object target,
                                    Callback.OnReloadListener onReloadListener,
                                    Convertor<T> convertor) {
        TargetContext targetContext = LoadSirUtil.getTargetContext(target);
        return new LoadService(convertor, targetContext, onReloadListener, mBuilder);
    }

    public static Builder beginBuidler() {
        return new Builder();
    }

    public static class Builder {
        private List<Callback> mCallbacks = new ArrayList<>();
        private Class<? extends Callback> mDefaultCallback;

        public Builder addCallback(Callback callback) {
            mCallbacks.add(callback);
            return this;
        }

        public Builder setDefaultCallback(Class<? extends Callback> defaultCallback) {
            mDefaultCallback = defaultCallback;
            return this;
        }

        List<Callback> getCallbacks() {
            return mCallbacks;
        }

        Class<? extends Callback> getDefaultCallback() {
            return mDefaultCallback;
        }

        public void commit() {
            getDefault().setBuilder(this);
        }

        public LoadSir build() {
            return new LoadSir(this);
        }
    }
}
