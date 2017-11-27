package framework.async;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cxw on 2017/7/20.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/20
 * @描述: 异步任务执行程序
 */

public class AsyncTaskExecutor {
    /** 任务执行延迟毫秒数，为了在此期间可以取消该任务考虑 */
    private static final long DELAY_MILLISECONDS = 200;

    /** 最大并行线程数 */
    private static final int MAX_THREAD_COUNT = Thread.NORM_PRIORITY;

    private static final int LOW_LEVEL_MAX_THREAD_COUNT = Thread.MAX_PRIORITY;
    private static AsyncTaskExecutor mInstance;
    private ScheduledThreadPoolExecutor mExecutor = null;
    private ScheduledThreadPoolExecutor mLowPriorityExecutor = null;
    public static synchronized  AsyncTaskExecutor getInstance(){
        if(mInstance == null){
            mInstance = new AsyncTaskExecutor();
        }
        return mInstance;
    }
    private AsyncTaskExecutor(){
        mExecutor = new ScheduledThreadPoolExecutor(MAX_THREAD_COUNT);
        mLowPriorityExecutor = new ScheduledThreadPoolExecutor(LOW_LEVEL_MAX_THREAD_COUNT);
    }
    /**
     * 延时执行任务
     *
     * @param task
     */
    public void execute(AsyncTask task){
        if(null == mExecutor || mExecutor.isShutdown()){
            mExecutor = new ScheduledThreadPoolExecutor(MAX_THREAD_COUNT);
        }
        mExecutor.schedule(task,DELAY_MILLISECONDS, TimeUnit.MILLISECONDS);
    }
    /**
     * 延时执行低优先级任务
     *
     * @param task
     */
    public void executeLowPriorityTask(AsyncTask task) {
        if(null == mLowPriorityExecutor || mLowPriorityExecutor.isShutdown()){
            mLowPriorityExecutor =new ScheduledThreadPoolExecutor(LOW_LEVEL_MAX_THREAD_COUNT);
        }
        mLowPriorityExecutor.schedule(task, DELAY_MILLISECONDS, TimeUnit.MILLISECONDS);
    }
    /**
     * 撤销当前未执行的任务
     */
    public void cancel() {
        mExecutor.shutdown();
    }
    /**
     * 撤销当前未执行的低优先级任务
     */
    public void cancelLowPriority() {
        mLowPriorityExecutor.shutdown();
    }
}
