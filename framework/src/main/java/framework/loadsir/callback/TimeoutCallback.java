package framework.loadsir.callback;

import android.content.Context;
import android.view.View;

import com.tylz.framework.R;
import com.tylz.myutils.comm.ToastUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public class TimeoutCallback extends Callback{
    @Override
    protected int onCreateView() {
        return R.layout.view_error;
    }

    @Override
    protected boolean onRetry(Context context, View view) {
        ToastUtils.showShortSafe("请检查网络连接后重试！");
        return false;
    }
}
