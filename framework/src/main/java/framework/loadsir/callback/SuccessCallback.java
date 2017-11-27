package framework.loadsir.callback;

import android.content.Context;
import android.view.View;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public class SuccessCallback extends Callback {
    public SuccessCallback(View view, Context context, OnReloadListener onReloadListener) {
        super(view, context, onReloadListener);
    }

    @Override
    protected int onCreateView() {
        return 0;
    }
}
