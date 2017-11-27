package framework.async;

import framework.exception.TransactionException;
import framework.utils.LogUtils;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: 异步任务，每个任务只能被执行一次
 */

public abstract  class AsyncTask implements Runnable {
    private static final String TAG = AsyncTask.class.getSimpleName();
    private final ResultListener mResultListener;
    private boolean mShowUi = false;
    private volatile boolean mIsShutDown = false;
    protected AsyncTask(ResultListener resultListener) {
        mResultListener = resultListener;
    }

    public AsyncTask(ResultListener resultListener, boolean showUi) {
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

            }
        }
    }

    /**
     * 需执行的异步任务内容
     * @return 异步任务结果
     * @throws TransactionException 执行任务时发生的异常
     */
    protected abstract Object doInBackground() throws TransactionException;

    public ResultListener getResultListener() {
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
                mResultListener.onExecute(result,te);

            }catch (Throwable e){
                LogUtils.error(TAG,"Fail to norify result.",e);
            }
        }
    }
}
