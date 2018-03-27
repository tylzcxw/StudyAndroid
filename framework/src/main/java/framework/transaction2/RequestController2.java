package framework.transaction2;

import java.util.ArrayList;
import java.util.List;

import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.async2.AsyncTask2;
import com.tylz.common.utils.LogManager;

/**
 * Created by cxw on 2017/7/20.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/20
 * @描述: TODO
 */

public class RequestController2 {
    private static RequestController2 mInstance = null;

    /**
     * 任务记录
     */
    private  static List<AsyncTask2>        mTasks    = null;
    /**
     * 请求记录
     */
    private static List<TransactionRequest2> mRequests = null;

    private RequestController2(){
        super();
    }
    public synchronized static RequestController2 getInstance(){
        if(null == mInstance){
            mInstance = new RequestController2();
            mTasks = new ArrayList<AsyncTask2>();
            mRequests = new ArrayList<TransactionRequest2>();
        }
        return  mInstance;
    }
    /**
     * 保存记录当前请求和任务
     *
     * @param request 请求
     * @param task    任务
     */
    public void addRequestAndTask(TransactionRequest2 request, AsyncTask2 task) {

        //静默交易不记录
        if (!request.isShowUi())
            return;

        mTasks.add(task);
        mRequests.add(request);
        LogManager.logI("==================after add task====================" + mTasks.toString());
        LogManager.logI("==================after add request====================" + mRequests.toString());

    }
    public boolean canShutDownRequest(){
        boolean canShutDown = true;
        return canShutDown;
    }
    public void finishNotShownAct(){
        BaseActivity topActivity = ActivityManager.getInstance().getTopActivity();
        if(topActivity.isShow() || topActivity.isCanFinish()){
            return;
        }
        topActivity.finish();
    }

    public void removeRequestAndTask(AsyncTask2 asyncTask2) {
        int removeIndex = mTasks.indexOf(asyncTask2);
        if(-1 == removeIndex){
            return;
        }
        mTasks.remove(removeIndex);
        mRequests.remove(removeIndex);
        LogManager.logI("==================after remove task====================" + mTasks.toString());
        LogManager.logI("==================after remove request====================" + mRequests.toString());
    }
}
