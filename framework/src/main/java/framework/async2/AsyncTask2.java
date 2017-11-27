package framework.async2;

import framework.exception.TransactionException;
import framework.transaction2.RequestController2;
import framework.ui.LoadingDialog;
import framework.utils.LogUtils;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: 异步任务，每个任务只能被执行一次
 */

public abstract  class AsyncTask2 implements Runnable {
    private static final String TAG = AsyncTask2.class.getSimpleName();
    private final ResultListener2 mResultListener;
    private boolean mShowUi = false;
    private volatile boolean mIsShutDown = false;
    protected AsyncTask2(ResultListener2 resultListener) {
        mResultListener = resultListener;
    }

    public AsyncTask2(ResultListener2 resultListener, boolean showUi) {
        mResultListener = resultListener;
        mShowUi = showUi;
    }

    @Override
    public void run() {
        try{
           LogUtils.error(TAG,"doInBackground...");
            Object result = doInBackground();
            notifyResult(result,null);
        }catch (TransactionException te){
            LogUtils.error(TAG,"Fail to Execute task.",te);
            notifyResult(null,te);
        }catch (Throwable e){
            LogUtils.error(TAG,"Fail to Execute task.",e);
        }finally {
            if(mShowUi && !mIsShutDown){
                LoadingDialog.getInstance().dismissLoading();
            }
        }
    }

    /**
     * 需执行的异步任务内容
     * @return 异步任务结果
     * @throws TransactionException 执行任务时发生的异常
     */
    protected abstract Object doInBackground() throws TransactionException;

    public ResultListener2 getResultListener() {
        return mResultListener;
    }

    /**
     * 通知监听器执行结果
     * @param result
     * @param te
     */
    private void notifyResult(final Object result,final TransactionException te){
        if(mResultListener != null){
            try{
                RequestController2.getInstance().removeRequestAndTask(this);
                mResultListener.onExecute(result,te);

            }catch (Throwable e){
                LogUtils.error(TAG,"Fail to norify result.",e);
            }
        }
    }
}
