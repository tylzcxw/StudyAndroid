package framework.transaction;

import java.util.ArrayList;
import java.util.List;

import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.async.AsyncTask;
import com.tylz.common.utils.LogManager;

/**
 * Created by cxw on 2017/7/20.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/20
 * @描述: TODO
 */

public class RequestController {
    private static RequestController mInstance = null;

    /**
     * 任务记录
     */
    private  static List<AsyncTask> mTasks = null;
    /**
     * 请求记录
     */
    private static List<TransactionRequest> mRequests = null;

    private  RequestController(){
        super();
    }
    public synchronized static RequestController getInstance(){
        if(null == mInstance){
            mInstance = new RequestController();
            mTasks = new ArrayList<AsyncTask>();
            mRequests = new ArrayList<TransactionRequest>();
        }
        return  mInstance;
    }
    /**
     * 保存记录当前请求和任务
     *
     * @param request 请求
     * @param task    任务
     */
    public void addRequestAndTask(TransactionRequest request, AsyncTask task) {

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
}
