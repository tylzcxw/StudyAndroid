package framework.transaction2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.example.commblib.config.C;
import com.example.commblib.utils.NetUtils;

import framework.app.ActivityManager;
import framework.async2.ResultListener2;
import framework.exception.TransactionException;
import framework.transaction.RequestController;
import framework.ui.CcbDialog;
import framework.utils.LogManager;
import framework.utils.UIUtils;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/7/24
 * @描述: 可直接操作UI的通讯结果监听器
 */

public abstract class RunUiThreadResultListener2<T> extends ResultListener2<T> {
    protected Activity mActivity;
    private String mResponseCode;

    public RunUiThreadResultListener2(Context context) {
        mActivity = UIUtils.getActivityFromContext(context);
    }

    private void checkContext() {
        if (null == mActivity) {
            LogManager.logE("Current Context is null");
            mActivity = ActivityManager.getInstance().getTopActivity();
        }
    }

    @Override
    public void onExecute(T result, Exception e) {
        checkContext();
        if (null == mActivity) {
            LogManager.logE("checked context is null");
            return;
        }
        if (!(mActivity instanceof Activity) || null == result) {
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(result, e, HandleType.HANDLE_NORMAL, null));
    }

    public void handleInnerDataNotParseException() {
        // TODO: 2017/9/26 暂时搁置
    }

    public void handleInnerDataNotParseError(String result) {
        checkContext();
        if (null == mActivity) {
            LogManager.logE("checked context is null");
            return;
        }
        if (!(mActivity instanceof Activity) || null == result) {
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null,result, null, HandleType.HANDLE_DATA_NOT_PARSE, null));
    }

    private class HandleRunnable implements Runnable {
        /**
         * 结果
         */
        private T mResult;
        /**
         * 异常
         */
        private Exception mException;
        /**
         * 处理类型
         */
        private HandleType mHandleType;
        /**
         * 请求
         */
        private TransactionRequest2 mRequest;
        private String mResultData;
        public HandleRunnable(T result, Exception e, HandleType handleType, TransactionRequest2 request) {
            mResult = result;
            mException = e;
            mHandleType = handleType;
            mRequest = request;
        }
        public HandleRunnable(T t,String result,Exception e, HandleType handleType, TransactionRequest2 request) {
            mResult = t;
            mResultData = result;
            mException = e;
            mHandleType = handleType;
            mRequest = request;
        }
        @Override
        public void run() {
            switch (mHandleType) {
                //正常返回
                case HANDLE_NORMAL:
                    onResult(mResult, mException);
                    break;
                case HANDLE_DATA_NOT_PARSE:
                    onResult(mResultData);
                    break;
                //超时
                case HANDLE_CONNECT_TIMEOUT:
                    handleConnTimeOutException();
                    break;
                //默认错误
                case HANDLE_DEFAULT_EXCEPTION:
                    handleDefaultException();
                    break;
                //解析错误
                case HANDLE_PARSE_ERROR:
                    handleParseErrorException();
                    break;

            }
        }
    }

    protected void onResult(String result) {

    }


    public void handleDefaultException() {
        if(TextUtils.isEmpty(mResponseCode)){
            showErrorDialog(mActivity,C.Error.NETWORK_ERROR);
        }else{
            showErrorDialog(mActivity,C.Error.SER_ERR);
        }
    }

    public void handleParseErrorException() {
        showErrorDialog(mActivity,C.Error.PARSE_ERROR);
    }

    protected void showErrorDialog(final Context context, String content) {
        if (!isShowUi()) {
            return;
        }
        CcbDialog.getInstance().showDialog(context, content, null, mOnClickConfirmListener);
    }

    private CcbDialog.OnClickConfirmListener mOnClickConfirmListener = new CcbDialog.OnClickConfirmListener() {
        @Override
        public void clickConfirm(Dialog dialog) {
            finishNotShownAct();
            if (null != getErrorDialogBtnAction()) {
                getErrorDialogBtnAction().clickConfirm(dialog);
            } else {
                dialog.dismiss();
            }
        }
    };

    protected void finishNotShownAct() {
        RequestController.getInstance().finishNotShownAct();
    }


    private CcbDialog.OnClickConfirmListener getErrorDialogBtnAction() {
        return null;
    }




    /**
     * 结果返回
     *
     * @param result 结果
     * @param e      异常， 注意，onResult里面Exception永远为空,为了兼容ResultListener的onExecuted加上Exception
     */
    public  void onResult(T result, Exception e){}

    private enum HandleType {
        /**
         * 正常
         */
        HANDLE_NORMAL,
        /**
         * 默认错误
         */
        HANDLE_DEFAULT_EXCEPTION,
        /**
         * 请求超时
         */
        HANDLE_CONNECT_TIMEOUT,
        /**
         * 解析异常
         */
        HANDLE_PARSE_ERROR,
        /**
         * 数据不解析
         */
        HANDLE_DATA_NOT_PARSE,

    }


    private TransactionException mException;
    public void handleInnerDefaultException(TransactionException e) {
        mException = e;
        checkContext();
        if (null == mActivity) {
            LogManager.logE("checked context is null");
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null, null, HandleType.HANDLE_DEFAULT_EXCEPTION, null));
    }
    /**
     * 处理超时
     */
    protected final void handleInnerConnException(TransactionException e) {
        mException = e;
        checkContext();
        if (null == mActivity) {
            LogManager.logE("checked context is null");
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null, null, HandleType.HANDLE_CONNECT_TIMEOUT, null));
    }
    protected void handleInnerParseErrorException() {
        checkContext();
        if(null == mActivity){
            LogManager.logE("checked context is null");
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null,null,HandleType.HANDLE_PARSE_ERROR,null));
    }

    public void handleConnTimeOutException() {
        String errMsg;
        if (null != mException) {
            errMsg = mException.getMessage();
        } else {
            errMsg = (NetUtils.isMobile(mActivity)) || (NetUtils.isWIFI(mActivity)) ? C.Error.NETWORK_ERROR : C.Error.NETWORK_NOT_CONN;
        }
        showErrorDialog(mActivity, errMsg);
    }
}
