package framework.transaction;


import com.tylz.common.config.C;
import com.tylz.common.httpconnection.MbsResult;
import com.tylz.common.utils.LogManager;
import com.tylz.common.utils.NetUtils;

import java.util.List;
import java.util.Map;

import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.async.ResultListener;
import framework.exception.TransactionException;

/**
 * Created by cxw on 2017/7/24.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/24
 * @描述: TODO
 */

public abstract class BaseTransactionResponse extends TransactionResponse {
    protected ResultListener mResultListener;
    protected boolean isUiThreadListener = false;
    protected  String mTxCode;
    protected TransactionRequest mRequest;
    private Map<String,List<String>> mHeaderMap;

    @Override
    public <T> T parseResult(MbsResult mbsResult, TransactionRequest request, String txCode, ResultListener resultListener) throws TransactionException {
        mResultListener = resultListener;
        isUiThreadListener = mResultListener instanceof RunUiThreadResultListener;
        mTxCode = txCode;
        mRequest = request;
        int responseCode = mbsResult.getResponseCode();
        String result = mbsResult.getStrContent();
        if(200 == responseCode || 701 == responseCode || 702 == responseCode){
            LogManager.logD("================正常业务逻辑 code=" + responseCode + "=================");
            T t = parseNormal(mbsResult);
            if(null != t && t instanceof BaseTransactionResponse){
                ((BaseTransactionResponse) t).mHeaderMap = mbsResult.getHeader();
                ((BaseTransactionResponse) t).mResultTime = System.currentTimeMillis();

            }
            return t;
        }
        //超时处理
        if(mbsResult.getConnExp() != null){
            BaseActivity topActivity = ActivityManager.getInstance().getTopActivity();
            String errMsg = NetUtils.isMobile(topActivity) || NetUtils.isWIFI(topActivity) ? C.Error.NETWORK_ERROR : C.Error.NETWORK_NOT_CONN;
            TransactionException exception = new TransactionException(errMsg);
            if(isUiThreadListener){
                LogManager.logD("================超时处理 交给RunUiThreadResultListener处理===============");
                ((RunUiThreadResultListener)mResultListener).handleInnerConnException(exception);
                return null;
            }else{
                LogManager.logD("================超时处理抛出默认处理===============");
                throw  exception;
            }
        }else{
            //默认错误处理
            String code = String.valueOf(responseCode);
            if(isUiThreadListener){
                LogManager.logD("====================code："+code+"交给RunUiThreadResultListener处理=========================");
                ((RunUiThreadResultListener) mResultListener).handleInnerDefaultException(code);
                return null;
            }else{
                LogManager.logD("====================code："+code+"抛出默认处理=========================");
                throw new TransactionException(C.Error.SER_ERR,null,code);
            }
        }
    }
    public abstract <T> T parseNormal(MbsResult mbsResult) throws TransactionException;
    public List<String> getHeaderMapValueByKey(String key){
        if(null == this.mHeaderMap){
            return null;
        }
        return this.mHeaderMap.get(key);
    }
    public Map<String,List<String>> getHeaderMap(){
        return this.mHeaderMap;
    }
    private long mResultTime = 0;
    public long getResultTime(){
        return mResultTime;
    }
}
