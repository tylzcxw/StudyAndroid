package framework.loadsir.callback;

import android.content.Context;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public abstract class Callback implements Serializable {
    private View             mRootView;
    private Context          mContext;
    private OnReloadListener mOnReloadListener;

    public Callback() {

    }

    Callback(View view, Context context, OnReloadListener onReloadListener) {
        mRootView = view;
        mContext = context;
        mOnReloadListener = onReloadListener;
    }

    public Callback setCallback(View view, Context context, OnReloadListener onReloadListener) {
        mRootView = view;
        mContext = context;
        mOnReloadListener = onReloadListener;
        return this;
    }
    public View getRootView(){
        int resId = onCreateView();
        if(resId == 0 && mRootView != null){
            return mRootView;
        }
        mRootView = View.inflate(mContext,onCreateView(),null);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRetry(mContext,mRootView)){
                    return;
                }
                if(mOnReloadListener != null){
                    mOnReloadListener.onReload(v);
                }
            }
        });
        return mRootView;
    }
    protected  boolean onRetry(Context context,View view){
        return false;
    }
    public Callback copy(){
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(bao);
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
        ObjectInputStream  ois;
        Object obj = null;
        try {
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Callback)obj;
    }
    public interface OnReloadListener {
        void onReload(View v);
    }

    protected abstract int onCreateView();
}
