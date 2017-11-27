package framework.loadsir.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public class TargetContext {
    private Context   mContext;
    private ViewGroup mParentView;
    private View      mOldContent;
    private int       mChildIndex;

    public TargetContext(Context context, ViewGroup parentView, View oldContent, int childIndex) {
        mContext = context;
        mParentView = parentView;
        mOldContent = oldContent;
        mChildIndex = childIndex;
    }

    public Context getContext() {
        return mContext;
    }

     ViewGroup getParentView() {
        return mParentView;
    }

    View getOldContent() {
        return mOldContent;
    }

     int getChildIndex() {
        return mChildIndex;
    }
}
