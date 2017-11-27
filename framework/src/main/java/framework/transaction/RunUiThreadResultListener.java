package framework.transaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.example.commblib.config.C;
import com.example.commblib.utils.NetUtils;

import framework.app.ActivityManager;
import framework.async.ResultListener;
import framework.exception.TransactionException;
import framework.security.LoginUtils;
import framework.security.login.LoginResultListener;
import framework.ui.CcbDialog;
import framework.utils.LogManager;
import framework.utils.UIUtils;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/7/24
 * @描述: 可直接操作UI的通讯结果监听器
 */

public abstract class RunUiThreadResultListener<T> extends ResultListener<T> {
    protected Activity mActivity;

    public RunUiThreadResultListener(Context context) {
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
        private TransactionRequest mRequest;

        public HandleRunnable(T result, Exception e, HandleType handleType, TransactionRequest request) {
            mResult = result;
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
                //安全处理
                case HANDLE_SECURITY:
                    handleTopSecurityException(mRequest);
                    break;
                //登录超时
                case HANDLE_LOGIN_TIMEOUT:
                    handleLoginTimeoutException(mRequest);
                    break;
                //未登录
                case HANDLE_NOT_LOGIN:
                    handleNotLoginException(mRequest);
                    break;
                //交易级错误
                case HANDLE_TRANSATION_EXCEPTION:
                    handleTransException(mException);
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
                case HANDLE_TOP_ERROR:
                    handleTopErrorException(mException);
                    break;
                case HANDLE_TOP_FLUSH:
                    handleTopFlushException(mException);
                    break;
            }
        }
    }
    public void handleTopFlushException(Exception e){
        handleTransException(e);
    }
    public void handleTopErrorException(Exception e){
        handleTransException(e);
    }
    public void handleParseErrorException() {
        showErrorDialog(mActivity,C.Error.PARSE_ERROR);
    }

    public void handleDefaultException() {
        if(TextUtils.isEmpty(mResponseCode)){
            showErrorDialog(mActivity,C.Error.NETWORK_ERROR);
        }else{
            showErrorDialog(mActivity,C.Error.SER_ERR);
        }
    }

    public void handleTransException(Exception exception) {
        if (!(exception instanceof TransactionException)) {
            showErrorDialog(mActivity, exception.getMessage());
            return;
        }
        String msg = exception.getMessage();
        showErrorDialog(mActivity, msg);
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

    public final CcbDialog.OnClickConfirmListener getInnerErrDialogBtnAction() {
        return getErrorDialogBtnAction();
    }

    private CcbDialog.OnClickConfirmListener getErrorDialogBtnAction() {
        return null;
    }

    public final void handleInnerNotLoginException(TransactionRequest request){
        checkContext();
        if(null == mActivity){
            LogManager.logE("checked context is null");
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null,null,HandleType.HANDLE_NOT_LOGIN,request));
    }
    public void handleNotLoginException(TransactionRequest request) {
        handleLoginTimeoutException(request);
    }
    public void handleInnerLoginTimeoutException(TransactionRequest request){
        checkContext();
        if(null == mActivity){
            LogManager.logE("checked context is null");
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null,null,HandleType.HANDLE_LOGIN_TIMEOUT,request));
    }
    public void handleLoginTimeoutException(final TransactionRequest request) {
        if (!isShowUi()) {
            return;
        }
        // todo 登录完成重发交易
        LoginUtils.doLogin(mActivity, new LoginResultListener() {
            @Override
            public void onSuccess() {
                if (null == request) {
                    return;
                }
                request.send(RunUiThreadResultListener.this);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                handleLoginCancel();
            }
        });

    }

    public void handleTopSecurityException(TransactionRequest request) {
        //交易安全验证之类
        //...
    }

    /**
     * 结果返回
     *
     * @param result 结果
     * @param e      异常， 注意，onResult里面Exception永远为空,为了兼容ResultListener的onExecuted加上Exception
     */
    public abstract void onResult(T result, Exception e);

    private enum HandleType {
        /**
         * 正常
         */
        HANDLE_NORMAL,
        /**
         * 安全模块
         */
        HANDLE_SECURITY,
        /**
         * 登录超时
         */
        HANDLE_LOGIN_TIMEOUT,
        /**
         * 未登录
         */
        HANDLE_NOT_LOGIN,
        /**
         * 交易级错误
         */
        HANDLE_TRANSATION_EXCEPTION,
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
         * 顶层异常
         */
        HANDLE_TOP_ERROR,
        /**
         * 顶层冲号
         */
        HANDLE_TOP_FLUSH
    }

    /**
     * 取消登录操作
     */
    protected void handleLoginCancel() {

    }

    private TransactionException mException;

    /**
     * 处理超时
     */
    public final void handleInnerConnException(TransactionException e) {
        mException = e;
        checkContext();
        if (null == mActivity) {
            LogManager.logE("checked context is null");
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null, null, HandleType.HANDLE_CONNECT_TIMEOUT, null));
    }

    private String mResponseCode;
    public final void handleInnerDefaultException(){
        checkContext();
        if (null == mActivity) {
            LogManager.logE("checked context is null");
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null,null,HandleType.HANDLE_DEFAULT_EXCEPTION,null));
    }
    /**
     * 处理默认异常
     */
    public final void handleInnerDefaultException(String code) {
        checkContext();
        if (null == mActivity) {
            LogManager.logE("checked context is null");
            return;
        }
        mActivity.runOnUiThread(new HandleRunnable(null, null, HandleType.HANDLE_DEFAULT_EXCEPTION, null));
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
