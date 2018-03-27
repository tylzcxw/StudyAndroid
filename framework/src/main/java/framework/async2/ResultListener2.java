package framework.async2;


import com.tylz.common.config.C;
import com.tylz.common.utils.LogManager;
import com.tylz.common.utils.NetUtils;

import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.exception.TransactionException;
import framework.exception.TransactionParamsCheckException;
import framework.exception.TransactionRepeatSendException;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: TODO
 */

public abstract  class ResultListener2<T> {
    protected  boolean mShowUi = true;
    protected  boolean mCallBackConnException = false;
    public boolean isShowUi(){
        return mShowUi;
    }
    public void setShowUi(boolean showUi){
        this.mShowUi = showUi;
    }

    /**
     * 执行完成后通知结果或错误
     * @param result 执行结果，当e不为空时该值无效
     * @param e 执行过程中发生的异常
     */
    public abstract  void onExecute(T result,Exception e);
    public void onRefused(TransactionException e){
        if(e instanceof TransactionParamsCheckException){
            if(C.F.LOCAL_PARAMS_CHECK_SHOW_DIALOG){
                return;
            }
            onExecute(null,e);
        }
        if(e instanceof TransactionRepeatSendException){
            onExecute(null,e);
        }
    }
    public void onShutDown(){
        if(!mCallBackConnException){
            return;
        }
        LogManager.logE("=======================onShutDown===============================");
        BaseActivity activity = ActivityManager.getInstance().getTopActivity();
        String errMsg = (NetUtils.isMobile(activity) || NetUtils.isWIFI(activity)) ? C.Error.NETWORK_ERROR :C.Error.NETWORK_ERROR_SHUT_DOWN;
        // TODO: 2017/7/20  错误码
        //String errorCode =
        TransactionException exception = new TransactionException(errMsg);
        onExecute(null,exception);
    }
}
